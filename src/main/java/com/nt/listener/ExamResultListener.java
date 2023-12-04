package com.nt.listener;

import java.util.Date;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
@Component("empListener")
public class ExamResultListener implements JobExecutionListener {
	Long start,end;
	@Override
	public void beforeJob(JobExecution jobExecution) {
		start=System.currentTimeMillis();
		System.out.println("execution start at"+new Date());
		System.out.println("jobExecution status:"+jobExecution.getStatus());
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("jobExecution exit status:"+jobExecution.getExitStatus());
		end=System.currentTimeMillis();
		System.out.println("job exution timing in milliseconds:"+(end-start));
		
		
	}

}
