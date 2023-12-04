package com.nt.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.nt.listener.ExamResultListener;
import com.nt.model.ExamResult;
import com.nt.processer.ExamResultProcesser;

@Configuration
@EnableBatchProcessing
public class ExamResultConfiguration {
@Autowired
private JobBuilderFactory jobBuilderFactory;
@Autowired
private StepBuilderFactory stepBuilderFactory;
@Autowired
private ExamResultProcesser processer;
@Autowired
private DataSource ds;
@Autowired
private ExamResultListener listener;

@Bean
public JdbcCursorItemReader<ExamResult> createReader(){
	return new JdbcCursorItemReaderBuilder<ExamResult>().name("reader")
			.dataSource(ds)
			.sql("SELECT ID,DOB,SEMESTER,PERCENTAGE FROM EXAM_RESULT")
			.beanRowMapper(ExamResult.class).build();
}//
@Bean
public FlatFileItemWriter<ExamResult>createWriter(){
	return new FlatFileItemWriterBuilder<ExamResult>()
			.name("writer").resource(new FileSystemResource("TopBrains.csv"))
			.lineSeparator("\r\n")
			.delimited().delimiter(",")
			.names(new String[]{"id","dob","semester","percentage"})
			.build();
}//
@Bean(name="step1")
public Step createStep1() {
	return stepBuilderFactory.get("step1")
			.<ExamResult,ExamResult>chunk(100)
             .reader(createReader()).processor(processer).writer(createWriter()).build();
}//step
@Bean(name="job1")
public Job createJob1() {
	return jobBuilderFactory.get("job1")
			.listener(listener)
			.incrementer(new RunIdIncrementer() )
			.start(createStep1()).build();
}



}
