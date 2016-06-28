package controllers.tracking;

import java.net.URI;
import java.net.URL;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import extensions.tracking.IAffiliateIDTracking;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import service.tracking.IAffiliateIDService;
import service.tracking.IVisitLogService;
import services.base.FoundationService;
import services.base.utils.CookieUtils;

public class VisitTracker extends Controller {

	@Inject
	IVisitLogService visitLogService;

	@Inject
	FoundationService foundation;

	@Inject
	IAffiliateIDTracking affiliateIDTracking;

	@Inject
	IAffiliateIDService affiliateIDService;

	public Result visit(String aidSource) {
		String referrer = request().getHeader("Referer");
		if (referrer == null) {
			return badRequest("Direct Visit not allowed");
		}
		try {
			String ip = request().remoteAddress();

			String sourceHost = ctx().request().host();

			String aid = null;
			// aid = ctx().request().getQueryString("aid");
			String eid = null;
			Integer taskType = null;

			String redirectUrl = null;
			if (aidSource == null) {
				// prefix mapping "/aid/xxxx/remaining-url"
				String uri = ctx().request().uri();
				if (uri != null && uri.startsWith("/aid/")) {
					String[] parts = uri.split("/");
					if (parts.length >= 3) {
						aidSource = parts[2];
						redirectUrl = uri.substring(5 + aid.length());
						if (redirectUrl.length() == 0) {
							redirectUrl = "/";
						}
						Logger.debug("Redirecting to '{}'", redirectUrl);
					} else {
						Logger.error(
								"Invalid uri for prefixed AID extraction: {}",
								uri);
					}
				} else {
					// Check any Referer field?
					if (referrer != null) {
						URL url = new URL(referrer);
						if (url != null) {
							int siteID = foundation.getSiteID();
							sourceHost = url.getHost();
							String dbaid = affiliateIDService
									.getAffiliateIDByReferer(siteID, sourceHost);
							if (dbaid != null) {
								Logger.debug("Referer: {}, AID: {}", referrer,
										dbaid);
								aidSource = dbaid;
							}
						}
					}
				}
			} else {
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
			Logger.debug("Tracker: AID: {}  Source: {}  IP: {}  PATH: {}", aid,
					sourceHost, ip, path);
			// 将aid放入cookie
			Context ctx = Context.current();
			if (!services.base.utils.StringUtils.isEmpty(aid)) {
				CookieUtils.setCookie(IAffiliateIDTracking.ORIGIN_STRING, aid,
						ctx);
			}
			visitLogService.saveVisitLog(foundation.getSiteID(), aid,
					sourceHost, ip, path, eid, taskType);
		} catch (Exception e) {
			Logger.error("VisitLog Persist Error", e);
		}
		response().setContentType("image/png");
		return noContent();
	}
}
