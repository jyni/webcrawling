package kr.okplace.job;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Superman
 */
public class Main {

    private final Log log = LogFactory.getLog(getClass());

    private ApplicationContext context;
    private JobRegistry jobRegistry;
    private JobExplorer jobExplorer;
    private JobRepository jobRepository;
    private JobLauncher jobLauncher;

    /**
     * @throws BeansException
     * @throws IOException
     */
    public Main() throws BeansException, IOException {
    	
        context = new ClassPathXmlApplicationContext(
//        	new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/*-job-context.xml"}
 			new String[] {"/kr/okplace/job/launch/simple-affair-launcher-context.xml", "/kr/okplace/job/nxmile-smb2-4th-affair-context.xml"}
// 			new String[] {"/kr/okplace/job/launch/simple-affair-launcher-context.xml", "/kr/okplace/job/nxmile-smb2-3rd-affair-context.xml"}
// 			new String[] {"/kr/okplace/job/launch/simple-affair-launcher-context.xml", "/kr/okplace/job/nxmile-smb2-2nd-affair-context.xml"}
// 			new String[] {"/kr/okplace/job/launch/simple-affair-launcher-context.xml", "/kr/okplace/job/nxmile-smb2-1st-affair-context.xml"}
// 			new String[] {"/kr/okplace/job/launch/simple-affair-launcher-context.xml", "/kr/okplace/job/nxmile-smb2-tomntoms-affair-context.xml"}
//   		new String[] {"/kr/okplace/job/launch/simple-affair-launcher-context.xml", "/kr/okplace/job/nxmile-tmap-affair-context.xml"}
//   		new String[] {"/kr/okplace/job/launch/simple-affair-launcher-context.xml", "/kr/okplace/job/nxmile-smb2-affair-context.xml"}
//			new String[] {"/kr/okplace/job/launch/simple-affair-launcher-context.xml", "/kr/okplace/job/nxmile-excel-affair-context.xml"}
//			new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/benefit-daily-job-context.xml"}
//        	new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/sbp-daily-job-context.xml"}
//        	new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/okmyshop-daily-job-context.xml"}
//			new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/sknetworks-event-job-context.xml"}
//			new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/okcashcon-weekly-job-context.xml"}
//			new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/shoptalk-daily-job-context.xml"}
//        	new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/poicenter-excel-job-context.xml"}
//        	new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/wemakeprice-daily-job-context.xml"}
//        	new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/wemakeprice-now-job-context.xml"}
//        	new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/ticketmonster-daily-job-context.xml"}
//        	new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/ticketmonster-now-job-context.xml"}
//        	new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/smartdial-log-daily-job-context.xml"}
//        	new String[] {"/kr/okplace/job/launch/quartz-job-launcher-context.xml", "/kr/okplace/job/sbp-bugfix-job-context.xml"}
        );
//        ApplicationContext context = new ClassPathXmlApplicationContext("kr/okplace/job/ticketmonster/now.xml");
//        ApplicationContext context = new ClassPathXmlApplicationContext("kr/okplace/job/ticketmonster/daily.xml");
        
        jobRegistry = context.getBean("jobRegistry", JobRegistry.class);
        jobExplorer = context.getBean("jobExplorer", JobExplorer.class);
        jobRepository = context.getBean("jobRepository", JobRepository.class);
        jobLauncher = context.getBean("jobLauncher", JobLauncher.class);
//        job = context.getBean("updateDailyTicketMonsterJob", Job.class);
    }

    public void run() {
    	
        Collection<String> jobNames = jobRegistry.getJobNames();
//        List<String> jobNames = jobExplorer.getJobNames();
        for(String jobName: jobNames) {
        	
            try {
            	log.info("Start job[" + jobName + "]!");
				jobLauncher.run(context.getBean(jobName, Job.class), new JobParameters());
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
            finally {
            	log.info("End job[" + jobName + "]!");
            }
        }
    }

    public static void main(String[] args) throws Exception {

        Main main = new Main();
//        main.run();
    }
}
