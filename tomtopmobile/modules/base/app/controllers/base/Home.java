package controllers.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.common.collect.FluentIterable;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import services.base.template.TemplateService;
import services.base.utils.MetaUtils;
import valueobjects.base.template.TemplateComposite;
import filters.common.CookieTrackingFilter;
import play.twirl.api.Html;

public class Home extends Controller {

	public Promise<Result> home() {
		
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
		List<String> showFragmentIds = Play.application().configuration().getStringList("showFragmentIds");
		Logger.debug("showFragmentIds:{}",showFragmentIds);
		return contents.multiGetPromise(showFragmentIds)
				.map((Map<String, Html> content) -> ok(views.html.base.home.index
						.render(content)));
		
//		return Promise.promise(() -> ok(views.html.base.home.index.render()));
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
