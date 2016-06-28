package handlers.mobile;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import play.Logger;
import services.mobile.MobileService;
import services.mobile.VisitLogService;
import valuesobject.mobile.member.MobileContext;

import com.google.common.eventbus.Subscribe;

import entity.mobile.MobileVisitLog;
import events.mobile.VisitEvent;

public class VisitLogHandler {

	@Inject
	VisitLogService visitLogService;

	@Inject
	MobileService mobileService;

	@Subscribe
	public void onVisit(VisitEvent event) {
		try {
			MobileContext mctx = mobileService.getMobileContext(event.getCtx());
			MobileVisitLog visitLog = new MobileVisitLog();
			BeanUtils.copyProperties(mctx, visitLog);
			visitLog.setCclientid(event.getClientid());
			visitLog.setCremoteaddress(event.getRemoteAddress());
			visitLog.setCrequesturi(event.getRequestUri());
			visitLog.setIconsumetime(event.getConsumeTime());
			visitLog.setIlanguageid(event.getLanguageid());
			visitLogService.addLog(visitLog);
		} catch (BeansException e) {
			Logger.warn("" + e.fillInStackTrace());
			e.printStackTrace();
		}
	}
}
