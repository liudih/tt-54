package handlers.member;

import javax.inject.Inject;

import mapper.member.MemberLoginHistoryMapper;
import play.Logger;

import com.google.common.eventbus.Subscribe;

import dto.member.MemberLoginHistory;
import events.member.LoginEvent;

public class LoginAuditTrailHandler {

	@Inject
	MemberLoginHistoryMapper mapper;

	@Subscribe
	public void onLogin(LoginEvent event) {
		Logger.debug("Audit Trail for Login: {}", event.getEmail());
		MemberLoginHistory history = new MemberLoginHistory();
		history.setDtimestamp(event.getTimestamp());
		history.setCemail(event.getEmail());
		history.setIwebsiteid(event.getSiteID());
		history.setCclientip(event.getClientIP());
		history.setCltc(event.getLTC());
		history.setCstc(event.getSTC());
		mapper.insert(history);
	}
}
