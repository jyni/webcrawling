package kr.okplace.job.launch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

public class SimpleLauncher {

	private JobLauncher jobLauncher;

	private Job job;

	public JobLauncher getJobLauncher() {
		return jobLauncher;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void launch() {
		
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
		try {
			JobExecution execution = jobLauncher.run(job, jobParameters);
			System.out.println("Exit Status : " + execution.getStatus());
		}
		catch (Exception e) {
			System.out.println("Unable to execute job");
			e.printStackTrace();
		}
	}
}
