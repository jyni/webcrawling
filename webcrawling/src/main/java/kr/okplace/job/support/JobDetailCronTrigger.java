package kr.okplace.job.support;

import java.util.Date;

import kr.okplace.job.launch.QuartzJobInternalLauncher;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

public class JobDetailCronTrigger extends CronTriggerImpl implements CronTrigger, InitializingBean, ApplicationContextAware {
	
	private static final long serialVersionUID = 1L;
	private static final Class<? extends org.quartz.Job> JOB_CLASS = QuartzJobInternalLauncher.class;
	private static final boolean DURABILITY = true;

	private ApplicationContext context;
	private String jobName;
	private String group;
	private long startDelay = 0L;

	public void setGroup(String group) {
		this.group = group;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public void setStartDelay(long startDelay) {
		this.startDelay = startDelay;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

//		Assert.notNull(jobClass, "Property[jobClass] required!");
//		Assert.notNull(group, "Property[group] required!");
		Assert.notNull(jobName, "Property[jobName] required!");
		Assert.notNull(context.getBean(jobName, Job.class), "Job[jobName] not exist!");

        JobLauncher jobLauncher = context.getBean("jobLauncher", JobLauncher.class);
        JobRegistry jobRegistry = context.getBean("jobRegistry", JobRegistry.class);
		Assert.notNull(jobLauncher, "Bean[jobLauncher] required!");
		Assert.notNull(jobRegistry, "Bean[jobRegistry] required!");

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobRegistry);
        jobDataMap.put("jobName", jobName);
        jobDataMap.put("group", group);

        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setJobClass(JOB_CLASS);
        // Quartz Version up 2.1.6 -> 2.1.7
        // http://forum.springsource.org/showthread.php?136030-Spring-quartz-not-work-for-quartz-2-1-7
        jobDetail.setDurability(DURABILITY);
        jobDetail.setGroup(group);
        jobDetail.setName(jobName);
        jobDetail.setJobDataMap(jobDataMap);

		super.setName(jobName + "DetailCronTrigger");
		super.setJobGroup(group);
		super.setJobName(jobName);
		super.setJobDataMap(jobDataMap);
		super.setStartTime(new Date(System.currentTimeMillis() + startDelay));
		
        Scheduler jobScheduler = context.getBean("jobScheduler", Scheduler.class);
		jobScheduler.addJob(jobDetail, true);
		jobScheduler.scheduleJob(this);
	}
}
