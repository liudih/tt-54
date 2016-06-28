package handlers.mobile;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import services.mobile.MobileService;
import services.mobile.VisitLogService;
import valuesobject.mobile.member.MobileContext;

import com.google.common.eventbus.Subscribe;

import entity.mobile.MobileVisitLog;
import events.mobile.VisitEvent;

public class VisitLogHandler {

	@Inject
	MobileService mobileService;

	@Inject
	VisitLogService visitLogService;

	@Subscribe
	public void onVisit(VisitEvent event) {
		MobileContext mctx = mobileService.getMobileContext(event.getCtx());
		MobileVisitLog visitLog = new MobileVisitLog();
		BeanUtils.copyProperties(mctx, visitLog);
		visitLog.setCclientid(event.getClientid());
		visitLog.setCremoteaddress(event.getRemoteAddress());
		visitLog.setCrequesturi(event.getRequestUri());
		visitLog.setIconsumetime(event.getConsumeTime());
		visitLog.setIlanguageid(event.getLanguageid());
		visitLogService.addLog(visitLog);
	}
}
