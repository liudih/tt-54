package services.base;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.base.EmailVariableMapper;
import play.Logger;
import dto.EmailVariable;

public class EmailVariableService {
	@Inject
	EmailVariableMapper emailVariableMapper;

	public Boolean addEmailVariable(EmailVariable emailVariable) {
		emailVariable.setDcreatedate(new Date());
		int result = emailVariableMapper.insertSelective(emailVariable);
		return result > 0 ? true : false;
	}

	public boolean updateEmailTemplateVariable(EmailVariable emailVariable) {
		emailVariable.setDcreatedate(new Date());
		int result = emailVariableMapper
				.updateByPrimaryKeySelective(emailVariable);
		return result > 0 ? true : false;
	}

	public EmailVariable getEmailVariableById(Integer variableid) {
		return emailVariableMapper.getEmailVariableById(variableid);
	}

	public boolean deleteEmailVariableById(Integer variableid) {
		try {
			emailVariableMapper.deleteEmailVariableById(variableid);
		} catch (Exception e) {
			Logger.error("delete email template variable error" + variableid);
			return false;
		}
		return true;
	}

	public List<EmailVariable> getAllEmailVariables() {
		return emailVariableMapper.getAllEmailVariables();
	}

	public List<EmailVariable> getEmailVariableByType(String type) {
		return emailVariableMapper.getEmailVariablesByType(type);
	}

}
