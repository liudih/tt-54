package controllers.interaction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;

import play.Logger;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.utils.FileUtils;
import services.base.utils.StringUtils;
import services.interaction.IProductPhotosService;
import services.interaction.ProductReviewService;
import services.interaction.review.IProductReviewsService;
import services.order.IOrderDetailService;
import services.product.IEntityMapService;
import services.product.IProductBadgeService;
import valueobjects.base.Page;
import valueobjects.product.ProductBadge;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import controllers.MemberLoginValidate;
import dto.ReviewsCountList;
import dto.interaction.Foverallrating;
import dto.interaction.InteractionComment;
import dto.interaction.InteractionProductMemberPhotos;
import dto.order.OrderDetail;
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
	IProductReviewsService productReviewsService;
	
	@Inject
	ProductReviewService productReviewsService2;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	FoundationService foundationService;
	@Inject
	IEntityMapService entityMapService;
	@Inject
	IOrderDetailService orderDetailService;
	@Inject
	IProductPhotosService photosService;

	public Result showAll(String listingId, int page) {
		int lang = foundationService.getLanguage();
		int site = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		List<ProductBadge> badges = badgeService.getProductBadgesByListingIDs(Lists.newArrayList(listingId), lang, site, currency, null,
				false, false);
		if (badges.size()==0) {
			return notFound();
		}
		
		Page<ReviewsCountList> pages = productReviewsService.getPages(
				listingId, page, PAGESIZE);
		
		//从更多按钮进来的
		String ismore = request().getQueryString("ismore");
		if(ismore!=null && "1".equals(ismore)){
			Map<String,Object> mjson = new HashMap<String,Object>();
			mjson.put("html", views.html.interaction.fragment.comment_list_common.render(pages.getList()).toString());
			mjson.put("totlePage", pages.totalPages());
			mjson.put("page", pages.pageNo());
			return ok(Json.toJson(mjson));
		}
		
		Double scores = productReviewsService.getAverageScore(listingId);	
		Integer starsWidth = (int) ((scores * 0.1) / 5 * 1000);
		List<Foverallrating> fList = productReviewsService
				.getFoverallratingsGroup(listingId);
		return ok(views.html.interaction.review.review_list.render(
				badges.get(0),scores, pages,
				starsWidth, fList));
	}
	
	@MemberLoginValidate
	public Result addReview(String listingID) {
		String orderDetailId = request().getQueryString("orderDetailId");
		Logger.debug("+++++orderDetailId+++++"+orderDetailId);
		int lang = foundationService.getLanguage();
		String email = foundationService.getLoginContext().getMemberID();
		int counts = productReviewsService.getReviewCount(listingID);
		Double scores = productReviewsService.getAverageScore(listingID);
		
		ProductBadge badge = badgeService.getByListing(listingID, foundationService.getWebContext());
		Integer starsWidth = (int) ((scores * 0.1) / 5 * 1000);
		List<Foverallrating> fList = productReviewsService
				.getFoverallratingsGroup(listingID);
		Map<String, String> attributeMap = entityMapService
				.getAttributeMap(listingID, lang);
		return ok(views.html.interaction.review.review_write.render(counts, scores,
				badge, starsWidth, fList, email, attributeMap, orderDetailId));
	}
	
	@MemberLoginValidate
	public Result doAddReview(String listingID) {
		Map<String, Object> resultMap = Maps.newHashMap();
		String email = foundationService.getLoginContext().getMemberID();
		String orderDetailId = productReviewsService.checkReview(email, listingID);
		if (orderDetailId == null) {
			resultMap.put("result", "no-order");
			return ok(Json.toJson(resultMap));
		}
		
		Form<ProductReviewForm> form = Form.form(ProductReviewForm.class)
				.bindFromRequest();
		
		if (form.hasErrors()) {
			resultMap.put("errorCode", 2);
			resultMap.put("required", form.errorsAsJson());
			return ok(Json.toJson(resultMap));
		}
		
		ProductReviewForm reviewForm = form.get();
		int site = foundationService.getSiteID();
		Logger.debug("reviewForm ================= ");
		Double foverallrating = (double) Math.round(reviewForm.getFoverallrating());
		InteractionComment ic = new InteractionComment();
		ic.setClistingid(listingID);
		ic.setCmemberemail(email);
		ic.setCcomment(reviewForm.getCcomment());
		ic.setIprice(reviewForm.getIprice());
		ic.setIquality(reviewForm.getIquality());
		ic.setIshipping(reviewForm.getIshipping());
		ic.setIusefulness(reviewForm.getIusefulness());
		ic.setFoverallrating(foverallrating);
		ic.setIstate(0);
		ic.setCplatform("mobile");
		ic.setIwebsiteid(site);
		ic.setDcreatedate(new Date());
		ic.setCsku(reviewForm.getScku());
		//存入订单id
		if(StringUtils.notEmpty(reviewForm.getOrderDetailId())){
			OrderDetail od = orderDetailService.getOrderDetailByCid(reviewForm.getOrderDetailId());
			if(od!=null){
				ic.setIorderid(od.getIorderid());
			}
		}
		Integer reviewId= productReviewsService.doAddProductReview(ic);
		//订单详情存入评论id
		if(StringUtils.notEmpty(reviewForm.getOrderDetailId()) && reviewId!=null){
			Logger.debug("reviewId====="+reviewId);
			orderDetailService.updateDetailCommentId(reviewId, reviewForm.getOrderDetailId());
		}
		
		//添加图片
		String previewIndexs = reviewForm.getIndexs();
		if(previewIndexs!=null && !"".equals(previewIndexs)){
			boolean result = photosService.addphotosForWeb("comment", previewIndexs,
					listingID, null, site, reviewId, email);
		}
		
		Promise.promise(()->productReviewsService.sendEmailReviewSubmit(email, foundationService.getWebContext()));
		resultMap.put("errorCode", 0);
		resultMap.put("result", "success");
		return ok(Json.toJson(resultMap));
	}
	
	@MemberLoginValidate
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
				//Logger.debug("file=="+file.getAbsoluteFile());
				Base64 base64=new Base64();
				byte[] b = base64.encode(FileUtils.toByteArray(file));
				productReviewsService2.pushPreveiwImageWithSession(new String(b),
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
	
	public Result getPreveiwImageWithSession(int index) {
		byte[] bytes = productReviewsService2.getPreveiwImageWithSession(index);
		return ok(bytes).as(photosService.getContentTypeWithSession(index));
	}
	
	public Result delPreveiwImageWithSession(int index) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		photosService.delPreveiwImageWithSession(index);
		resultMap.put("index", index);
		resultMap.put("errorCode", notError);
		return ok(Json.toJson(resultMap));

	}

	public Result at(int iid) {
		InteractionProductMemberPhotos p = photosService.getPhotoById(iid);
		if (p != null) {
			byte[] bs = getImagePart(iid);
			return ok(bs).as(p.getCcontenttype());
		}
		return badRequest();
	}
	private byte[] getImagePart(int iid) {
		int start = 0;
		int len = 1024 * 10;
		byte[] blist = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			while (true) {
				blist = photosService.getPhotoPartById(iid, start, len);
				if (blist == null) {
					break;
				}
				Logger.debug("start " + start + "  end  " + len);
				out.write(blist);
				start += len;
			}
			return out.toByteArray();
		} catch (Exception ex) {
			Logger.error("get img error: ", ex);
		} finally {
			try {
				out.flush();
				out.close();
			} catch (Exception ex) {
				Logger.error("close img error: ", ex);
			}
		}
		return null;
	}
	

}
