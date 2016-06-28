package services.livechat;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.libs.F;
import services.base.FoundationService;
import services.common.UUIDGenerator;
import valueobjects.livechat.AliasResolution;
import valueobjects.livechat.DutyCustomerService;
import valueobjects.livechat.Status;
import valueobjects.livechat.control.AbortConnectControl;
import valueobjects.livechat.control.AcceptControl;
import valueobjects.livechat.control.ClosedControl;
import valueobjects.livechat.control.ConnectControl;
import valueobjects.livechat.control.Control;
import valueobjects.livechat.control.EstablishedControl;
import valueobjects.livechat.control.HangupControl;
import valueobjects.livechat.control.LogoutControl;
import valueobjects.livechat.control.PingControl;
import valueobjects.livechat.control.QuitControl;
import valueobjects.livechat.control.RefreshControl;
import valueobjects.livechat.control.RingControl;
import valueobjects.livechat.control.TransferControl;
import valueobjects.livechat.control.WaitControl;
import valueobjects.livechat.data.LiveChatMessage;
import valueobjects.livechat.leave.LeaveMessage;
import valueobjects.livechat.score.LiveChatSessionScore;
import valueobjects.livechat.session.ChatSession;
import valueobjects.livechat.session.ConnectRequest;
import valueobjects.livechat.status.LiveChatStatus;
import valueobjects.livechat.status.NoneStatus;
import valueobjects.livechat.status.WaitStatus;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterators;
import com.google.common.eventbus.EventBus;

import extensions.livechat.LiveChatAliasResolver;
import extensions.livechat.role.ILiveChatOnDutyCustomerServiceProvider;
import extensions.livechat.topic.IWelcomeSentenceProvider;
import extensions.livechat.topic.WelcomeSentence;

public class LiveChatService {

	@Inject
	IChatSessionService chatSession;

	@Inject
	EventBus eventBus;

	@Inject
	Set<LiveChatAliasResolver> aliasResolvers;

	@Inject
	Set<IWelcomeSentenceProvider> ws;

	@Inject
	Set<ILiveChatOnDutyCustomerServiceProvider> dutyCustomerServiceProviders;

	@Inject
	FoundationService foundation;

	public void login(String ltc, String alias) {
		Logger.debug("LiveChat Alias [{}] Logged In, LTC={}", alias, ltc);
		chatSession.login(ltc, alias);
		scheduler();
	}

	public List<Control> ping(String ltc, PingControl control) {
		Logger.trace("Ping from {}", control.getAlias());
		chatSession.updateStatus(ltc, control.getAlias());
		boolean iswait = control.isBiswait();
		List<Control> controlList = chatSession.dequeueControl(ltc);
		if (iswait) {
			if (null == controlList || controlList.size() == 0) {
				controlList = Lists.newArrayList();
				WaitControl wc = new WaitControl(chatSession.getWaitCount(ltc));
				controlList.add(wc);
				return controlList;
			}
		}

		return controlList;
	}

	public LiveChatStatus connect(String originLTC, ConnectControl control,
			int siteID, int languageID) {
		Logger.debug("Connecting to: {}, Origin: {}", control.getDestination(),
				originLTC);
		ConnectRequest crequest = new ConnectRequest(originLTC,
				control.getDestination(), siteID, languageID);
		// ~ 队列里面不能有重复ltc
		if (chatSession.hasRepeatConnectRequest(crequest) == false) {
			chatSession.enqueueConnectRequest(crequest);
		} else {
			Logger.debug(" skip repeat connection {} ", originLTC);
		}
		scheduler();
		if (chatSession.hasRepeatConnectRequest(crequest)) {
			int number = chatSession.getWaitCount(originLTC);
			return new WaitStatus(number);
		}
		return new NoneStatus();
	}

	public void ring(String destinationLTC, String originLTC,
			RingControl control) {
		Logger.debug("Ringing Destination LTC: {}, Origin Alias: {} topic {}",
				destinationLTC, control.getOriginAlias(), control.getTopic());
		chatSession.setRingStatus(originLTC, control.getOriginAlias(),
				destinationLTC);
		chatSession.enqueueControl(destinationLTC, control);
	}

	public void accept(String destinationLTC, AcceptControl control) {
		String originAlias = control.getAlias();
		String origin = chatSession.getRingStatus(originAlias, destinationLTC);
		Status destinationStatus = chatSession.getStatus(destinationLTC);
		if (origin == null) {
			throw new RuntimeException(
					"Not ringing, please let origin connect again");
		}
		if (destinationStatus == null) {
			throw new RuntimeException("Destination status is NULL");
		}
		String destinationAlias = destinationStatus.getAlias();
		Logger.debug("Accept connection from {} -> LTC={} --> topic={}",
				originAlias, origin, control.getTopic());
		// established
		ChatSession session = new ChatSession(UUIDGenerator.createAsString());
		session.setOrigin(origin);
		session.setTopic(control.getTopic());
		// Session destination is LTC
		session.setDestination(destinationLTC);
		chatSession.createSession(session);
		chatSession.enqueueControl(origin,
				new EstablishedControl(session.getId(), destinationAlias));
		chatSession.enqueueControl(destinationLTC, new EstablishedControl(
				session.getId(), originAlias));
		eventBus.post(chatSession);
		Logger.debug("Chat Established: {}", session);
		chatSession.clearRingStatus(originAlias, destinationLTC);

		// seach database
		Status status = this.getStatusByLTC(session.getOrigin());
		int ilanguageid = this.getStatusByLTC(session.getOrigin())
				.getIlanguageid();
		WelcomeSentence ws = this.getWelcomeSentence(ilanguageid);
		// send welcome sentence to Other and self
		if (status.getIroleid() == 2) {
			this.postMessageToOther(session.getId(), session.getDestination(),
					ws.getWelcomeSentence());
			this.postMessageToSelf(session.getId(), session.getDestination(),
					ws.getWelcomeSentence());
		} else {
			this.postMessageToOther(session.getId(), session.getOrigin(),
					ws.getWelcomeSentence());
			this.postMessageToSelf(session.getId(), session.getOrigin(),
					ws.getWelcomeSentence());
		}
	}

	public void abortConnect(String originLTC, AbortConnectControl ctrlObj) {
		Logger.debug("Abort Connection: {} {}", originLTC,
				ctrlObj.getDestination());
		chatSession.removeRelatedConnectRequest(originLTC,
				ctrlObj.getDestination());
	}

	public void hangup(String initiatorLTC, HangupControl control) {
		ChatSession session = chatSession.getChatSession(control.getId());
		if (session == null) {
			throw new RuntimeException("LiveChat Session not found: "
					+ control.getId());
		}
		Logger.debug("Hangup Session: {}", control.getId());
		chatSession.enqueueControl(session.getDestination(), new ClosedControl(
				session.getId()));
		chatSession.enqueueControl(session.getOrigin(), new ClosedControl(
				session.getId()));
		chatSession.clearSession(session.getId());
		scheduler();
	}

	public void quit(String initiatorLTC, QuitControl control) {
		Logger.debug("LTC Quit: {}", initiatorLTC);
		// remove connect queue
		chatSession.removeRelatedConnectRequest(initiatorLTC, null);
		// loop all existing chat sessions and closes it
		for (ChatSession s : getChatSessions(initiatorLTC)) {
			hangup(initiatorLTC, new HangupControl(s.getId()));
		}
		// flush all control messages
		chatSession.flushControl(initiatorLTC);
		scheduler();
	}

	public void postMessageToOther(String chatSessionID, String from,
			String message) {
		// lookup session table
		ChatSession session = chatSession.getChatSession(chatSessionID);
		if (session == null) {
			throw new RuntimeException("Session Not Found: " + chatSessionID);
		}

		String to = getOtherSide(session, from);

		LiveChatMessage m = new LiveChatMessage();
		m.setChatSessionID(chatSessionID);
		m.setTimestamp(new Date());
		m.setText(message);
		m.setFrom(from);
		m.setTo(to);
		if (session.getTopic().indexOf(':') > 0) {
			m.setTopic(session.getTopic().split(":")[1]);
		} else {
			m.setTopic(session.getTopic());
		}

		Status fromStatus = getStatusByLTC(from);
		Status toStatus = getStatusByLTC(to);

		m.setFromAlias(fromStatus != null ? fromStatus.getAlias() : null);
		m.setToAlias(toStatus != null ? toStatus.getAlias() : null);

		chatSession.enqueue(m);
		// notify others (e.g. message archiver)
		eventBus.post(m);
	}

	public void postMessageToSelf(String chatSessionID, String to,
			String message) {
		// lookup session table
		ChatSession session = chatSession.getChatSession(chatSessionID);
		if (session == null) {
			throw new RuntimeException("Session Not Found: " + chatSessionID);
		}

		LiveChatMessage m = new LiveChatMessage();
		m.setChatSessionID(chatSessionID);
		m.setTimestamp(new Date());
		m.setText(message);
		m.setFrom(to);
		m.setTo(to);

		chatSession.enqueue(m);
	}

	public List<LiveChatMessage> receiveMessage(String chatSessionID, String to) {
		return chatSession.dequeue(chatSessionID, to);
	}

	public Status getStatusByLTC(String ltc) {
		return chatSession.getStatus(ltc);
	}

	public Status getStatusByAlias(String alias) {
		return chatSession.getStatusByAlias(alias);
	}

	public Collection<ChatSession> getChatSessions(String ltc) {
		return chatSession.getChatSessionsByLTC(ltc);
	}

	/**
	 * 处理ConnectRequest队列中的链接请求
	 */
	public void scheduler() {

		List<F.Tuple<ConnectRequest, AliasResolution>> reqs = chatSession
				.mapConnectRequest(request -> {
					AliasResolution ar = resolveDestinationAliasLTC(
							request.getDestinationAlias(), request.getSiteID(),
							request.getLanguageID());
					return F.Tuple(request, ar);
				});

		List<F.Tuple<ConnectRequest, AliasResolution>> possibleReqs = FluentIterable
				.from(reqs).filter(r -> r._2 != null && r._2.hasResult())
				.toList();

		Map<String, Integer> map = Maps.newHashMap();
		for (F.Tuple<ConnectRequest, AliasResolution> req : possibleReqs) {

			ConnectRequest request = req._1;
			AliasResolution ar = req._2;

			Integer allowNumber = ar.getAllowNumber();
			if (map.containsKey(ar.getLTC())) {
				allowNumber = map.get(ar.getLTC());
			}
			if (allowNumber <= 0) {
				continue;
			}
			allowNumber--;
			map.put(ar.getLTC(), allowNumber);

			String destinationLTC = ar.getLTC();
			Status destStatus = chatSession.getStatus(destinationLTC);
			Status originStatus = chatSession.getStatus(request.getOriginLTC());
			if (destStatus != null && originStatus != null) {
				// ring the destination
				Logger.debug("ring create -> destStatus {}  originStatus {} ",
						destStatus.getAlias(), originStatus.getAlias());
				ring(destinationLTC, request.getOriginLTC(), new RingControl(
						originStatus.getAlias(), request.getDestinationAlias()));
			} else {
				Logger.debug(
						"Either Origin/Destination is not Online, removing from waiting queue: Origin:{}, Dest:{}",
						originStatus, destStatus);
			}
			chatSession.removeConnectRequest(request);

		}

		List<F.Tuple<ConnectRequest, AliasResolution>> errorReqs = FluentIterable
				.from(reqs).filter(r -> r._2 != null)
				.filter(r -> !r._2.hasResult() && !r._2.isRetryable()).toList();
		for (F.Tuple<ConnectRequest, AliasResolution> req : errorReqs) {
			ConnectRequest request = req._1;
			Logger.debug(
					"Unrecoverable Connect Request: {} -> {}, removing from waiting queue",
					request.getOriginLTC(), request.getDestinationAlias());
			chatSession.removeConnectRequest(request);
		}
	}

	private AliasResolution resolveDestinationAliasLTC(String destination,
			int siteID, int languageID) {
		// destination should not be LTC
		// need some indirection to the user's LTC
		for (LiveChatAliasResolver r : aliasResolvers) {
			if (r.canResolve(destination)) {
				AliasResolution ar = r.resolve(destination, siteID, languageID);
				return ar;
			}
		}
		return null;
	}

	private String getOtherSide(ChatSession session, String side) {
		if (session.getOrigin().equals(side)) {
			return session.getDestination();
		} else if (session.getDestination().equals(side)) {
			return session.getOrigin();
		}
		throw new RuntimeException("Cannot determine other side for session: "
				+ session.getId() + ", side: " + side);
	}

	public ChatSession getChatSession(String chatSessionID) {
		return this.chatSession.getChatSession(chatSessionID);
	}

	public Collection<ChatSession> getAllSession() {
		return chatSession.getAllSession();
	}

	public void detetStatus(String ltc) {
		chatSession.deleteStatus(ltc);
	}

	public void refresh(String destinationLTC, RefreshControl control) {
		Collection<ChatSession> chatSessionList = this
				.getChatSessions(destinationLTC);
		for (ChatSession cs : chatSessionList) {
			String ltc = cs.getOrigin();
			Status status = this.getStatusByLTC(ltc);
			chatSession.enqueueControl(
					destinationLTC,
					new RefreshControl(cs.getId(), status.getAlias(), status
							.getIp(), status.getCountry()));
		}
		eventBus.post(chatSession);
	}

	public void postLeaveMessage(LeaveMessage leaveMsg, String from) {

		LeaveMessage m = new LeaveMessage();
		m.setCcontent(leaveMsg.getCcontent());
		m.setCemail(leaveMsg.getCemail());
		Status status = this.getStatusByLTC(from);
		Logger.debug("status:" + status.toString());
		m.setCltc(from);
		m.setIlanguageid(status.getIlanguageid());
		m.setCip(status.getIp());
		m.setCtopic(status.getTopic());
		m.setCalias(status.getAlias());

		// notify others (e.g. message archiver)
		eventBus.post(m);
	}

	public void updateStatus(String ltc, Integer ilanguageid, String topic,
			String ip, String country, Integer iroleid) {
		chatSession.updateStatus(ltc, ilanguageid, topic, ip, country, iroleid);
	}

	public void saveCommentScore(LiveChatSessionScore lcs) {
		ChatSession cs = chatSession.getChatSession(lcs.getSessionId());
		if (cs == null) {
			Logger.debug("score not found session " + lcs.getSessionId());
			return;
		}
		Status csst = chatSession.getStatus((cs.getDestination().equals(lcs
				.getCustomerLtc())) ? cs.getOrigin() : cs.getDestination());
		if (csst == null) {
			Logger.debug("score not found customer service ");
		} else {
			lcs.setCustomerServiceAlias(csst.getAlias());
			lcs.setCustomerServiceLtc(csst.getLtc());
		}

		Status cst = chatSession.getStatus(lcs.getCustomerLtc());
		if (null == cst) {
			Logger.debug("score not found customer " + lcs.getCustomerLtc());
		}
		lcs.setCustomerAlias(cst.getAlias());
		lcs.setTopic(cst.getTopic());
		eventBus.post(lcs);
	}

	public void logout(String ltc, LogoutControl logout) {
		Logger.debug("LiveChat Logged out, LTC={}", ltc);
		this.quit(ltc, new QuitControl());
		chatSession.deleteStatus(ltc);
	}

	public WelcomeSentence getWelcomeSentence(int langaugeId) {
		if (ws != null && ws.size() > 0) {
			return ((IWelcomeSentenceProvider) ws.toArray()[0])
					.getWelcomeSentence(langaugeId).get(0);
		}
		return new WelcomeSentence("", "");
	}

	public LiveChatStatus transfer(String initiatorLTC, int siteId,
			TransferControl control) {
		Logger.debug("Transfer from {} to {}", initiatorLTC, control.getTo());
		ChatSession cs = chatSession.getChatSession(control.getId());
		if (null == cs) {
			Logger.debug("Transfer error session not fount");
			return new NoneStatus();
		}
		String custemerltc = cs.getDestination();
		if (initiatorLTC.equals(cs.getDestination())) {
			custemerltc = cs.getOrigin();
		}
		HangupControl hc = new HangupControl(control.getId());
		// create connect
		Status toStatus = chatSession.getStatusByAlias(control.getTo());
		Status cStatus = chatSession.getStatus(custemerltc);
		this.hangup(initiatorLTC, hc);

		TransferControl cc = new TransferControl(control.getId(), "ltc:"
				+ toStatus.getLtc(), chatSession.getWaitCount(custemerltc),
				cStatus.getTopic());
		chatSession.enqueueControl(custemerltc, cc);
		return new NoneStatus();
	}

	// ~ 获取所有客服
	public List<DutyCustomerService> getDutyCS() {
		List<DutyCustomerService> cslist = Lists.newArrayList();
		String ltc = foundation.getLoginContext().getLTC();
		for (ILiveChatOnDutyCustomerServiceProvider sqp : dutyCustomerServiceProviders) {
			cslist = Lists.newArrayList(Iterators.transform(sqp
					.getCustomerService().iterator(), u -> {
				DutyCustomerService dcs = new DutyCustomerService();
				dcs.setAlias(u);
				dcs.setActive(false);
				dcs.setSelf(false);
				valueobjects.livechat.Status status = this.getStatusByAlias(u);
				if (status != null) {
					dcs.setActive(true);
					dcs.setSelf(status.getLtc().equals(ltc));
				}
				return dcs;
			}));
		}
		return cslist;
	}

}
