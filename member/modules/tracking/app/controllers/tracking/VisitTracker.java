package controllers.tracking;

import java.net.URI;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.google.common.eventbus.EventBus;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import service.tracking.IVisitLogService;
import services.base.FoundationService;
import entity.tracking.VisitLog;
import extensions.tracking.IAffiliateIDTracking;

public class VisitTracker extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	IAffiliateIDTracking affiliateIDTracking;

	@Inject
	EventBus eventBus;

	public Result visit() {
		String referrer = request().getHeader("Referer");
		if (referrer == null) {
			return badRequest("Direct Visit not allowed");
		}
		try {
			String ip = request().remoteAddress();
			String aidSource = affiliateIDTracking.getAffiliateID(ctx());
			String source = affiliateIDTracking.getSourceHost(ctx());
			String aid = null;
			String eid = null;
			Integer taskType = null;
			// example aid=123456:test123
			if (aidSource != null) {
				String[] aids = aidSource.split("-");
				aid = aids[0];
				if (aids.length > 1) {
					eid = aids[1];
				}
				if (aids.length > 2 && !"".equals(aids[2])
						&& StringUtils.isNumeric(aids[2])) {
					taskType = Integer.parseInt(aids[2]);
				}
			}
			URI url = new URI(referrer);
			String path = url.getPath()
					+ (url.getQuery() != null ? "?" + url.getQuery() : "")
					+ (url.getFragment() != null ? "#" + url.getFragment() : "");
			Logger.trace("Tracker: AID: {}  Source: {}  IP: {}  PATH: {}", aid,
					source, ip, path);
			VisitLog visitLog = new VisitLog();
			visitLog.setIwebsiteid(foundation.getSiteID());
			visitLog.setCaid(aid);
			visitLog.setCsource(source);
			visitLog.setCip(ip);
			visitLog.setCpath(path);
			visitLog.setCeid(eid);
			visitLog.setItasktype(taskType);
			eventBus.post(visitLog);
		} catch (Exception e) {
			Logger.error("VisitLog Persist Error", e);
		}
		response().setContentType("image/png");
		return noContent();
	}
}
