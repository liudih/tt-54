package controllers.interaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductCategoryMapperMapper;

import org.springframework.beans.BeanUtils;

import authenticators.member.MemberLoginAuthenticator;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.FoundationService;
import services.base.captcha.CaptchaService;
import services.interaction.product.post.ProductPostEvaluateService;
import services.interaction.product.post.ProductPostService;
import services.interaction.product.post.ProductPostTypeService;
import services.member.login.LoginService;
import services.product.CategoryEnquiryService;
import services.product.IProductBadgeService;
import valueobjects.base.Page;
import valueobjects.member.MemberInSession;
import valueobjects.product.ProductBadge;
import valueobjects.product.category.CategoryReverseComposite;
import dto.ProductPostType;
import dto.interaction.ProductPost;
import dto.interaction.ProductPostEvaluate;
import dto.product.ProductCategoryMapper;
import forms.interaction.ProductPostEvaluteForm;
import forms.interaction.ProductPostForm;

public class Post extends Controller {

	@Inject
	CaptchaService captchaService;

	@Inject
	ProductPostService postServer;

	@Inject
	ProductPostTypeService typeService;

	@Inject
	ProductPostEvaluateService evaluateService;

	@Inject
	LoginService loginService;

	@Inject
	FoundationService foundation;

	@Inject
	IProductBadgeService productBadgeService;

	@Inject
	ProductCategoryMapperMapper mapper;

	@Inject
	CategoryEnquiryService enquiry;

	public Result list(String listingid, int typeid, int page, int pageSize) {

		int languageid = foundation.getLanguage();
		String currency = foundation.getCurrency();

		List<ProductCategoryMapper> categories = mapper
				.selectByListingId(listingid);

		CategoryReverseComposite rev = null;

		if (categories != null && categories.size() > 0) {
			rev = enquiry.getReverseCategory(categories.get(0).getIcategory(),
					languageid, foundation.getSiteID());
		}

		ProductBadge bagde = productBadgeService.getByListing(listingid,
				languageid,currency);

		List<ProductPostType> types = typeService.getAll();

		Page<dto.ProductPost> resultPage = postServer.getByListingidPage(
				listingid, typeid, page, pageSize);
		return ok(views.html.interaction.product.post.list.render(bagde, types,
				resultPage, rev, loginService.getLoginEmail()));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result add() {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Form<ProductPostForm> form = Form.form(ProductPostForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			resultMap.put("required", form.errorsAsJson());
			resultMap.put("errorCode", 1);
			return ok(Json.toJson(resultMap));
		}
		ProductPostForm postForm = form.get();
		ProductPost post = new ProductPost();
		BeanUtils.copyProperties(postForm, post);
		boolean result = postServer.addProductPost(post);
		if (result) {
			resultMap.put("errorCode", 0);
		} else {
			resultMap.put("errorCode", 1);
		}
		return ok(Json.toJson(resultMap));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result addEvalute() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		MemberInSession session = loginService.getLoginData();
		if (session == null) {
			resultMap.put("errorCode", 1);
			return ok(Json.toJson(resultMap));
		}
		String email = session.getEmail();

		Form<ProductPostEvaluteForm> form = Form.form(
				ProductPostEvaluteForm.class).bindFromRequest();
		if (form.hasErrors()) {
			resultMap.put("required", form.errorsAsJson());
			resultMap.put("errorCode", 2);
			return ok(Json.toJson(resultMap));
		}

		ProductPostEvaluteForm evaluteForm = form.get();
		ProductPostEvaluate evalute = new ProductPostEvaluate();
		BeanUtils.copyProperties(evaluteForm, evalute);
		evalute.setCmemberemail(email);
		if (evaluateService.checkExtist(evaluteForm.getIpostid(), email)) {
			resultMap.put("errorCode", 3);
			return ok(Json.toJson(resultMap));
		}
		boolean result = evaluateService.addEvalute(evalute);
		if (!result) {
			resultMap.put("errorCode", 4);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 0);
		return ok(Json.toJson(resultMap));
	}

}
