package controllers.interaction;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.interaciton.review.ProductReviewsService;
import services.interaction.product.post.ProductPostService;
import services.interaction.product.post.ProductPostTypeService;
import services.member.MemberBaseService;
import services.member.login.ILoginService;
import services.member.login.LoginService;
import services.price.PriceService;
import services.product.IProductBadgeService;
import valueobjects.base.Page;
import valueobjects.price.Price;
import valueobjects.product.ProductBadge;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.ProductSpecBuilder;
import authenticators.member.MemberLoginAuthenticator;
import dto.ProductPost;
import dto.ProductPostType;
import dto.interaction.Foverallrating;

public class MemberProductPost extends Controller {

	@Inject
	LoginService loginService;

	@Inject
	MemberBaseService memberBaseService;

	@Inject
	ProductPostService servic;

	@Inject
	ProductPostTypeService typeService;

	@Inject
	PriceService priceService;

	@Inject
	IProductBadgeService productBadgeService;

	@Inject
	FoundationService foundationService;

	@Inject
	ProductReviewsService productReviewsService;
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result list(int reply, int type, int date, int page, int pageSize) {

		String email = loginService.getLoginData().getEmail();
		Page<ProductPost> resultPage = servic.getProductPostPage(email, reply,
				type, date, page, pageSize);
		List<ProductPostType> types = typeService.getAll();

		return ok(views.html.interaction.member.product.post.list.render(
				resultPage, types));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result detail(String listingid, int iid) {

		IProductSpec spec = ProductSpecBuilder.build(listingid).setQty(1).get();
		Price price = priceService.getPrice(spec);

		int languageid = foundationService.getLanguage();
		String currency = foundationService.getCurrency();
		ProductBadge product = productBadgeService.getByListing(listingid,
				languageid,currency);

		product.setPrice(price);
		List<ProductPostType> types = typeService.getAll();
		ProductPost post = servic.getProductPostById(iid);

		List<Foverallrating> fovers = productReviewsService
				.getFoverallratingsGroup(listingid);
		int reviewTotail = productReviewsService.getReviewCount(listingid);

		double scores = productReviewsService.getAverageScore(listingid);

		int starsWidth = (int) ((scores * 0.1) / 5 * 1000);
		
		List<Html> loginButtons = loginService.getOtherLoginButtons();

		return ok(views.html.interaction.member.product.post.detail.render(
				product, post, types, listingid, fovers, reviewTotail, scores,
				starsWidth, loginService.getLoginEmail(),loginButtons));
	}

}
