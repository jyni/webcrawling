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
public class DeleteBenefitExpireDataTasklet implements Tasklet, InitializingBean {

	@Autowired
	private MerchantLocationBenefitMapper mapper;
	

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 * @see MerchantLocationBenefitMapper#deleteByProviderBenefitKey(Integer)
	 * 
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		mapper.deleteBenefitExpireDataTasklet();
		
//		mapper.deleteOrphanedMerchant();
		
		return RepeatStatus.FINISHED;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

}
