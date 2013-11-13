package kr.okplace.job.launch;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class QuartzJobInternalLauncher extends QuartzJobBean {

	private static Log log = LogFactory.getLog(QuartzJobInternalLauncher.class);

	/**
	 * Special key in job data map for the name of a job to run.
	 */
	static final String JOB_NAME = "jobName";

	private static final long MIN_INTERVAL = 3*60*1000;

	private JobLocator jobLocator;
	private JobLauncher jobLauncher;

	/**
	 * Public setter for the {@link JobLocator}.
	 * @param jobLocator the {@link JobLocator} to set
	 */
	public void setJobLocator(JobLocator jobLocator) {
		this.jobLocator = jobLocator;
	}

	/**
	 * Public setter for the {@link JobLauncher}.
	 * @param jobLauncher the {@link JobLauncher} to set
	 */
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) {
		
		try {
			JobDataMap jobDataMap = context.getMergedJobDataMap();
			JobDetail jobDetail = context.getJobDetail();
			JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);
			String jobName = jobDetail.getKey().getName();
			Job job = jobLocator.getJob(jobName);
			jobLauncher.run(job, jobParameters);
		}
		catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchJobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Copy parameters that are of the correct type over to
	 * {@link JobParameters}, ignoring jobName.
	 * 
	 * @return a {@link JobParameters} instance
	 */
	private JobParameters getJobParametersFromJobMap(Map<String, Object> jobDataMap) {

		JobParametersBuilder builder = new JobParametersBuilder();
		String key;
		Object value;
		for (Entry<String, Object> entry: jobDataMap.entrySet()) {
			
			key = entry.getKey();
			value = entry.getValue();
			if (value instanceof String && !key.equals(JOB_NAME)) {
				builder.addString(key, (String)value);
			}
			else if (value instanceof Float || value instanceof Double) {
				builder.addDouble(key, ((Number)value).doubleValue());
			}
			else if (value instanceof Integer || value instanceof Long) {
				builder.addLong(key, ((Number)value).longValue());
			}
			else if (value instanceof Date) {
				builder.addDate(key, (Date)value);
			}
			else {
				log.debug("JobDataMap contains values which are not job parameters (ignoring).");
			}
			
			builder.addLong("run.id", System.currentTimeMillis()/MIN_INTERVAL);
		}

		return builder.toJobParameters();
	}
}
