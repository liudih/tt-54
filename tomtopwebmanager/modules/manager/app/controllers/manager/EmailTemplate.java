package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.ILanguageService;
import services.base.EmailTemplateService;
import services.base.WebsiteService;
import session.ISessionService;
import valueobjects.base.Page;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

import dto.EmailType;
import dto.Website;
import forms.EmailTemplateForm;

public class EmailTemplate extends Controller {

	@Inject
	EmailTemplateService emailTemplateService;

	@Inject
	WebsiteService websiteEnquiryService;

	@Inject
	ILanguageService languageService;

	@Inject
	ISessionService sessionService;

	final static Integer PAGE_SIZE = 15;

	final static int NOT_ERROR = 0;

	final static int REQUIRED_ERROR = 1;

	final static int SERVER_ERROR = 2;

	@controllers.AdminRole(menuName = "EmailTmpTwoLevelMgr")
	public Result manageEmailTemplate(Integer page, Integer websiteid,
			String ctype,Integer languageId) {
		List<dto.EmailTemplate> emailTemplates = emailTemplateService
				.getEmailTemplateByPage(websiteid, page, PAGE_SIZE, ctype,languageId);
		Integer emailTemplateCount = emailTemplateService
				.getEmailTemplateCount();
		List<EmailType> emailTypes = emailTemplateService.getAllEmailType();
		List<dto.Website> websites = websiteEnquiryService.getAll();
		Map<Integer, String> websiteMap = Maps.newHashMap();
		Map<Integer, String> languageMap = Maps.newHashMap();
		for (Website website : websites) {
			websiteMap.put(website.getIid(), website.getCcode());
		}
		List<dto.Language> languages = languageService.getAllLanguage();
		for (dto.Language language : languages) {
			languageMap.put(language.getIid(), language.getCname());
		}
		Page<dto.EmailTemplate> emailTemplatePage = new Page<dto.EmailTemplate>(
				emailTemplates, emailTemplateCount, page, PAGE_SIZE);
		return ok(views.html.manager.emailtemplate.email_template.render(
				emailTemplatePage, emailTypes, websiteMap, languageMap,ctype,languageId,websiteid));
	}

	public Result addNewEmailTemplate() {
		Form<EmailTemplateForm> emailTemplateForm = Form.form(
				EmailTemplateForm.class).bindFromRequest();
		EmailTemplateForm eForm = emailTemplateForm.get();
		dto.EmailTemplate emailTemplate = new dto.EmailTemplate();
		BeanUtils.copyProperties(eForm, emailTemplate);
		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		emailTemplate.setCcreateuser(user.getCcreateuser());
		Boolean result = emailTemplateService.addEmailTemplate(emailTemplate);
		Integer website = eForm.getIwebsiteid();
		Integer language = eForm.getIlanguage();
		Integer lastPage = (eForm.getTotalPage() + 1) / PAGE_SIZE;
		if (lastPage < 1) {
			lastPage = 1;
		}
		if (result) {
			return redirect(controllers.manager.routes.EmailTemplate
					.manageEmailTemplate(lastPage, website, "all",language));
		}
		return badRequest();
	}

	public Result editEmailTemplate() {
		Form<EmailTemplateForm> emailTemplateForm = Form.form(
				EmailTemplateForm.class).bindFromRequest();
		EmailTemplateForm eForm = emailTemplateForm.get();
		dto.EmailTemplate emailTemplate = new dto.EmailTemplate();
		BeanUtils.copyProperties(eForm, emailTemplate);
		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		emailTemplate.setCcreateuser(user.getCcreateuser());
		Boolean result = emailTemplateService
				.updateEmailTemplate(emailTemplate);
		Integer pageNo = eForm.getPageNo();
		Integer website = eForm.getIwebsiteid();
		Integer language = eForm.getIlanguage();
		if (result) {
			return redirect(controllers.manager.routes.EmailTemplate
					.manageEmailTemplate(pageNo, website, "all",language));
		}
		return badRequest();
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result deleteEmailTemplate() {
		JsonNode jsonNode = request().body().asJson();
		Integer templageid = jsonNode.get("id").asInt();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		dto.EmailTemplate template = emailTemplateService
				.getEmailTemplateById(templageid);
		if (null != template) {
			boolean result = emailTemplateService
					.deleteEmailTemplateByIid(templageid);
			if (result) {
				resultMap.put("errorCode", NOT_ERROR);
				return ok(Json.toJson(resultMap));
			}
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result getAllEmailTemplateTypes() {
		List<EmailType> emailTypes = emailTemplateService.getAllEmailType();
		List<String> types = com.google.common.collect.Lists.transform(
				emailTypes, i -> i.getCname());
		return ok(Json.toJson(types));
	}

}
