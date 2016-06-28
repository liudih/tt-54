package services.base;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.base.EmailTemplateMapper;
import mapper.base.EmailTypeMapper;
import mapper.base.EmailVariableMapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;

import services.IEmailTemplateService;

import com.google.common.collect.Maps;

import dto.EmailTemplate;
import dto.EmailType;
import dto.EmailVariable;

public class EmailTemplateService implements IEmailTemplateService{

	@Inject
	EmailTemplateMapper emailTemplateMapper;

	@Inject
	EmailTypeMapper emailTypeMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	EmailVariableMapper emailVariableMapper;

	public boolean addEmailTemplate(EmailTemplate emailTemplate) {
		emailTemplate.setDcreatedate(new Date());
		int result = emailTemplateMapper.insert(emailTemplate);
		return result > 0 ? true : false;
	}

	public EmailTemplate getEmailTemplateBylangAndSiteAndType(Integer language,
			Integer iwebsiteid, String emailType) {
		return emailTemplateMapper.getEmailTemplateBylangAndSiteAndType(
				language, iwebsiteid, emailType);
	}

	public boolean deleteEmailTemplateByIid(int iid) {
		int result = emailTemplateMapper.deleteByPrimaryKey(iid);
		return result > 0 ? true : false;
	}

	public dto.EmailTemplate getEmailTemplateByiid(int iid) {
		EmailTemplate emailTemplate = emailTemplateMapper
				.selectByPrimaryKey(iid);
		dto.EmailTemplate emailTemplateVo = new dto.EmailTemplate();
		BeanUtils.copyProperties(emailTemplate, emailTemplateVo);
		return emailTemplateVo;
	}

	public List<EmailType> getAllEmailType() {
		return emailTypeMapper.getAllEmailType();
	}

	public List<dto.EmailTemplate> getEmailTemplateByPage(
			Integer websiteid, Integer page, Integer pageSize, String ctype, Integer languageId) {
		return emailTemplateMapper.getEmailTemplateByPages(websiteid, page,
				pageSize, ctype,languageId);
	}

	public Integer getEmailTemplateCount() {
		return emailTemplateMapper.getEmailTemplateCount();
	}

	public Boolean updateEmailTemplate(dto.EmailTemplate emailTemplate) {
		emailTemplate.setDcreatedate(new Date());
		int result = emailTemplateMapper
				.updateByPrimaryKeySelective(emailTemplate);
		return result > 0 ? true : false;
	}

	public boolean addNewEmailTemplate(EmailTemplate emailTemplate) {
		int result = emailTemplateMapper.insert(emailTemplate);
		return result > 0 ? true : false;
	}

	public dto.EmailTemplate getEmailTemplateById(Integer templageid) {
		return emailTemplateMapper.getEmailTemplateById(templageid);
	}

	public Map<String, String> getEmailContent(Object object) throws Exception {
		Integer siteId = foundationService.getSiteID();
		return getEmailContent(object, siteId);
	}
	
	/**
	 * 从数据库查询出来的邮件发送模板在这个方法中进行相应链接替换
	 * @param object
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getEmailContent(Object object, Integer siteId)
			throws Exception {
		Map<String, String> emailTitleAndContentMap = Maps.newHashMap();
		Map<String, Object> objectmap = PropertyUtils.describe(object);
		String emailContent = "";
		String title = "";
		if (null != objectmap && objectmap.size() > 0) {
			Integer language = Integer.valueOf((objectmap.get("language")
					.toString()));
			String emailType = objectmap.get("emailType").toString();
			EmailTemplate emailTemplate = emailTemplateMapper
					.getEmailTemplateBylangAndSiteAndType(language, siteId,
							emailType);
			if (null != emailTemplate) {
				emailContent = emailTemplate.getCcontent();
				title = emailTemplate.getCtitle();
			}
			List<EmailVariable> emailVariables = emailVariableMapper
					.getEmailVariablesByType(emailType);
			if (null != emailVariables && emailVariables.size() > 0) {
				for (EmailVariable emailVariable : emailVariables) {
					String variable = emailVariable.getCname();
					String key = emailVariable.getCname().replace("#{", "")
							.replace("}", "");
					Object value = objectmap.get(key);
					if (title.contains(variable)) {
						if (null != value) {
							title = title.replace(variable, value.toString());
						}
					}
					if (emailContent.contains(variable)) {
						if (null != value) {
							emailContent = emailContent.replace(variable,
									value.toString());
						}
					}
				}

			}
		}
		emailTitleAndContentMap.put("content", emailContent);
		emailTitleAndContentMap.put("title", title);
		return emailTitleAndContentMap;
	}
}
