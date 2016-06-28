package controllers.interaction.review;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.captcha.CaptchaService;
import services.base.utils.StringUtils;
import services.interaciton.review.ProductReviewsService;
import services.interaction.ProductPhotosService;
import services.loyalty.IPointsService;
import services.member.IMemberRoleService;
import services.member.IMemberUpdateService;
import services.member.login.LoginService;
import services.order.OrderDetailService;
import services.price.PriceService;
import services.product.IProductBadgeService;
import session.ISessionService;
import valueobjects.base.Page;
import valueobjects.member.MemberInSession;
import valueobjects.product.ProductBadge;
import authenticators.member.MemberLoginAuthenticator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.ReviewsCountList;
import dto.interaction.Foverallrating;
import dto.interaction.InteractionComment;
import dto.interaction.InteractionCommentHelpEvaluate;
import dto.interaction.InteractionProductMemberVideo;
import dto.member.MemberBase;
import dto.order.OrderDetail;
import dto.product.ProductBase;
import forms.interaction.review.CommentsEvaluteForm;
import forms.interaction.review.ProductReviewForm;

public class ProductReview extends Controller {
	// 保存错误
	public static final int SAVE_ERROR = -3;
	// 错误ID
	public static final int ERROR_ID = -2;
	// 未登录
	public static final int NOT_LOGIN = -1;
	// 登入没错
	public final static int NOT_ERROR = 0;
	// 会员已提交过
	public final static int ALREADY_EXIST = 00;
	// 可以提交
	public final static int CHECKED = 1;
	// 保存成功
	public final static int SAVE_SUCCESS = 2;
	// 每页显示总条数
	public final static int PAGESIZE = 10;

	final static int SERVER_ERRORS = 1;

	final static int CONTENT_TYPE_ERROR = 2;

	final static int SIZE_ERROR = 3;

	final static int ACCEPT_SIZE = 50;

	final int notError = 0;

	final int typeError = -4;

	final int serverError = -5;

	final int uploadError = -6;

	final int maxNumEerror = -7;

	final int maxUploadNum = 5;

	final String guid = "TYPEIID";

	final String countid = "countid";

	@Inject
	LoginService loginService;

	@Inject
	ProductReviewsService productReviewsService;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	FoundationService foundationService;

	@Inject
	CaptchaService captchaService;

	@Inject
	IMemberUpdateService memberUpdateService;

	@Inject
	ProductPhotosService photosService;

	@Inject
	ISessionService sessionService;

	@Inject
	PriceService priceService;

	@Inject
	IPointsService pointsService;

	@Inject
	OrderDetailService orderDetailService;

	@Inject
	IMemberRoleService memberRoleService;

	public Result list(int productId, int categoryId) {
		return TODO;
	}

	/**
	 * @author luoys
	 * @date Jan.22.2015
	 * @description look the function of the product review details
	 * @param listingId
	 * @param page
	 * @return counts,scores,pages,productBase,productBaeImageURL,name,refURL
	 */
	public Result showAll(String listingId, int page) {
		int lang = foundationService.getLanguage();
		int site = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		List<ProductBadge> badges = badgeService.getProductBadgesByListingIDs(
				Lists.newArrayList(listingId), lang, site, currency, null,
				false, false);
		if (badges.size() == 0) {
			return notFound();
		}
		Double scores = productReviewsService.getAverageScore(listingId);
		Page<ReviewsCountList> pages = productReviewsService.getPages(
				listingId, page, PAGESIZE);

		Integer starsWidth = (int) ((scores * 0.1) / 5 * 1000);
		List<Foverallrating> fList = productReviewsService
				.getFoverallratingsGroup(listingId);
		List<Html> loginButtons = loginService.getOtherLoginButtons();
		return ok(views.html.interaction.review.product_review_list.render(
				badges.get(0), scores, pages, starsWidth, fList, loginButtons));
	}

	/**
	 * @author luoys
	 * @date Apr.13.2015
	 * @description Comments useful help and no help
	 * @return resultMap
	 */
	@Authenticated(MemberLoginAuthenticator.class)
	public Result addEvalute() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		MemberInSession session = loginService.getLoginData();
		if (session == null) {
			resultMap.put("errorCode", 1);
			return ok(Json.toJson(resultMap));
		}
		String email = session.getEmail();

		Form<CommentsEvaluteForm> form = Form.form(CommentsEvaluteForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			resultMap.put("required", form.errorsAsJson());
			resultMap.put("errorCode", 2);
			return ok(Json.toJson(resultMap));
		}

		CommentsEvaluteForm evaluteForm = form.get();
		InteractionCommentHelpEvaluate evalute = new InteractionCommentHelpEvaluate();
		BeanUtils.copyProperties(evaluteForm, evalute);
		evalute.setCmemberemail(email);
		if (productReviewsService.checkExtist(evaluteForm.getIcommentid(),
				email)) {
			resultMap.put("errorCode", 3);
			return ok(Json.toJson(resultMap));
		}
		boolean result = productReviewsService.addEvalute(evalute);
		if (!result) {
			resultMap.put("errorCode", 4);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 0);
		return ok(Json.toJson(resultMap));
	}

	/**
	 * @author luoys
	 * @date Jan.24.2015
	 * @description To get the customer's comment on the product properties and
	 *              show to write reviews page
	 * @param listingID
	 * @return listingID, badge, form
	 */
	@Authenticated(MemberLoginAuthenticator.class)
	public Result addReview(String listingID) {
		// 订单详情id
		String orderDetailId = request().getQueryString("orderDetailId");
		String isVip = request().getQueryString("isVip");
		Logger.debug("+++++orderDetailId+++++" + orderDetailId);
		Logger.debug("+++++isVip+++++" + isVip);

		int counts = productReviewsService.getReviewCount(listingID);
		Double scores = productReviewsService.getAverageScore(listingID);

		ProductBadge badge = getProductBadge(listingID);
		Integer starsWidth = (int) ((scores * 0.1) / 5 * 1000);
		List<Foverallrating> fList = productReviewsService
				.getFoverallratingsGroup(listingID);
		String email = loginService.getLoginData().getEmail();

		return ok(views.html.interaction.review.product_write_review.render(
				counts, scores, badge, starsWidth, fList, email, orderDetailId,
				isVip));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result doAddPicture(String listingID) {
		Logger.debug("in doAddPicture ================= ");
		Map<String, Object> resultMap = Maps.newHashMap();
		MultipartFormData body = request().body().asMultipartFormData();

		Form<ProductReviewForm> form = Form.form(ProductReviewForm.class)
				.bindFromRequest();

		if (form.hasErrors()) {
			resultMap.put("errorCode", 2);
			resultMap.put("required", form.errorsAsJson());
			return ok(Json.toJson(resultMap));
		}

		ProductReviewForm reviewForm = form.get();
		Integer index = reviewForm.getIndex();
		List<FilePart> parts = body.getFiles();
		for (FilePart part : parts) {
			String contentType = part.getContentType();

			File file = part.getFile();
			if (file != null) {
				photosService.pushPreveiwImageWithSession(file,
						Integer.valueOf(index), contentType);
				resultMap.put("index", index);
				resultMap.put("clistingid", listingID);
				resultMap.put("errorCode", notError);
				return ok(Json.toJson(resultMap));
			}
			resultMap.put("errorCode", serverError);
		}
		return ok(Json.toJson(resultMap));
	}

	/**
	 * @author luoys
	 * @date Jan.24.2015
	 * @description
	 * @param listingID
	 * @return badge.getUrl()
	 */
	@Authenticated(MemberLoginAuthenticator.class)
	public Result doAddReview(String listingID) {
		Map<String, Object> resultMap = Maps.newHashMap();
		String email = loginService.getLoginData().getEmail();
		Form<ProductReviewForm> form = Form.form(ProductReviewForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			resultMap.put("errorCode", 2);
			resultMap.put("required", form.errorsAsJson());
			return ok(Json.toJson(resultMap));
		}
		ProductReviewForm reviewForm = form.get();
		String isVip = reviewForm.getIsVip();
		// 判断 isVip
		if (isVip == null || "".equals(isVip)) {
			String orderDetailId = productReviewsService.checkReview(email,
					listingID);
			if (orderDetailId == null) {
				resultMap.put("result", "no-order");
				return ok(Json.toJson(resultMap));
			}
		}
		ProductBase productBase = productReviewsService
				.getProductBaseByListingId(listingID);
		MemberBase mb = new MemberBase();

		if (mb.getCaccount() == null) {
			mb.setCaccount(reviewForm.getNickname());
			memberUpdateService.SaveMemberAccount(reviewForm.getEmail());
		}
		int site = foundationService.getSiteID();
		Double foverallrating = (double) Math.round(reviewForm
				.getFoverallrating());
		InteractionComment ic = new InteractionComment();
		Integer iid = productReviewsService.getMaxId();
		ic.setIid(iid);
		ic.setClistingid(listingID);
		ic.setCsku(productBase.getCsku());
		ic.setCmemberemail(email);
		ic.setCcomment(reviewForm.getCcomment());
		ic.setIprice(reviewForm.getIprice());
		ic.setIquality(reviewForm.getIquality());
		ic.setIshipping(reviewForm.getIshipping());
		ic.setIusefulness(reviewForm.getIusefulness());
		ic.setFoverallrating(foverallrating);
		ic.setIstate(0);
		ic.setIwebsiteid(site);
		ic.setDcreatedate(new Date());
		if (isVip == null || "".equals(isVip)) {
			ic.setCplatform("tomtop");
			// 存入订单id
			if (StringUtils.notEmpty(reviewForm.getOrderDetailId())) {
				OrderDetail od = orderDetailService
						.getOrderDetailByCid(reviewForm.getOrderDetailId());
				if (od != null) {
					ic.setIorderid(od.getIorderid());
				}
			}
			productReviewsService.doAddProductReview(ic);
			// 订单详情存入评论id
			if (StringUtils.notEmpty(reviewForm.getOrderDetailId())
					&& ic.getIid() != null) {
				Logger.debug("reviewId=====" + ic.getIid());
				orderDetailService.updateDetailCommentId(ic.getIid(),
						reviewForm.getOrderDetailId());
			}
		} else {
			ic.setCplatform(isVip);
			productReviewsService.doAddProductReview(ic);
		}
		// 添加视频
		if (reviewForm.getVideoUrl() != null
				&& !"".equals(reviewForm.getVideoUrl())) {
			InteractionProductMemberVideo video = new InteractionProductMemberVideo();
			video.setClistingid(listingID);
			video.setCsku(productBase.getCsku());
			video.setCmemberemail(email);
			video.setIcomment(ic.getIid());
			video.setCvideourl(reviewForm.getVideoUrl());
			video.setClabel(reviewForm.getVideoTitle());
			video.setIauditorstatus(0);
			video.setIwebsiteid(site);
			video.setDcreatedate(new Date());
			productReviewsService.addVideoReview(video);
		}

		// 添加图片
		String previewIndexs = reviewForm.getIndexs();
		if (previewIndexs != null && !"".equals(previewIndexs)) {
			boolean result = photosService.addphotos("comment", previewIndexs,
					listingID, productBase.getCsku(), site, ic.getIid(), email);
		}

		Promise.promise(() -> productReviewsService.sendEmailReviewSubmit(
				email, foundationService.getWebContext()));
		resultMap.put("errorCode", 0);
		resultMap.put("result", "success");
		return ok(Json.toJson(resultMap));
	}

	private ProductBadge getProductBadge(String listingID) {
		List<ProductBadge> badges = badgeService.getProductBadgesByListingIDs(
				Lists.newArrayList(listingID), foundationService.getLanguage(),
				foundationService.getSiteID(), foundationService.getCurrency(),
				null);
		if (badges.size() < 1) {
			throw new RuntimeException("Listing " + listingID + " not found");
		}
		ProductBadge badge = badges.get(0);
		return badge;
	}

	/**
	 * @author luoys
	 * @date Jan.24.2015
	 * @description Check whether the login
	 * @return resultMap
	 */
	// 驗證是否已經登錄 和 是否可以评论
	public Result checkSign(String listingId) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		MemberInSession session = loginService.getLoginData();
		if (session == null || session.getEmail() == null
				|| "".equals(session.getEmail())) {
			mjson.put("result", "nologin");
			return ok(Json.toJson(mjson));
		}
		mjson.put("isvip", "");
		String orderDetailId = productReviewsService.checkReview(
				session.getEmail(), listingId);
		if (orderDetailId != null) {
			mjson.put("result", "success");
			mjson.put("cid", orderDetailId);
			return ok(Json.toJson(mjson));
		}

		Integer memberId = session.getMemberId();
		List<Integer> list = memberRoleService.getRoleIdByUserId(memberId);
		if (list != null && list.contains(1)) {
			mjson.put("isvip", "webvip");
			mjson.put("result", "success");
			mjson.put("cid", "");
		}

		return ok(Json.toJson(mjson));
	}

}
