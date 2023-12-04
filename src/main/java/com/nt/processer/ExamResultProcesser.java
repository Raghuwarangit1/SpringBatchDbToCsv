package com.nt.processer;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.nt.model.ExamResult;
@Component
public class ExamResultProcesser implements ItemProcessor<ExamResult, ExamResult> {

	@Override
	public ExamResult process(ExamResult item) throws Exception {
		if(item.getPercentage()>=90.0)
			return item;
		
		else	return null;
	}

}
