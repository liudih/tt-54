package services;
import java.util.List;
import java.util.Map;

import dto.EmailTemplate;
import dto.EmailType;


public interface IEmailTemplateService {
	public boolean addEmailTemplate(EmailTemplate emailTemplate);
	
	public EmailTemplate getEmailTemplateBylangAndSiteAndType(Integer language,
			Integer iwebsiteid, String emailType);
	
	public boolean deleteEmailTemplateByIid(int iid);
	
	public dto.EmailTemplate getEmailTemplateByiid(int iid);
	
	public List<EmailType> getAllEmailType();
	
	public List<EmailTemplate> getEmailTemplateByPage(
			Integer websiteid, Integer page, Integer pageSize, String ctype, Integer languageId);
	
	public Integer getEmailTemplateCount();
	
	public Boolean updateEmailTemplate(EmailTemplate emailTemplate);
	
	public boolean addNewEmailTemplate(EmailTemplate emailTemplate);
	
	public EmailTemplate getEmailTemplateById(Integer templageid);
	
	public Map<String, String> getEmailContent(Object object) throws Exception;
	
	public Map<String, String> getEmailContent(Object object, Integer siteId)
			throws Exception;
	
}
