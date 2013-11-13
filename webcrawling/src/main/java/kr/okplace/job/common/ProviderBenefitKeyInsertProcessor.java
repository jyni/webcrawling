package kr.okplace.job.common;

import kr.okplace.benefit.domain.BenefitBuilder;
import kr.okplace.benefit.domain.ProviderBenefit;
import kr.okplace.benefit.domain.ProviderBenefitHolder;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * <h1>유입처키를 혜택에 매핑하기 위한 Processor</h1>
 * <p>유입처키(PROVIDER_BENEFIT_SEQ)를 혜택(BENEFIT.PROVIDER_BENEFIT_SEQ)에 매핑하기 위한 Processor.</p>
 * <p>유입처(PROVIDER_BENEFIT)의 카테고리 분류(CATEG_DEPTH1, CATEG_DEPTH2, CATEG_DEPTH3)를 이용하여
 *  유입처키(PROVIDER_BENEFIT_SEQ)를 찾아와서 혜택(BENEFIT.PROVIDER_BENEFIT_SEQ)에 매핑한다.</p>
 * <p>유입처(PROVIDER_BENEFIT)의 카테고리 분류(CATEG_DEPTH1, CATEG_DEPTH2, CATEG_DEPTH3)값은 spring context property값으로 받아온다.</p>
 * <p>Spring 설정 예</p>
 * <code>
	&lt;bean id="wemakepriceItemProcessor" class="kr.okplace.job.common.ProviderBenefitKeyInsertProcessor"&gt;
		&lt;property name="benefitCategoryDepth1" value="${benefitCategoryDepth1.social}" /&gt;
		&lt;property name="benefitCategoryDepth2" value="${benefitCategoryDepth2.wemakeprice}" /&gt;
	&lt;/bean&gt;
 * </code>
 *  
 * @author Superman
 * @see #process(ProviderBenefitHolder)
 * @see org.springframework.batch.item.ItemProcessor
 * @see ProviderBenefitKeyMapInsertProcessor
 * @see kr.okplace.benefit.domain.ProviderBenefitHolder
 */
public class ProviderBenefitKeyInsertProcessor implements ItemProcessor<ProviderBenefitHolder, ProviderBenefitHolder>, InitializingBean {

	@Autowired
	private MerchantLocationBenefitMapper mapper;

	private Integer providerBenefitKey;
	
	private String benefitCategoryDepth1;
	private String benefitCategoryDepth2;
	private String benefitCategoryDepth3;

	public void setBenefitCategoryDepth1(String benefitCategoryDepth1) {
		this.benefitCategoryDepth1 = benefitCategoryDepth1;
	}

	public void setBenefitCategoryDepth2(String benefitCategoryDepth2) {
		this.benefitCategoryDepth2 = benefitCategoryDepth2;
	}

	public void setBenefitCategoryDepth3(String benefitCategoryDepth3) {
		this.benefitCategoryDepth3 = benefitCategoryDepth3;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(benefitCategoryDepth1, "Property[benefitCategoryDepth1] must be set!");
		Assert.notNull(benefitCategoryDepth2, "Property[benefitCategoryDepth2] must be set!");
	}

	private Integer getProviderBenefitKey(ProviderBenefit providerBenefit) {
		
		if(providerBenefitKey==null) {
			this.providerBenefitKey = mapper.selectProviderBenefitKyByBenefitCategoryPair(providerBenefit);
		}
		
		return providerBenefitKey;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public ProviderBenefitHolder process(ProviderBenefitHolder item) throws Exception {

		ProviderBenefit providerBenefit = new ProviderBenefit();
		providerBenefit.setCategoryDepth1(benefitCategoryDepth1);
		providerBenefit.setCategoryDepth2(benefitCategoryDepth2);
		providerBenefit.setCategoryDepth3(benefitCategoryDepth3);
		providerBenefit.setKey(getProviderBenefitKey(providerBenefit));
		
		item.setProviderBenefit(providerBenefit);
		if(item instanceof BenefitBuilder) {
			((BenefitBuilder)item).buildBenefit();
		}

		return item;
	}
}
