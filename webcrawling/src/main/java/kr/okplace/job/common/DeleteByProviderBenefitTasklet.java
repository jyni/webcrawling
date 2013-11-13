package kr.okplace.job.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.okplace.benefit.domain.ProviderBenefit;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * <h1>유입처 Class(ProviderBenefit)의 Data를 이용해 혜택관련 Data를 삭제하는 Class</h1>
 * 
 * <p>유입처 Class(ProviderBenefit)의 Data(categDepth1, categDepth2, categDepth3)를 이용해
 * 혜택관련 Data(BENEFIT, MERCHANT, LOCATION)를 삭제하는 Class.</p>
 * <p>join table(MERCHANT_LOCATION, BENEFIT_LOCATION)의 Data는 DB 설정(cascade on delete)으로 삭제.</p>
 * 
 * @author Superman
 * @see org.springframework.batch.core.step.tasklet.Tasklet
 * @see DeleteByProviderBenefitTasklet
 * @see MerchantLocationBenefitMapper#deleteByProviderBenefit(ProviderBenefit)
 */
public class DeleteByProviderBenefitTasklet implements Tasklet, InitializingBean {

	/**
	 * <p>유입처의 Category Pattern</p>
	 * <code>^(2\\d\\d)(?:\\W?(1\\d\\d)(?:\\W?(\\d{3}))?)?$</code>
	 */
	public static final Pattern CATEG_PAIR_PATTERN = Pattern.compile("^(2\\d\\d)(?:\\W?(1\\d\\d)(?:\\W?(\\d{3}))?)?$");

	@Autowired
	private MerchantLocationBenefitMapper mapper;
	
	private List<ProviderBenefit> providerBenefits;

	/**
	 * <p>삭제 대상 혜택의 유입처 정보(PROVIDER_BENEFIT)를 목록에 추가</p>
	 * <p>유입처 키를 찾아오기 위한 혜택 카테고리(categDepth1, categDepth2, categDepth3)정보를 ProviderBenefit Instance로 추가한다.</p>
	 * 
	 * @param providerBenefit 목록에 추가할 삭제 대상 혜택의 유입처 정보(PROVIDER_BENEFIT)
	 */
	private void addProviderBenefit(ProviderBenefit providerBenefit) {
		
		if(providerBenefits==null) {
			providerBenefits = new ArrayList<ProviderBenefit>();
		}
		
		this.providerBenefits.add(providerBenefit);
	}

	/**
	 * <p>삭제 대상 혜택의 유입처 정보(PROVIDER_BENEFIT)중 관련 혜택 카테고리를 목록에 추가</p>
	 * <p>유입처 키를 찾아오기 위한 혜택 카테고리(categDepth1, categDepth2, categDepth3)정보를 추가한다.</p>
	 * 
	 * @param pair
	 */
	private void addBenefitCategoryPair(String pair) {
		
		Matcher m = CATEG_PAIR_PATTERN.matcher(pair);
		Assert.isTrue(m.matches(), "Property[benefitCategoryPair] must be patten[" + CATEG_PAIR_PATTERN.pattern() + "]!");
		
		ProviderBenefit providerBenefit = new ProviderBenefit();
		providerBenefit.setCategoryDepth2(m.group(1));
		providerBenefit.setCategoryDepth1(m.group(2));
		providerBenefit.setCategoryDepth3(m.group(3));
		
		addProviderBenefit(providerBenefit);
	}

	/**
	 * @param providerBenefit
	 */
	public void setProviderBenefit(ProviderBenefit providerBenefit) {
		addProviderBenefit(providerBenefit);
	}

	/**
	 * @param providerBenefits
	 */
	public void setProviderBenefits(List<ProviderBenefit> providerBenefits) {
		this.providerBenefits = providerBenefits;
	}
	
	/**
	 * @param pair
	 */
	public void setBenefitCategoryPair(String pair) {

		Assert.notNull(pair, "Property[benefitCategoryPair] must be not null!");

		addBenefitCategoryPair(pair);
	}
	
	/**
	 * @param triplet
	 */
	public void setBenefitCategoryPairs(List<String> triplet) {

		Assert.notEmpty(triplet, "Property[benefitCategoryPairs] must be not empty!");

		for(String pair: triplet.get(0).split(",\\s*")) {
			addBenefitCategoryPair(pair);
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notEmpty(providerBenefits, "Property[benefitCategoryPair or benefitCategoryPairs] must be set!");
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		for(ProviderBenefit providerBenefit: providerBenefits) {
			mapper.deleteByProviderBenefit(providerBenefit);
		}
		
//		mapper.deleteOrphanedMerchant();
		
		return RepeatStatus.FINISHED;
	}

}
