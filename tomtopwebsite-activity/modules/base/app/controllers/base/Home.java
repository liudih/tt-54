package controllers.base;

import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.inject.Inject;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.activity.template.TemplateService;
import services.base.utils.MetaUtils;
import valueobjects.base.template.TemplateComposite;
import valueobjects.price.Price;
import valueobjects.product.spec.SingleProductSpec;
import filters.common.CookieTrackingFilter;

public class Home extends Controller {

	@Inject
	//services.product.IProductBadgeServic productBadgeServic;
//	@Inject
//	services.product.IEntityMapService ientityMapService;
//	@Inject
//	services.product.IProductEnquiryService iproductEnquiryService;

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

		 valueobjects.product.ProductBadge pb = new  valueobjects.product.ProductBadge();
		 pb.setImageUrl("/d/f/e.jpg");
		 pb.setTitle("tets-------------------sefwe");
		 Price p =new Price(new SingleProductSpec("sss",1));
		 p.setCurrency("USD");
		 p.setDiscount(0.1);
		 p.setUnitPrice(8);
		 pb.setPrice(p);
		 pb.setUrl("sfewfwe.html");
		return contents.multiGetPromise("home-banner", "daily-deals",
				"super-deals", "new-arrivals", "send-email-home",
				"featured-items", "hot-sales", "clearance-sales",
				"hot-categories", "hot-events", "hot_columns", "ad-right",
				"like-onfacebook", "hot-product", "send-email-home-2",
				"hot-sales-more-link", "clearance-sales-more-link",
				"advert-home-top").map(
				(Map<String, Html> content) -> ok(views.html.base.home.index
						.render("index",pb)));

		// return Promise.pure(ok(views.html.base.product.category_badge_list.render(pb)));
		// return Promise.pure(ok("index"));
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
		return notFound("not found");
		// return notFound(views.html.base.not_found.render(header));
	}

	public static Result errorResult(RequestHeader request, Throwable t) {
		String errorCode = RandomStringUtils.random(8,
				"abcdefghijkmnpqrstuvwxyz23456789");
		Logger.error(generateErrorLine(errorCode, request), t);
		if (t != null && t.getCause() != null) {
			Logger.error("Further cause", t.getCause());
		}
		//return internalServerError(errorCode);
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
