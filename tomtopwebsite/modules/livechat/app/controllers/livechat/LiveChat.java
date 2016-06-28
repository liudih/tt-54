package controllers.livechat;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.utils.DateFormatUtils;
import services.base.FoundationService;
import services.base.utils.Utils;
import services.livechat.LiveChatService;
import valueobjects.livechat.control.AbortConnectControl;
import valueobjects.livechat.control.AcceptControl;
import valueobjects.livechat.control.ConnectControl;
import valueobjects.livechat.control.Control;
import valueobjects.livechat.control.ControlCodec;
import valueobjects.livechat.control.HangupControl;
import valueobjects.livechat.control.LogoutControl;
import valueobjects.livechat.control.PingControl;
import valueobjects.livechat.control.QuitControl;
import valueobjects.livechat.control.RefreshControl;
import valueobjects.livechat.control.TransferControl;
import valueobjects.livechat.control.WaitControl;
import valueobjects.livechat.data.LiveChatMessage;
import valueobjects.livechat.data.SimpleMessage;
import valueobjects.livechat.leave.LeaveMessage;
import valueobjects.livechat.score.LiveChatSessionScore;
import valueobjects.livechat.status.LiveChatStatus;
import valueobjects.livechat.status.NoneStatus;
import valueobjects.livechat.status.RoleType;
import valueobjects.livechat.status.StatusType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import extensions.livechat.role.LiveChatRoleStatusProvider;
import extensions.livechat.score.SessionScoreQuestionProvider;
import extensions.livechat.topic.ChatTopic;
import extensions.livechat.topic.ChatTopicProvider;
import forms.livechat.leave.LeaveMessageForm;

/**
 * LiveChat Controllers: There are two channels for each person,
 * 
 * <ul>
 * <li>Control Channel: control the lifecycle of each conversation</li>
 * <li>Data Channel: send messages</li>
 * </ul>
 * 
 * @author kmtong
 *
 */
public class LiveChat extends Controller {

	ObjectMapper om = new ObjectMapper();

	@Inject
	LiveChatService service;

	@Inject
	FoundationService foundation;

	@Inject
	Set<LiveChatRoleStatusProvider> liveChatRoleStatusProviders;

	@Inject
	Set<SessionScoreQuestionProvider> scoreQuestions;

	@Inject
	Set<ChatTopicProvider> topics;

	@BodyParser.Of(BodyParser.Json.class)
	public Promise<Result> control() {
		JsonNode ctrl = request().body().asJson();
		if (ctrl == null) {
			return Promise.pure(badRequest());
		}
		Control ctrlObj = ControlCodec.decode(ctrl);
		if (ctrlObj == null) {
			return Promise.pure(badRequest());
		}
		String origin = mySide();
		switch (ctrlObj.getType()) {
		case PING:
			return Promise.pure(ok(Json.toJson(service.ping(origin,
					(PingControl) ctrlObj))));
		case CONNECT:

			// 连接时只有客户才需要更新 ip，语言，国家，等 信息
			if (((ConnectControl) ctrlObj).getIroleid() == (RoleType.CUSTOMER
					.getType())) {
				service.updateStatus(origin,
						((ConnectControl) ctrlObj).getIlanguageid(),
						((ConnectControl) ctrlObj).getDestination(),
						foundation.getClientIP(), foundation.getCountry(),
						((ConnectControl) ctrlObj).getIroleid());
			}
			LiveChatStatus status = this
					.hasOnlineCustomerService((ConnectControl) ctrlObj);
			if (status.getStatus().equals(StatusType.NONE) == false) {
				Logger.debug("not be to connect service {} ! ",
						Json.toJson(status));
				return Promise.pure(ok(Json.toJson(status)));
			}
			Logger.debug("connect service! {} ", Json.toJson(ctrlObj));
			// destination should not be LTC
			// as LTC is a highly security sensitive data
			status = service.connect(origin, (ConnectControl) ctrlObj,
					foundation.getSiteID(),
					((ConnectControl) ctrlObj).getIlanguageid());
			Logger.debug(" connect status {} ! ", Json.toJson(status));
			return Promise.pure(ok(Json.toJson(status)));
		case ACCEPT:
			service.accept(origin, (AcceptControl) ctrlObj);
			return Promise.pure(ok());
		case ABORTCONNECT:
			service.abortConnect(origin, (AbortConnectControl) ctrlObj);
			return Promise.pure(ok());
		case HANGUP:
			service.hangup(origin, (HangupControl) ctrlObj);
			return Promise.pure(ok());
		case QUIT:
			service.quit(origin, (QuitControl) ctrlObj);
			return Promise.pure(ok());
		case LOGOUT:
			service.logout(origin, (LogoutControl) ctrlObj);
			return Promise.pure(ok());
		case TRANSFER:
			LiveChatStatus tstatus = service.transfer(origin,
					foundation.getSiteID(), (TransferControl) ctrlObj);
			return Promise.pure(ok(Json.toJson(tstatus)));
		default:
			return Promise.pure(badRequest("Not implemented Control Type: "
					+ ctrlObj.getType()));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result refresh() {
		Logger.debug("====REFRESH=========");
		JsonNode ctrl = request().body().asJson();
		if (ctrl == null) {
			return badRequest();
		}
		Control ctrlObj = ControlCodec.decode(ctrl);
		String origin = mySide();

		service.refresh(origin, (RefreshControl) ctrlObj);

		return ok();
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result data(String chatSessionID) {
		JsonNode data = request().body().asJson();
		String myself = mySide();
		if (data != null) {
			SimpleMessage message = messageParser(data);
			if (message != null && message.getText() != null) {
				service.postMessageToOther(chatSessionID, myself,
						message.getText());
				service.postMessageToSelf(chatSessionID, myself,
						message.getText());
			}
		}

		List<LiveChatMessage> msgs = service.receiveMessage(chatSessionID,
				myself);

		List<SimpleMessage> simpleMsgList = Lists.transform(
				msgs,
				m -> new SimpleMessage(m.getText(), service.getStatusByLTC(
						m.getFrom()).getAlias(), DateFormatUtils.getHHmmss(m
						.getTimestamp())));
		return ok(Json.toJson(simpleMsgList));
	}

	private String mySide() {
		return foundation.getLoginContext().getLTC();
	}

	private SimpleMessage messageParser(JsonNode data) {
		SimpleMessage msg = om.convertValue(data, SimpleMessage.class);
		return msg;
	}

	/**
	 * 默认是有的
	 * 
	 * @param destination
	 * @return
	 */
	private LiveChatStatus hasOnlineCustomerService(ConnectControl control) {
		for (LiveChatRoleStatusProvider r : liveChatRoleStatusProviders) {
			if (r.canResolve(control.getDestination())) {
				LiveChatStatus status = r.getStatus(control.getDestination(),
						this.mySide(), control.getIlanguageid());
				if (status.getStatus().equals(StatusType.NONE) == false) {
					return status;
				}
			}
		}
		return new NoneStatus();
	}

	public Result leave() {
		return ok(views.html.livechat.leave_message.render());
	}

	public Result leaveSave() {
		Logger.debug("========save leave msg=======");
		Form<LeaveMessageForm> form = Form.form(LeaveMessageForm.class)
				.bindFromRequest();

		LeaveMessage leavemsg = new LeaveMessage();
		LeaveMessageForm uform = form.get();
		BeanUtils.copyProperties(uform, leavemsg);

		String myself = mySide();
		service.postLeaveMessage(leavemsg, myself);
		return ok();
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result saveLivechatCommentScore() {
		try {
			JsonNode data = request().body().asJson();
			LiveChatSessionScore lcs = om.convertValue(data,
					LiveChatSessionScore.class);
			lcs.setCustomerLtc(this.mySide());
			service.saveCommentScore(lcs);
		} catch (Exception ex) {
			Logger.error("", ex);
			return internalServerError();
		}
		return ok();
	}

	public Result getTopic(Integer languageid) {
		Logger.debug("get lang {}", languageid);
		List<ChatTopic> allTopics = Utils.flatten(FluentIterable.from(topics)
				.transform(tp -> tp.getTopics(languageid)).toList());
		return ok(Json.toJson(allTopics));
	}

	public Result getActiveCustomerService(){
		return ok(Json.toJson(service.getDutyCS()));
	}
}
