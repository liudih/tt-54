package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.EmailVariableService;
import session.ISessionService;

import com.fasterxml.jackson.databind.JsonNode;

import dto.EmailVariable;

public class EmailTemplateVariable extends Controller {

	@Inject
	EmailVariableService emailVariableService;

	@Inject
	ISessionService sessionService;

	final static Integer PAGE_SIZE = 10;

	final static int NOT_ERROR = 0;

	final static int REQUIRED_ERROR = 1;

	final static int SERVER_ERROR = 2;

	@controllers.AdminRole(menuName = "EmailTmpVariableMgr")
	public Result manageEmailVariable() {
		List<EmailVariable> emailVariables = emailVariableService
				.getAllEmailVariables();
		return ok(views.html.manager.emailtemplate.email_template_variable
				.render(emailVariables));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result addEmailVariable() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JsonNode jsonNode = request().body().asJson();
		String ctype = jsonNode.get("ctype").asText();
		String cname = jsonNode.get("cname").asText();
		String cremark = jsonNode.get("cremark").asText();
		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");
		if (null != user) {
			EmailVariable emailVariable = new EmailVariable();
			emailVariable.setCtype(ctype);
			emailVariable.setCname(cname);
			emailVariable.setCremark(cremark);
			emailVariable.setCcreateuser(user.getCcreateuser());
			Boolean result = emailVariableService
					.addEmailVariable(emailVariable);
			if (result) {
				resultMap.put("iid", emailVariable.getIid());
				resultMap.put("user", emailVariable.getCcreateuser());
				resultMap.put("date", emailVariable.getDate());
				resultMap.put("type", emailVariable.getCtype());
				resultMap.put("errorCode", NOT_ERROR);
				return ok(Json.toJson(resultMap));
			}
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result eiditEmailVariable() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JsonNode jsonNode = request().body().asJson();
		Integer id = jsonNode.get("iid").asInt();
		String ctype = jsonNode.get("ctype").asText();
		String cname = jsonNode.get("cname").asText();
		String cremark = jsonNode.get("cremark").asText();
		EmailVariable emailVar = emailVariableService.getEmailVariableById(id);
		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");
		if (null != user) {
			if (null != emailVar) {
				EmailVariable emailVariable = new EmailVariable();
				emailVariable.setIid(id);
				emailVariable.setCtype(ctype);
				emailVariable.setCname(cname);
				emailVariable.setCremark(cremark);
				emailVariable.setCcreateuser(user.getCcreateuser());
				boolean result = emailVariableService
						.updateEmailTemplateVariable(emailVariable);
				if (result) {
					resultMap.put("errorCode", NOT_ERROR);
					return ok(Json.toJson(resultMap));
				}
			}
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result deleteEmailVariable() {
		JsonNode jsonNode = request().body().asJson();
		Integer variableid = jsonNode.get("id").asInt();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		EmailVariable emailVariable = emailVariableService
				.getEmailVariableById(variableid);
		if (null != emailVariable) {
			boolean result = emailVariableService
					.deleteEmailVariableById(variableid);
			if (result) {
				resultMap.put("errorCode", NOT_ERROR);
				return ok(Json.toJson(resultMap));
			}
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result getEmailTemplateVariable() {
		JsonNode jsonNode = request().body().asJson();
		String ctype = jsonNode.get("ctype").asText();
		List<EmailVariable> emailVariables = emailVariableService
				.getEmailVariableByType(ctype);
		return ok(Json.toJson(emailVariables));
	}
}
