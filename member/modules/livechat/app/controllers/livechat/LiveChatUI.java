package controllers.livechat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ILanguageService;
import services.base.FoundationService;
import services.base.utils.Utils;
import services.livechat.LiveChatService;
import valueobjects.livechat.DutyCustomerService;
import valueobjects.livechat.score.LiveChatScoreQuestion;
import valueobjects.livechat.session.ChatSession;

import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import dto.Chatroom;
import dto.SimpleLanguage;
import dto.Language;
import extensions.livechat.role.EnquiryRoleProvider;
import extensions.livechat.role.ILiveChatOnDutyCustomerServiceProvider;
import extensions.livechat.role.LiveChatRoleProvider;
import extensions.livechat.role.SupportRoleProvider;
import extensions.livechat.score.SessionScoreQuestionProvider;
import extensions.livechat.topic.ChatTopic;
import extensions.livechat.topic.ChatTopicProvider;

public class LiveChatUI extends Controller {

	@Inject
	Set<SupportRoleProvider> supports;

	@Inject
	Set<EnquiryRoleProvider> enquiries;

	@Inject
	Set<ChatTopicProvider> topics;

	@Inject
	LiveChatService chatService;

	@Inject
	FoundationService foundation;

	@Inject
	ILanguageService langService;

	@Inject
	Set<SessionScoreQuestionProvider> scoreQuestions;

	public Result startEnquiry() {
		Context ctx = Context.current();
		F.Tuple<String, String> ltcAlias = lookupRoleAlias(ctx, enquiries);
		if (ltcAlias == null) {
			return badRequest("Sorry, you are not able to use livechat");
		}
		chatService.login(ltcAlias._1, ltcAlias._2);
		return redirect(routes.LiveChatUI.supportTopic());
	}

	public Result startSupport() {
		Context ctx = Context.current();
		F.Tuple<String, String> ltcAlias = lookupRoleAlias(ctx, supports);
		if (ltcAlias == null) {
			return badRequest("Sorry, you are not the operator");
		}
		chatService.login(ltcAlias._1, ltcAlias._2);
		return redirect(routes.LiveChatUI.customerServiceWaiting());
	}

	public Result supportTopic() {
		Context ctx = Context.current();
		List<ChatTopic> allTopics = Utils.flatten(FluentIterable.from(topics)
				.transform(tp -> tp.getTopics(foundation.getLanguage()))
				.toList());

		List<Language> alllanguage = langService.getAllLanguage();
		List<SimpleLanguage> allLanguage = Lists.transform(alllanguage,
				l -> new SimpleLanguage(l.getIid(), l.getCfullname()));
		int currentLanguage = foundation.getLanguage();

		valueobjects.livechat.Status myStatus = chatService
				.getStatusByLTC(selfLTC(ctx));
		if (myStatus == null) {
			return badRequest("Status not found");
		}
		return ok(views.html.livechat.topic.render(allTopics,
				myStatus.getAlias(), allLanguage, currentLanguage));
	}

	public Result waiting(String destination, int number) {
		Context ctx = Context.current();
		valueobjects.livechat.Status myStatus = chatService
				.getStatusByLTC(selfLTC(ctx));
		if (myStatus == null) {
			return badRequest("Status not found");
		}
		return ok(views.html.livechat.waiting.render(myStatus.getAlias(),
				destination,number));
	}

	public Result customerServiceWaiting() {
		Context ctx = Context.current();
		valueobjects.livechat.Status myStatus = chatService
				.getStatusByLTC(selfLTC(ctx));
		if (myStatus == null) {
			return badRequest("Status not found");
		}
		return ok(views.html.livechat.customer_service_waiting.render(myStatus
				.getAlias()));
	}

	public Result chatroom(Integer roleId) {
		String myselfltc = foundation.getLoginContext().getLTC();

		Collection<ChatSession> chatSessionList = chatService
				.getChatSessions(myselfltc);
		Collection<Chatroom> chatroomList = Collections2
				.transform(
						chatSessionList,
						cs -> {
							dto.Chatroom cr = new dto.Chatroom();
							valueobjects.livechat.Status status = null;
							if (!myselfltc.equals(cs.getOrigin())) {
								status = chatService.getStatusByLTC(cs
										.getOrigin());
							} else {
								status = chatService.getStatusByLTC(cs
										.getDestination());
							}

							String otherAlias = status.getAlias();
							cr.setOtherAlias(otherAlias);
							cr.setSessionid(cs.getId());
							cr.setIp(status.getIp());
							cr.setCountry(status.getCountry());
							return cr;
						});

		valueobjects.livechat.Status mystatus = chatService
				.getStatusByLTC(myselfltc);
		if (mystatus == null) {
			Logger.debug("status is null,myLtc:" + myselfltc);
			return badRequest("Status not found");
		}
		String selfAlias = mystatus.getAlias();

		return ok(views.html.livechat.chatroom.render(chatroomList, selfAlias,
				roleId, this.getScoreQuestion(),chatService.getDutyCS()));
	}

	protected F.Tuple<String, String> lookupRoleAlias(Context ctx,
			Set<? extends LiveChatRoleProvider> providers) {
		Optional<? extends LiveChatRoleProvider> first = FluentIterable.from(
				providers).firstMatch(s -> s.isInRole(ctx));
		if (!first.isPresent()) {
			return null;
		}
		String alias = first.get().getAlias(ctx);
		String ltc = selfLTC(ctx);
		F.Tuple<String, String> ltcAlias = F.Tuple(ltc, alias);
		return ltcAlias;
	}

	protected String selfLTC(Context ctx) {
		return foundation.getLoginContext(ctx).getLTC();
	}

	private List<LiveChatScoreQuestion> getScoreQuestion() {
		for (SessionScoreQuestionProvider sqp : scoreQuestions) {
			return sqp.getScoreQuestion(foundation.getLanguage());
		}
		return new ArrayList<LiveChatScoreQuestion>();
	}
	
	

}
