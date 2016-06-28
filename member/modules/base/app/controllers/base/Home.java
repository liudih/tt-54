package controllers.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.HeaderNames;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.utils.MetaUtils;
import services.base.template.TemplateService;
import valueobjects.base.template.TemplateComposite;
import filters.common.CookieTrackingFilter;

public class Home extends Controller {

	public Promise<Result> home() {
		// meta handling
		MetaUtils
				.currentMetaBuilder()
				.setTitle(
						"Global Online Shopping for high quality RC models, "
								+ "Cell phone, Cameras & accessories, Outdoor sports at Tomtop.com")
				.setDescription(
						"Tomtop global online shopping offers a variety of high quality products, "
								+ "including RC Models, Cell Phone, Cameras & Accessories, Outdoor Sports,"
								+ " Computer Accessories, Lightings, Car Accessories, Apparels and home gadgets.")
				.addKeyword(
						"Global Online Shopping, China Electronics Wholesale, Tomtop");
		TemplateComposite contents = TemplateService.getInstance()
				.getContents();

		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.SECOND, 120);
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE d MMM yyyy HH:mm:ss 'GMT'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String str = sdf.format(cd.getTime());
		response().setHeader(HeaderNames.EXPIRES, str);
		response().setHeader(CACHE_CONTROL, "max-age=60");
		return contents.multiGetPromise("home-banner", "daily-deals",
				"super-deals", "new-arrivals", "send-email-home",
				"featured-items", "hot-sales", "clearance-sales",
				"hot-categories", "hot-events", "hot_columns", "ad-right",
				"like-onfacebook", "hot-product", "send-email-home-2",
				"hot-sales-more-link", "clearance-sales-more-link",
				"advert-home-top", "advert-home-head")
				//
				.map((Map<String, Html> content) -> ok(views.html.base.home.index
						.render(content)));
	}

	public static Promise<Result> notFoundPromiseResult() {
		return Promise.pure(notFoundResult());
	}

	public static Promise<Result> errorPromiseResult(RequestHeader request,
			Throwable t) {
		return Promise.pure(errorResult(request, t));
	}

	public static Result notFoundResult() {
		String header = request().getHeader("Referer");
		return notFound(views.html.base.not_found.render(header));
	}

	public static Result errorResult(RequestHeader request, Throwable t) {
		String errorCode = RandomStringUtils.random(8,
				"abcdefghijkmnpqrstuvwxyz23456789");
		Logger.error(generateErrorLine(errorCode, request), t);
		if (t != null && t.getCause() != null) {
			Logger.error("Further cause", t.getCause());
		}
		return internalServerError(views.html.base.error.render(request, t,
				errorCode));
	}

	private static String generateErrorLine(String errorCode,
			RequestHeader request) {
		StringBuilder sb = new StringBuilder();
		sb.append("Application Error #");
		sb.append(errorCode);
		sb.append(" IP=");
		sb.append(request.remoteAddress());
		sb.append(" LTC=");
		sb.append(request.cookie(CookieTrackingFilter.TT_LTC) != null ? request
				.cookie(CookieTrackingFilter.TT_LTC).value() : "<null>");
		sb.append("\nRequest (");
		sb.append(request.method());
		sb.append(") [");
		sb.append(request.uri());
		sb.append("]");
		return sb.toString();
	}
}
