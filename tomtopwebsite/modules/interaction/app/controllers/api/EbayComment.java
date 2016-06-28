package controllers.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.interaction.InteractionCommentMapper;
import mapper.product.ProductBaseMapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.base.utils.DateFormatUtils;
import services.base.FoundationService;
import services.base.api.ApiBase;
import services.base.api.ServiceResponse;
import services.base.api.ServiceResponseCode;
import services.interaciton.review.ProductReviewsUpdateService;
import valueobjects.interaction.SimpleComment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import dto.interaction.InteractionComment;
import dto.product.ProductBase;

public class EbayComment extends ApiBase {

	// add by wujirui 2015-06-11 begin
	// 商品评论状态-- 未审核
	private static final Integer STATE_WAITING_AUDIT = 0;

	// 商品评论状态-- 审核通过
	private static final Integer STATE_APPROVE = 1;

	// 商品评论状态-- 审核不通过
	private static final Integer STATE_OPPOSE = 2;

	// 好评
	public final static String POSITIVE = "POSITIVE";

	// 中评
	public final static String NEUTRAL = "NEUTRAL";

	// 差评
	public final static String NEGATIVE = "NEGATIVE";
	// add by wujirui 2015-06-11 end

	@Inject
	InteractionCommentMapper interactionCommentMapper;
	@Inject
	ProductReviewsUpdateService productReviewsUpdateService;
	@Inject
	FoundationService foundationService;
	@Inject
	ProductBaseMapper productBaseMapper;

	private Boolean _checkCommentExits(String sku, String email,
			String commentContent) {
		InteractionComment comment = interactionCommentMapper
				.getCommentByParams(sku, email, commentContent);
		if (null == comment) {
			return true;
		}

		return false;
	}

	/**
	 * ERP定时推送ebay评论数据，然后保存到新网站 任务执行时间：
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result getEbayCommentByERP() {
		ServiceResponse serviceResponse = new ServiceResponse();
		try {
			JsonNode node = request().body().asJson();
			if (node == null) {
				return badRequest("Expecting Json data");

			}
			ObjectMapper objectMapper = new ObjectMapper();
			Object resultObj = node.get("result");
			List<Map<String, String>> resutList = objectMapper.readValue(
					resultObj.toString(), List.class);
			Integer counter = 0;
			if (null != resutList) { // modify by wujirui 增加判断，防止出现空指针异常
				counter = resutList.size();
			}
			Logger.debug("------------count: " + counter);
			int websiteId = foundationService.getSiteID();
			List<String> commentType = new ArrayList<>();
			commentType.add(productReviewsUpdateService.POSITIVE);
			commentType.add(productReviewsUpdateService.NEUTRAL);
			HashSet<String> skus = new HashSet<>();
			List<SimpleComment> simpleComments = new ArrayList<>();
			for (Object object : resutList) {
				Map<String, String> comment = (Map<String, String>) object;
				String sku = comment.get("sku");
				String feedbackType = comment.get("feedbackType");
				// 只导入好评中评
				if (!commentType.contains(feedbackType)) {
					continue;
				}
				String customerEmail = comment.get("email");
				if (StringUtils.isEmpty(customerEmail)) {
					continue;
				}
				SimpleComment simpleComment = new SimpleComment();
				simpleComment.setSku(sku);
				simpleComment.setEmail(customerEmail);
				simpleComments.add(simpleComment);
				skus.add(sku);
			}

			List<ProductBase> productBases = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(skus)) {
				productBases = productBaseMapper.checkSkuListingExits(
						new ArrayList<String>(skus), websiteId);
			}
			HashMap<String, String> skuListingMap = new HashMap<>();
			for (ProductBase productBase : productBases) {
				skuListingMap.put(productBase.getCsku(),
						productBase.getClistingid());
			}
			List<InteractionComment> interactionComments = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(simpleComments)) {
				interactionComments = interactionCommentMapper
						.checkSkuCommentExits(simpleComments);
			}
			Multimap<String, String> skuEmailMap = ArrayListMultimap.create();
			Multimap<String, String> skuCommentMap = ArrayListMultimap.create();
			for (InteractionComment interactionComment : interactionComments) {
				skuEmailMap.put(interactionComment.getCsku(),
						interactionComment.getCmemberemail());
				skuCommentMap.put(interactionComment.getCsku(),
						interactionComment.getCcomment());
			}

			String[] gradeArray = new String[5];
			for (Object object : resutList) {
				Map<String, String> comment = (Map<String, String>) object;
				String sku = comment.get("sku");
				String customerEmail = comment.get("email");
				String content = comment.get("feedbackContent");
				if (null != skuEmailMap.get(sku)) {
					if (skuEmailMap.get(sku).contains(customerEmail)
							&& skuCommentMap.get(sku).contains(content)) {
						Logger.debug("-----sku and email dumplicate:  " + sku
								+ " email: " + customerEmail);
						continue;
					}
				}
				String createdTime = comment.get("feedbackTime");
				String country = comment.get("country");
				if (null == skuListingMap.get(sku)) {
					Logger.debug("-----no listing id for this sku: " + sku);
					continue;
				}
				if (StringUtils.isEmpty(content)) {
					content = "Good seller!";
				} else {
					content = content.replaceAll("(?i)ebayer", "seller");
					content = content.replaceAll("(?i)ebay ", "");
				}
				InteractionComment interactionComment = new InteractionComment();
				interactionComment.setClistingid(skuListingMap.get(sku));
				interactionComment.setCcomment(content);
				interactionComment.setCmemberemail(customerEmail);
				interactionComment.setCsku(sku);
				interactionComment.setDcreatedate(DateFormatUtils
						.getFormatDateYmdhmsByStr(createdTime));
				interactionComment.setIstate(STATE_WAITING_AUDIT); // 评论设置为"未审核"
																	// modify by
																	// wujirui
																	// 2015-06-11
				interactionComment.setCcountry(country);
				interactionComment.setDauditdate(null);
				interactionComment.setIwebsiteid(websiteId);

				Logger.info("Method getEbayCommentByERP---feedbackType = "
						+ comment.get("feedbackType"));

				// 如果是中评 评级分数随机生成3分或4分，其中3分和4分出现几率各占50%；
				if (NEUTRAL.equals(comment.get("feedbackType"))) {
					gradeArray = getGradeArray(0.50, 3, 0.50, 4); // 获取生成的评级
				}
				// 如果是好评 评级分数随机生成4分或5分，其中4分出现几率占70%，5分出现几率占30%；
				else if (POSITIVE.equals(comment.get("feedbackType"))) {
					gradeArray = getGradeArray(0.70, 4, 0.30, 5); // 获取生成的评级
				}

				interactionComment.setIprice(Integer.parseInt(gradeArray[0])); // 价格评级
				interactionComment.setIquality(Integer.parseInt(gradeArray[1])); // 质量评级
				interactionComment
						.setIshipping(Integer.parseInt(gradeArray[2])); // 物流评级
				interactionComment.setIusefulness(Integer
						.parseInt(gradeArray[3])); // 有用评级
				interactionComment.setFoverallrating(Double
						.parseDouble(gradeArray[4])); // 综合评级

				productReviewsUpdateService.saveComment(interactionComment);
				Logger.debug("------------left count: " + counter--);
			}

			return ok("successfully");
		} catch (Exception p) {
			Logger.error("exception error", p);
			serviceResponse.setCode(ServiceResponseCode.ERROR);
			serviceResponse.setAck(ServiceResponseCode.ERROR_ACK);
			serviceResponse.setDescription("update fail");

			return ok(Json.toJson(serviceResponse));
		}
	}

	/**
	 * author： wujirui date: 2015-06-11 随机生成一个3到5的评论分
	 * 数组中包含五个元素，最后一个元素作为“综合评级（foverallrating）”，是前四个的平均值； 前四个分别为：1.iprice（价格评级）
	 * 2.质量评级（iquality） 3.物流评级（ishipping） 4.有用评级（iusefulness）；
	 * 
	 * @return arry
	 */
	public String[] getGradeArray(double rate0, int result0, double rate1,
			int result1) {
		String[] arry = new String[5];
		int sum = 0;
		int random = 0;
		double avg = 0.0;
		for (int i = 0; i < arry.length - 1; i++) {
			random = percentageRandom(rate0, result0, rate1, result1);// 获取一个3---4的随机数
			arry[i] = String.valueOf(random);
			sum += random;
		}
		avg = sum * 1.0 / (arry.length - 1);// 计算平均值；
		arry[arry.length - 1] = String.valueOf(avg);// 将平均值放到数组的最末位
		return arry;
	}

	/**
	 * 随机生产两个数
	 * 
	 * @param rate0
	 *            第一个数出现的百分比
	 * @param result0
	 *            需要返回的第一个数
	 * @param rate1
	 *            第二个数出现的百分比
	 * @param result1
	 *            需要返回的第二个数
	 * @return
	 */
	private static int percentageRandom(double rate0, int result0,
			double rate1, int result1) {
		double randomNumber = Math.random();
		if (randomNumber >= 0 && randomNumber <= rate0) {
			return result0;
		} else if (randomNumber >= rate0 && randomNumber <= rate0 + rate1) {
			return result1;
		}
		return result1;
	}

}
