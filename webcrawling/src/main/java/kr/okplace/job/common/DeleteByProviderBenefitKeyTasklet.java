package kr.okplace.job.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * <h1>유입처 키(providerBenefitKey)를 이용해 혜택관련 Data를 삭제하는 Class</h1>
 * 
 * <p>유입처 키(providerBenefitKey)를 이용해 혜택관련 Data(BENEFIT, MERCHANT, LOCATION)를 삭제하는 Class.</p>
 * <p>join table(MERCHANT_LOCATION, BENEFIT_LOCATION)의 Data는 DB 설정(cascade on delete)으로 삭제.</p>
 * 
 * @author Superman
 * @see org.springframework.batch.core.step.tasklet.Tasklet
 * @see DeleteByProviderBenefitTasklet
 * @see MerchantLocationBenefitMapper#deleteByProviderBenefitKey(Integer)
 */
public class DeleteByProviderBenefitKeyTasklet implements Tasklet, InitializingBean {

	@Autowired
	private MerchantLocationBenefitMapper mapper;
	
	private List<Integer> providerBenefitKeys;

	/**
	 * <p>삭제 대상 혜택의 유입처 키(PROVIDER_BENEFIT_SEQ)</p>
	 * 
	 * @param providerBenefitKey 삭제 대상 혜택의 유입처 키(PROVIDER_BENEFIT_SEQ)
	 */
	public void setProviderBenefitKey(Integer providerBenefitKey) {
		
		if(providerBenefitKeys==null) {
			providerBenefitKeys = new ArrayList<Integer>();
		}
		
		this.providerBenefitKeys.add(providerBenefitKey);
	}

	/**
	 * <p>삭제 대상 혜택의 유입처 키(PROVIDER_BENEFIT_SEQ) 목록</p>
	 * 
	 * @param providerBenefitKeys 삭제 대상 혜택의 유입처 키(PROVIDER_BENEFIT_SEQ) 목록
	 */
	public void setProviderBenefitKeys(List<Integer> providerBenefitKeys) {
		this.providerBenefitKeys = providerBenefitKeys;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
//		Assert.notNull(providerBenefitKeys, "Property[providerBenefitKey or providerBenefitKeys] must be set!");
		Assert.notEmpty(providerBenefitKeys, "Property[providerBenefitKey or providerBenefitKeys] must be set!");
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 * @see MerchantLocationBenefitMapper#deleteByProviderBenefitKey(Integer)
	 * 
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		for(Integer providerBenefitKey: providerBenefitKeys) {
			mapper.deleteByProviderBenefitKey(providerBenefitKey);
		}
		
//		mapper.deleteOrphanedMerchant();
		
		return RepeatStatus.FINISHED;
	}

}
