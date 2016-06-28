package services.interaction;

import java.util.Date;

import javax.inject.Inject;

import dto.interaction.ReportError;
import mapper.interaction.ReportErrorMapper;
import forms.interaction.ReportErrorForm;

public class ReportErrorService {

	@Inject
	ReportErrorMapper reportErrorMapper;
	
	public boolean addReportError(ReportErrorForm form){
		ReportError p = new ReportError();
		p.setCemail(form.getCemail().toLowerCase());
		p.setCinquiry(form.getCinquiry());
		p.setClistingid(form.getClistingid());
		p.setCsku(form.getCsku());
		p.setCerrortype(form.getCerrortype());
		p.setDcreatedate(new Date());
		int flag = reportErrorMapper.insertSelective(p);
		return flag>0;
	}

	
}
