package controllers.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import services.base.utils.DateFormatUtils;
import services.interaciton.review.ProductReviewsService;
import services.loyalty.IPointsService;
import services.review.ReviewMangerService;
import session.ISessionService;
import util.AppsUtil;
import valueobjects.base.Page;
import services.base.FoundationService;

import play.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import dto.interaction.InteractionComment;
import entity.manager.AdminUser;
import events.product.ProductUpdateEvent;
import forms.review.ReviewForm;

public class Review extends Controller {

	@Inject
	ProductReviewsService reviewService;
	@Inject
	IPointsService pointService;
	@Inject
	ReviewMangerService reviewMangerService;
	@Inject
	ISessionService sessionService;
	@Inject
	EventBus eventBus;
	@Inject
	private FoundationService foundation;

	final static int NOT_ERROR = 0;

	final static int REQUIRED_ERROR = 1;

	final static int SERVER_ERROR = 2;

	final static String todayStr = AppsUtil.getTodayStrFormat("yyyy-MM-dd");

	@controllers.AdminRole(menuName = "ProductReviewsMgr")
	public Result reviewsManagement(Integer page, Integer pageSize, String sku,
			Integer status, String email, String startDate, String endDate,
			String content, Integer websiteid) {
		Logger.debug("sid--------------------------------------------:{}",websiteid);
		Date startDate1 = DateFormatUtils.getNowBeforeByDay(
				Calendar.WEEK_OF_MONTH, -1);
		Date endDate1 = DateFormatUtils.getCurrentTimeD();
		if (!"".equals(startDate) && !"".equals(endDate)) {
			startDate1 = DateFormatUtils.getFormatDateByStr(startDate);
			if (endDate.length() == 10) {
				endDate1 = DateFormatUtils.getFormatDateYmdhmsByStr(endDate
						+ " 23:59:59");
				;
			} else {
				endDate1 = DateFormatUtils.getFormatDateByStr(endDate);
				;
			}

		}

		String startDateString = DateFormatUtils
				.getStrFromYYYYMMDDHHMMSS(startDate1);
		String endDateString = DateFormatUtils
				.getStrFromYYYYMMDDHHMMSS(endDate1);
		List<InteractionComment> reviews = reviewService.getReviews(page,
				pageSize, sku, status, email, startDate1, endDate1, content,
				websiteid);
		Integer total = reviewService.getReviewsCount(sku, status, email,
				startDate1, endDate1, content, websiteid);
		Page<InteractionComment> reviewPage = new Page<InteractionComment>(
				reviews, total, page, pageSize);

		return ok(views.html.manager.review.review_manager.render(reviewPage,
				sku, status, email, startDateString, endDateString, content,
				websiteid));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result reviewVerify() {
		Map<String, Object> resultMap = Maps.newHashMap();
		JsonNode jsonNode = request().body().asJson();
		Integer id = jsonNode.get("id").asInt();
		Integer status = jsonNode.get("status").asInt();
		Integer points = jsonNode.get("points").asInt();
		String email = jsonNode.get("email").asText();
		Integer siteid = jsonNode.get("siteid").asInt();
		InteractionComment review = reviewService.getReviewById(id);
		if (null == review) {
			resultMap.put("errorCode", REQUIRED_ERROR);
			return ok(Json.toJson(resultMap));
		}
		review.setDauditdate(new Date());
		review.setIstate(status);
		Boolean result = reviewService.updateReview(review);

		if (result) {
			resultMap.put("errorCode", NOT_ERROR);
			resultMap.put("dauditdate", review.getDauditdate2());
			resultMap.put("state", status);
			if (!"webvip".equals(review.getCplatform())) {
				// 送积分
				siteid = siteid == 0 ? 1 : siteid;
				if (status == 1 && points <= 10 && points > 0) {
					pointService.grantPoints(email, siteid, points,
							"upload comment", "upload comment", 1,
							"upload comment");
				}
			}
			eventBus.post(new ProductUpdateEvent(review.getClistingid(),
					ProductUpdateEvent.ProductHandleType.update));
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", SERVER_ERROR);
		return ok(Json.toJson(resultMap));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result batchVerify() {
		JsonNode jsonNode = request().body().asJson();
		String ids = jsonNode.get("ids").asText();
		Integer type = jsonNode.get("type").asInt();
		Integer points = jsonNode.get("points").asInt();
		List<Integer> idList = Lists.newArrayList();
		String[] idListString = ids.split("-");
		if (idListString.length > 0) {
			for (String idString : idListString) {
				idList.add(Integer.valueOf(idString));
			}
		}
		boolean result = reviewService.batchVerify(idList, type);
		if (result) {
			// 送积分
			if (type == 1 && points != null && points <= 10 && points > 0) {
				List<InteractionComment> rlist = reviewService
						.getReviewsByIds(idList);
				for (InteractionComment i : rlist) {
					if (!"webvip".equals(i.getCplatform())) {
						int sid = i.getIwebsiteid() != null ? i.getIwebsiteid()
								: 1;
						pointService.grantPoints(i.getCmemberemail(), sid,
								points, "upload comment", "upload comment", 1,
								"upload comment");
					}
					eventBus.post(new ProductUpdateEvent(i.getClistingid(),
							ProductUpdateEvent.ProductHandleType.update));
				}
			}
			return ok("success");
		}
		return ok("faile");
	}

	public Result editComment(Integer id) {
		InteractionComment ic = reviewService.getReviewById(id);
		return ok(views.html.manager.review.review_edit.render(ic));
	}

	/**
	 * 搜索sku
	 * 
	 * @return
	 */
	public Result searchforSku(String skustr) {
		List<String> list = reviewMangerService.getSku(skustr);
		return ok(Json.toJson(list));
	}

	/**
	 * 保存商品评论
	 * 
	 * @return
	 */
	public Result saveReviewManger() {
		Form<ReviewForm> form = Form.form(ReviewForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}
		boolean result = false;
		ReviewForm rform = form.get();
		int siteId = foundation.getSiteID();
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		int count = reviewMangerService.addReview(rform, rform.getWebsiteid(), user.getCusername(),
				reviewService);
		if(count>0){
			result = true;
		}
		return ok(Json.toJson(result));
	}

	/**
	 * 修改商品评论
	 * 
	 * @return
	 */
	public Result updateReviewManger() {
		Form<ReviewForm> form = Form.form(ReviewForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}
		boolean result = false;
		ReviewForm rform = form.get();
		int count = reviewMangerService.updateReview(rform, reviewService);
		if(count>0){
			result = true ;
		}
		return ok(Json.toJson(result));
	}

	/**
	 * 验证Sku是否存在
	 * 
	 * @return
	 */
	public Result vaildSku(String sku,Integer websiteid) {
		String listingid = reviewMangerService.getListingIdBySku(sku, websiteid);
		String res = "";
		if (listingid != null && !"".equals(listingid)) {
			res = "success";
		}

		return ok(res);
	}

	/**
	 * 验证邮箱格式
	 * 
	 * @return
	 */
	public Result vaildEmail(String email) {
		boolean b = AppsUtil.checkEmail(email);
		String res = "";
		if (b) {
			res = "success";
		}
		return ok(res);
	}

	/**
	 * 验证时间格式
	 * 
	 * @return
	 */
	public Result vaildDate(String date) {
		boolean b = AppsUtil.isValidDate(date);
		String res = "";
		if (b) {
			res = "success";
		}
		return ok(res);
	}

	/**
	 * 下载template
	 * 
	 * @return
	 */
	public Result downloadTemplate() {
		return redirect(routes.Assets.at("/temp/template.xls"));
	}

	/**
	 * 上传xls文件
	 * 
	 * @return
	 */
	public Result upload() {
		MultipartFormData body = request().body().asMultipartFormData();
		int siteId = foundation.getSiteID();
		List<FilePart> parts = new ArrayList<FilePart>();
		File file = null;
		if (body != null) {
			parts = body.getFiles();
			file = parts.get(0).getFile();
		}
		String[][] result = null;
		result = reviewMangerService.getData(file, 1);
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		int rowLength = result.length;
		List<ReviewForm> rformlist = new ArrayList<ReviewForm>();
		ReviewForm rform = null;
		StringBuffer msg = new StringBuffer();
		String listingid = "";
		Integer usef = 0;
		Integer ship = 0;
		Integer price = 0;
		Integer quality = 0;
		boolean b = false;
		for (int i = 0; i < rowLength; i++) {
			rform = new ReviewForm();
			listingid = reviewMangerService.getListingIdBySku(
					AppsUtil.trim(result[i][0]), Integer.parseInt(result[i][8]));
			if (listingid != null && !"".equals(listingid)) {
				rform.setCsku(result[i][0]);
			} else {
				msg.append("第" + (i + 1) + "行 sku不正确 (" + result[i][0] + ")\n");
			}
			try {
				usef = Integer.parseInt(result[i][1]);
				if (usef < 1 || usef > 5) {
					msg.append("第" + (i + 1) + "行 Usefulness 数字只能 1-5 星\n");
				} else {
					rform.setUsefulness(usef);
				}
				ship = Integer.parseInt(result[i][2]);
				if (ship < 1 || ship > 5) {
					msg.append("第" + (i + 1) + "行 Shipping 数字只能 1-5 星\n");
				} else {
					rform.setShipping(ship);
				}
				price = Integer.parseInt(result[i][3]);
				if (price < 1 || price > 5) {
					msg.append("第" + (i + 1) + "行 Price 数字只能 1-5 星\n");
				} else {
					rform.setPrice(price);
				}
				quality = Integer.parseInt(result[i][4]);
				if (quality < 1 || quality > 5) {
					msg.append("第" + (i + 1) + "行 Quality 数字只能 1-5 星\n");
				} else {
					rform.setQuality(quality);
				}
			} catch (Exception e) {
				msg.append("第" + (i + 1) + "行 数字类型转化失败 \n");
			}
			if ("".equals(AppsUtil.trim(result[i][5]))) {
				msg.append("第" + (i + 1) + "行 Details 不得为空 \n");
			} else if (AppsUtil.trim(result[i][5]).length() > 1000) {
				msg.append("第" + (i + 1) + "行 Details 字符长度不得超过1000 \n");
			} else {
				rform.setCdtl(result[i][5]);
			}
			b = AppsUtil.checkEmail(AppsUtil.trim(result[i][6]));
			if (b) {
				rform.setCemail(result[i][6]);
			} else {
				msg.append("第" + (i + 1) + "行 邮件格式不正确(" + result[i][6] + ")\n");
			}
			b = AppsUtil.isValidUploadDate(AppsUtil.trim(result[i][7]));
			if (b) {
				rform.setDdate(result[i][7]);
			} else {
				msg.append("第" + (i + 1) + "行 时间格式不正确(" + result[i][7] + ")\n");
			}
			rform.setWebsiteid(Integer.parseInt(result[i][8]));
			rformlist.add(rform);
		}
		if (msg.length() == 0) {
			reviewMangerService.uploadReview(rformlist, Integer.parseInt(result[0][8]), user.getCusername(),
					reviewService);
			return redirect(controllers.manager.routes.Review
					.reviewsManagement(1, 15, "", 3, "", todayStr, todayStr,
							null, 1));
		} else {
			return ok(msg.toString());
		}
	}
}
