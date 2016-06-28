package controllers.manager;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Collections2;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.EmailAccountService;
import services.base.EmailTemplateService;
import services.base.FoundationService;
import services.customerService.ProfessionSkillTopicService;
import services.manager.AdminUserService;
import services.manager.livechat.LeaveMsgInfoService;
import services.member.registration.MemberRegistrationService;
import services.messaging.IMessageService;
import valueobjects.base.Page;
import valueobjects.manager.ReplyLeaveMessageEmailModel;
import base.util.mail.EmailUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Maps;

import context.ContextUtils;
import session.ISessionService;
import dto.EmailAccount;
import dto.AdminUser;
import entity.manager.LeaveMsgInfo;
import entity.manager.ProfessionSkillTopic;
import entity.messaging.Broadcast;

@controllers.AdminRole(menuName = "LeaveMessage")
public class LeaveMessage extends Controller {
	@Inject
	LeaveMsgInfoService infoService;

	@Inject
	ProfessionSkillTopicService topicService;

	@Inject
	EmailAccountService emailAccountService;

	@Inject
	EmailTemplateService emailTemplateService;

	@Inject
	ISessionService sessionService;

	@Inject
	AdminUserService adminUserService;

	@Inject
	IMessageService iMessageService;

	@Inject
	FoundationService fService;

	@Inject
	MemberRegistrationService registration;

	public Result manage(int page) {
		List<ProfessionSkillTopic> topicList = topicService
				.getAllEnableTopics();
		Page<dto.LeaveMsgInfo> p = infoService.getLeaveMsgInfoPage(page);
		List<AdminUser> users = adminUserService.getadminUserList();
		return ok(views.html.manager.leavemessage.manage.render(p, topicList,
				-1, -1, null, users, null, null));
	}

	public Result search() {
		List<ProfessionSkillTopic> topicList = topicService
				.getAllEnableTopics();
		List<AdminUser> users = adminUserService.getadminUserList();

		DynamicForm in = Form.form().bindFromRequest();
		String ilanguageidStr = in.get("ilanguageid_search");
		String itopicStr = in.get("leave_message_topic");
		String bishandleStr = in.get("leave_message_bishandle");
		String pretreatmentuseridstr = in.get("leave_message_p_user");
		String handleuseridstr = in.get("leave_message_handle");

		Integer ilanguageid = Integer.parseInt(ilanguageidStr);
		Integer itopicid = Integer.parseInt(itopicStr);
		Boolean bishandle = "-1".equals(bishandleStr) ? null : Boolean
				.parseBoolean(bishandleStr);
		Integer pretreatmentuserid = Integer.parseInt(pretreatmentuseridstr);
		if (-1 == pretreatmentuserid)
			pretreatmentuserid = null;
		Integer handleuserid = Integer.parseInt(handleuseridstr);
		if (-1 == handleuserid)
			handleuserid = null;
		String handler = null;
		if (handleuserid != null) {
			Integer hid = handleuserid;
			Collection<AdminUser> ulist = Collections2.filter(users,
					u -> u.getIid() == hid);
			if (null != ulist && ulist.size() > 0) {
				handler = ((AdminUser) ulist.toArray()[0]).getCusername();
			}
		}
		Page<dto.LeaveMsgInfo> p = infoService.searchLeaveMsgInfoPage(1,
				ilanguageid, itopicid, bishandle, pretreatmentuserid, handler);
		Logger.debug("user-count->" + users.size());
		return ok(views.html.manager.leavemessage.manage.render(p, topicList,
				ilanguageid, itopicid, bishandle, users, pretreatmentuserid,
				handleuserid));
	}

	public Result searchLeaveMessagePage(int page, Integer ilanguageid,
			Integer itopicid, String bishandleStr, Integer pretreatmentuserid,
			Integer handlerid) {

		List<AdminUser> users = adminUserService.getadminUserList();
		List<ProfessionSkillTopic> topicList = topicService
				.getAllEnableTopics();
		String handler = null;
		Integer hid = handlerid;
		if (handlerid != null) {
			Collection<AdminUser> ulist = Collections2.filter(users,
					u -> u.getIid() == hid);
			if (null != ulist && ulist.size() > 0) {
				handler = ((AdminUser) ulist.toArray()[0]).getCusername();
			}
		}
		if (-1 == pretreatmentuserid)
			pretreatmentuserid = null;
		Boolean bishandle = "-1".equals(bishandleStr) ? null : Boolean
				.parseBoolean(bishandleStr);
		Page<dto.LeaveMsgInfo> p = infoService.searchLeaveMsgInfoPage(page,
				ilanguageid, itopicid, bishandle, pretreatmentuserid, handler);

		return ok(views.html.manager.leavemessage.manage.render(p, topicList,
				ilanguageid, itopicid, bishandle, users, pretreatmentuserid,
				handlerid));
	}

	public Result handle(Integer iid) {
		Map<String, Object> resultMap = Maps.newHashMap();

		if (infoService.leaveMsgInfoHandle(iid)) {
			resultMap.put("errorCode", 1);
		} else {
			resultMap.put("errorCode", 0);
		}

		return ok(Json.toJson(resultMap));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result replyMessage() {
		try {
			entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
					.get("ADMIN_LOGIN_CONTEXT");
			if (null == user) {
				return badRequest("no login user!");
			}
			JsonNode jnode = request().body().asJson();
			if (jnode == null) {
				return internalServerError("Expecting Json data");
			}

			int lmid = jnode.get("leaveMsgId").asInt();
			String title = jnode.get("title").asText();
			String content = jnode.get("content").asText();
			LeaveMsgInfo lminfo = infoService.getLeaveMsgById(lmid);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(lminfo.getDcreatedate());
			ReplyLeaveMessageEmailModel replyLeaveMessageEmailModel = new ReplyLeaveMessageEmailModel(
					lminfo.getIlanguageid());
			replyLeaveMessageEmailModel.setContent(content);
			replyLeaveMessageEmailModel.setQuestion(lminfo.getCcontent());
			replyLeaveMessageEmailModel.setReplyUserName(user.getCusername());
			replyLeaveMessageEmailModel.setQuestionTime(dateString);
			Map<String, String> emailcontentmap = emailTemplateService
					.getEmailContent(replyLeaveMessageEmailModel);
			if (title == null || title.trim().length() == 0) {
				title = emailcontentmap.get("title");
			}
			EmailAccount ea = emailAccountService.getEmailAccount("smtp");
			if (EmailUtil.send(title, emailcontentmap.get("content"), ea,
					lminfo.getCemail())) {
				infoService.updateById(user.getIid(), content,
						Integer.valueOf(lminfo.getIid()));
			} else {
				return internalServerError("sended faile");
			}
		} catch (Exception ex) {
			return internalServerError(ex.getMessage());
		}
		return ok();
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result replyMsgBySystem() {
		try {
			entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
					.get("ADMIN_LOGIN_CONTEXT");
			if (null == user) {
				return badRequest("no login user!");
			}
			JsonNode jnode = request().body().asJson();
			if (jnode == null) {
				return internalServerError("Expecting Json data");
			}

			if (jnode.get("content") == null
					|| jnode.get("content").asText() == null
					|| jnode.get("content").asText().isEmpty()) {
				return internalServerError("content not be empty");
			}
			if (registration.getEmail(jnode.get("email").asText(),
					ContextUtils.getWebContext(Context.current())) == false) {
				return internalServerError("email invalid !");
			}
			Logger.debug("" + jnode);
			int lmid = jnode.get("leaveMsgId").asInt();
			Broadcast info = new Broadcast();
			info.setContent(jnode.get("content").asText());
			info.setSubject(jnode.get("title").asText());
			info.setEmail(jnode.get("email").asText());
			info.setSendId(user.getIid());
			info.setFrom("TOMTOP team");
			info.setType(enums.messaging.Type.PERSONAL.getCode());
			info.setStatus(enums.messaging.Status.unread.getCode());
			info.setSendMethod(enums.messaging.SendMethod.MANUAL.getCode());
			info.setWebsiteId(fService.getLanguage());
			int id = iMessageService.insert(info);
			infoService.updateById(user.getIid(),
					jnode.get("content").asText(), lmid);
		} catch (Exception ex) {
			return internalServerError(ex.getMessage());
		}
		return ok();
	}
}
