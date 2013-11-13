package kr.okplace.job.common;

import java.util.HashMap;
import java.util.Map;

import kr.okplace.benefit.domain.BenefitBuilder;
import kr.okplace.benefit.domain.ProviderBenefit;
import kr.okplace.benefit.domain.ProviderBenefitHolder;
import kr.okplace.benefit.domain.ProviderBenefitHolderWithCategory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <h1>유입처키를 혜택에 매핑하기 위한 Processor</h1>
 * <p>유입처키(PROVIDER_BENEFIT_SEQ)를 혜택(BENEFIT.PROVIDER_BENEFIT_SEQ)에 매핑하기 위한 Processor.</p>
 * <p>유입처(PROVIDER_BENEFIT)의 카테고리 분류(CATEG_DEPTH1, CATEG_DEPTH2, CATEG_DEPTH3)를 이용하여
 *  유입처키(PROVIDER_BENEFIT_SEQ)를 찾아와서 혜택(BENEFIT.PROVIDER_BENEFIT_SEQ)에 매핑한다.</p>
 * <p>유입처(PROVIDER_BENEFIT)의 카테고리 분류(CATEG_DEPTH1, CATEG_DEPTH2, CATEG_DEPTH3)값은
 *  {@link kr.okplace.benefit.domain.ProviderBenefitHolderWithCategory#getCategoryDepth1() ProviderBenefitHolderWithCategory.getCategoryDepth1()},
 *  {@link kr.okplace.benefit.domain.ProviderBenefitHolderWithCategory#getCategoryDepth2() ProviderBenefitHolderWithCategory.getCategoryDepth2()},
 *  {@link kr.okplace.benefit.domain.ProviderBenefitHolderWithCategory#getCategoryDepth3() ProviderBenefitHolderWithCategory.getCategoryDepth3()}를
 *  통하여 받아온다.</p>
 *  <p>Spring 설정 예</p>
 *  <p>providerBenefitKeyMapInsertProcessor은 mapper-context.xml에 등록되어 있다.</p>
 *  <code>
		&lt;bean id="providerBenefitKeyMapInsertProcessor" class="kr.okplace.job.common.ProviderBenefitKeyMapInsertProcessor" /&gt;
 *  </code>
 *  <code>
		&lt;tasklet&gt;
			&lt;chunk
				reader="okcashconItemReader"
				processor="providerBenefitKeyMapInsertProcessor"
				writer="commonMerchantLocationBenefitInserter"
				commit-interval="100" /&gt;
		&lt;/tasklet&gt;
 *  </code>
 *  
 * @author Superman
 * @see #process(ProviderBenefitHolderWithCategory)
 * @see org.springframework.batch.item.ItemProcessor
 * @see ProviderBenefitKeyInsertProcessor
 * @see kr.okplace.benefit.domain.ProviderBenefitHolderWithCategory
 */
public class ProviderBenefitKeyMapInsertProcessor implements ItemProcessor<ProviderBenefitHolderWithCategory, ProviderBenefitHolder> {

	@Autowired
	private MerchantLocationBenefitMapper mapper;

	private Map<ProviderBenefit, Integer> providerBenefitKeyMap = new HashMap<ProviderBenefit, Integer>();

	private Integer getProviderBenefitKey(ProviderBenefit providerBenefit) {
		
		if(!providerBenefitKeyMap.containsKey(providerBenefit)) {
			Integer value = mapper.selectProviderBenefitKyByBenefitCategoryPair(providerBenefit);
			this.providerBenefitKeyMap.put(providerBenefit, value);
		}
		
		return providerBenefitKeyMap.get(providerBenefit);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 * @see kr.okplace.job.okcashcon.OkCashConProcessor#process(kr.okplace.job.okcashcon.OkCashCon)
	 */
	@Override
	public ProviderBenefitHolder process(ProviderBenefitHolderWithCategory item) throws Exception {

		ProviderBenefit providerBenefit = new ProviderBenefit();
		providerBenefit.setCategoryDepth1(item.getCategoryDepth1());
		providerBenefit.setCategoryDepth2(item.getCategoryDepth2());
		providerBenefit.setCategoryDepth3(item.getCategoryDepth3());
		providerBenefit.setKey(getProviderBenefitKey(providerBenefit));
		
		item.setProviderBenefit(providerBenefit);
		if(item instanceof BenefitBuilder) {
			((BenefitBuilder)item).buildBenefit();
		}

		return item;
	}
}
