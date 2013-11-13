package kr.okplace.job.common;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

public class VelocityItemWriter<T> implements ItemWriter<T>, StepExecutionListener, InitializingBean {

	private static final Log log = LogFactory.getLog(VelocityItemWriter.class);
	
	private VelocityEngine velocityEngine;
	private Map<String, Object> context;
	private Resource template;
	private Resource resource;
	private String encoding = "UTF-8";
	
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	
	public void setTemplate(Resource template) {
		this.template = template;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		Assert.notNull(velocityEngine, "A velocityEngine must be provided.");
		Assert.notNull(template, "A template must be provided.");
		Assert.notNull(resource, "A resource must be provided.");

		velocityEngine.init();
		
		if(context==null) {
			context = new HashMap<String, Object>();
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return ExitStatus.COMPLETED;
	}

	@Override
	public void write(List<? extends T> items) throws Exception {
		
		context.put("items", items);

		String location = template.getFilename();

		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resource.getFile()), encoding));
			VelocityEngineUtils.mergeTemplate(velocityEngine, location, encoding, context, writer);
			writer.flush();
		}
		catch (UnsupportedEncodingException e) {
			log.error("Unsupported Encoding[" + encoding + "]!", e);
		}
		catch (FileNotFoundException e) {
			log.error("File[" + resource.getFilename() + "] Not Found!", e);
		}
		catch (IOException e) {
			log.error("Can't open file[" + resource.getFilename() + "]!", e);
		}
		finally {
			if(writer!=null) {
				writer.close();
			}
		}
		
	}
}
