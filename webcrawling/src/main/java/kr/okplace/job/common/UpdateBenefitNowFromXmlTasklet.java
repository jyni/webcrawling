package kr.okplace.job.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.okplace.benefit.domain.BenefitInterface;
import kr.okplace.benefit.domain.ProviderBenefit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.Assert;

/**
 * @author Superman
 */
@Scope("prototype")
public class UpdateBenefitNowFromXmlTasklet implements Tasklet, StepExecutionListener, InitializingBean {
	
	private static final Log log = LogFactory.getLog(UpdateBenefitNowFromXmlTasklet.class);

	private static final Pattern CATEG_PAIR_PATTERN = Pattern.compile("^(2\\d\\d)\\W?(1\\d\\d|\\*)$");
	private static final String INPUT_FILE_NAME = "current";

	@Autowired
	private MerchantLocationBenefitMapper mapper;

	private ItemReader<BenefitInterface> reader;
	private ItemWriter<BenefitInterface> deleter;
	private ItemWriter<BenefitInterface> inserter;
	
	private ProviderBenefit providerBenefit;
	private URL resource;
	private FileSystemResource current;
//	private String deleteStatementId;
//	private String insertStatementId;
	
	List<BenefitInterface> backups;

	public void setReader(ItemReader<BenefitInterface> reader) {
		this.reader = reader;
	}

	public void setDeleter(ItemWriter<BenefitInterface> deleter) {
		this.deleter = deleter;
	}

	public void setInserter(ItemWriter<BenefitInterface> inserter) {
		this.inserter = inserter;
	}

	public void setBenefitCategoryPair(String pair) {
		
		Matcher m = CATEG_PAIR_PATTERN.matcher(pair);
		Assert.isTrue(m.matches(), "Property[benefitCategoryPair] must be patten[" + CATEG_PAIR_PATTERN.pattern() + "]!");

		if(providerBenefit==null) {
			providerBenefit = new ProviderBenefit();
		}
		
		providerBenefit.setCategoryDepth2(m.group(1));
		providerBenefit.setCategoryDepth1(m.group(2));
	}

	public void setProviderBenefitKey(Integer providerBenefitKey) {

		Assert.notNull(providerBenefitKey, "Property[providerBenefitKey] must be an integer!");
		

		if(providerBenefit==null) {
			providerBenefit = new ProviderBenefit();
		}
		
		providerBenefit.setKey(providerBenefitKey);
	}

	public void setResource(String resource) throws MalformedURLException {
		this.resource = new URL(resource);
	}

	public void setCurrent(String current) {
		this.current = new FileSystemResource(current);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(providerBenefit, "Property[providerBenefitKey or benefitCategoryPair] must be set!");
		Assert.notNull(resource, "Property[resource] must be set!");
		Assert.notNull(current, "property[current] must be set!");
		Assert.notNull(reader, "property[reader] must be set!");
		Assert.notNull(inserter, "property[inserter] must be set!");
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		
		ExecutionContext stepContext = stepExecution.getExecutionContext();
		stepContext.putString(INPUT_FILE_NAME, "file:///" + current.getFile().getAbsolutePath());
		
		if(current.exists()) {
			
			if(log.isInfoEnabled()) {
				log.info("start reading backup path[" + current.getPath() + "]!");
			}

			readBackups(stepContext);
			
			if(log.isInfoEnabled()) {
				log.info("end reading backup file[" + current.getPath() + "]!");
			}
		}
		
		writeBackups(stepContext);
	}

	private void readBackups(ExecutionContext stepContext) {
		
		try {
			open(reader);
			backups = new ArrayList<BenefitInterface>();
			BenefitInterface backup;
			while ((backup = reader.read()) != null) {
				backups.add(backup);
			}
			
			stepContext.put("backups", backups);
		}
		catch (UnexpectedInputException e) {
			log.error("input file[" + current.getPath() + "] not expected!", e);
		}
		catch (ParseException e) {
			log.error("input file[" + current.getPath() + "] can't parsed!", e);
		}
		catch (NonTransientResourceException e) {
			log.error("input file[" + current.getPath() + "] not transient?", e);
		}
		catch (Exception e) {
			log.error("input file[" + current.getPath() + "] error!", e);
		}
		finally {
			close(reader);
		}
	}

	private void writeBackups(ExecutionContext stepContext) {
		
		InputStreamReader is;
		OutputStreamWriter os = null;
		try {
			os = new OutputStreamWriter(current.getOutputStream(), "UTF-8");
			is = new InputStreamReader(resource.openStream(), "UTF-8");
			char[] c = new char[1];
			while(is.read(c)>=0) {
				os.write(c);
			}
		}
		catch (IOException e) {
			log.error("input file[" + resource.getPath() + "] or output file[" + current.getPath() + "] error!", e);
		}
		finally {
			if(os!=null) try {
				os.close();
			}
			catch (IOException e) {}
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return ExitStatus.COMPLETED;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		
		ExecutionContext stepContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();

		if(backups==null) {
			
			if(log.isDebugEnabled()) {
				log.info("New Instance[" + this.hashCode() + "]!");
			}
			
			backups = (List<BenefitInterface>) stepContext.get("backups");
		}

		List<BenefitInterface> insertItems = new ArrayList<BenefitInterface>();
		List<BenefitInterface> deleteItems = new ArrayList<BenefitInterface>();
		try {
			open(reader);
			BenefitInterface benefit;
			BenefitInterface backup;
			String externalKey;
			while ((benefit = reader.read()) != null) {
				
				externalKey = benefit.getBenefitExternalKey();
				if(externalKey!=null && externalKey.length()>0) {

					backup = getEqualExternalKeyItem(externalKey);
					// 1. externalKey.
					if(backup==null || !backup.equals(benefit)) {
						insertItems.add(benefit);
					}
/*				// 2. externalKey.
					else if(!backup.equals(Benefit)) {
						deleteItems.add(backup.getExternalKey());
						insertItems.add(Benefit);
					}
*/				// 3-1.
					else {
						backups.remove(backup);
					}
				}
			}
		}
		catch (UnexpectedInputException e) {
			log.error("input file[" + current + "] not expected!", e);
		}
		catch (ParseException e) {
			log.error("input file[" + current + "] can't parsed!", e);
		}
		catch (NonTransientResourceException e) {
			log.error("input file[" + current + "] not transient?", e);
		}
		catch (Exception e) {
			log.error("input file[" + current + "] error!", e);
		}
		finally {
			close(reader);	
		}

		// 3-2. backups.
		if(deleter!=null && backups!=null) for(BenefitInterface b: backups) {
			deleteItems.add(b);
		}

		if(log.isDebugEnabled()) {
			log.debug("deleteItems.size():" + deleteItems.size());
			log.debug("insertItems.size():" + insertItems.size());
		}
		
		try {
			Integer providerBenefitKey = providerBenefit.getKey();
			if(providerBenefitKey==null) {
				providerBenefitKey = mapper.selectProviderBenefitKyByBenefitCategoryPair(providerBenefit);
				providerBenefit.setKey(providerBenefitKey);
			}
			
			if(deleteItems.size() > 0) {
				
				for(BenefitInterface key: deleteItems) {
					key.setProviderBenefitKey(providerBenefitKey);
				}
				
				deleter.write(deleteItems);
			}
			
			if(insertItems.size() > 0) {
				
				for(BenefitInterface benefit: insertItems) {
					benefit.setProviderBenefitKey(providerBenefitKey);
				}
				
				inserter.write(insertItems);
			}
		}
		catch (Exception e) {
			log.error("input file[" + current + "]!", e);
		}

		return RepeatStatus.FINISHED;
	}

	private BenefitInterface getEqualExternalKeyItem(String externalKey) {

		if(backups==null || backups.isEmpty()) {
			return null;
		}
		
		String key;
		for(BenefitInterface b: backups) {
			key = b.getBenefitExternalKey();
			if(key.equals(externalKey)) {
				return b;
			}
		}

		return null;
	}

	/**
	 * Open the reader if applicable.
	 */
	private void open(ItemReader<?> reader) {
		if (reader instanceof ItemStream) {
			((ItemStream) reader).open(new ExecutionContext());
		}
	}

	/**
	 * Close the reader if applicable.
	 */
	private void close(ItemReader<?> reader) {
		if (reader instanceof ItemStream) {
			((ItemStream) reader).close();
		}
	}
}
