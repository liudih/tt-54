package services.mobile.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.base.utils.Utils;
import services.interaction.review.IProductReviewsService;
import utils.DateUtils;
import utils.ValidataUtils;
import valueobjects.base.Page;

import com.google.common.collect.Lists;

import dto.ReviewsCountList;
import dto.interaction.Foverallrating;
import dto.mobile.ReviewsCountListInfo;

/**
 * 
 * 评论
 * 
 * @author lichong
 *
 */
public class InteractionCommentService {

	@Inject
	IProductReviewsService productReviewsService;

	public utils.Page<Map<String, Object>> getProductCommentPage(String lId,
			int p, int size) {
		Page<ReviewsCountListInfo> reviews = this.getPages(lId, p, size);
		if (reviews != null) {
			List<ReviewsCountListInfo> reviewList = reviews.getList();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (ReviewsCountListInfo review : reviewList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("comment",
						ValidataUtils.validataStr(review.getCcomment()));
				map.put("name", ValidataUtils.validataStr(review.getbName()));
				map.put("imgurls", new ArrayList<String>());
				map.put("video",
						ValidataUtils.validataStr(review.getCommentVideoUrl()));
				map.put("date", review.getDcreatedate());
				map.put("ustar", review.getFoverallratingStarWidth());
				list.add(map);
			}
			return new utils.Page<Map<String, Object>>(list,
					reviews.totalCount(), p, size);
		}
		return null;
	}

	/**
	 * 获取各星级评分
	 * 
	 * @param lid
	 * @return
	 */
	public Map<String, Object> getProductComment(String lid) {
		List<Foverallrating> foverallrating = productReviewsService
				.getFoverallratingsGroup(lid);
		Double score = productReviewsService.getAverageScore(lid);
		Integer count = productReviewsService.getReviewCount(lid);
		Map<String, Object> result = new HashMap<String, Object>();
		for (Foverallrating f : foverallrating) {
			result.put("st" + f.getType(), f.getNum());
		}
		result.put("score", ValidataUtils.validataDouble(score));
		result.put("qty", ValidataUtils.validataInt(count));
		return result;
	}

	public Map<String, Object> getProductStar(String lid) {
		Double score = productReviewsService.getAverageScore(lid);
		Integer count = productReviewsService.getReviewCount(lid);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("score", ValidataUtils.validataDouble(score));
		result.put("qty", ValidataUtils.validataInt(count));
		return result;
	}

	/**
	 * 进行用户对产品评论的分页查询详情
	 * 
	 * @param listingId
	 * @param page
	 * @param pageSize
	 * @return 产品评论详情
	 */
	public Page<ReviewsCountListInfo> getPages(String listingId, Integer page,
			Integer pageSize) {
		page = page < 1 ? 1 : page;
		pageSize = pageSize < 0 ? 6 : pageSize;
		Page<ReviewsCountList> reviewCounts = productReviewsService.getPages(
				listingId, page, pageSize);
		List<ReviewsCountListInfo> reviewsList = getReviewsCountList(reviewCounts
				.getList());
		return new Page<ReviewsCountListInfo>(reviewsList,
				reviewCounts.totalCount(), page, pageSize);
	}

	public List<ReviewsCountListInfo> getReviewsCountList(
			List<ReviewsCountList> reviews) {
		if (null == reviews || reviews.size() < 1) {
			return Lists.newArrayList();
		}
		List<ReviewsCountListInfo> reviewsInCountList = Lists.newArrayList();
		for (ReviewsCountList review : reviews) {
			Integer commentId = review.getCommentId();
			String ccomment = review.getCcomment();
			Integer iprice = review.getIpriceStarWidth();
			Integer iquality = review.getIqualityStarWidth();
			Integer ishipping = review.getIshippingStarWidth();
			Integer iusefulness = review.getIusefulness();
			double foverallrating = review.getFoverallratingStarWidth();
			Long dcreatedate = DateUtils.parseDate(review.getDcreatedate())
					.getTime();
			Integer istate = review.getIstate();
			String bName = Utils.getIncompleteEmail(review.getEmail());
			String memberPhoto = controllers.mobile.member.routes.Photo.at(
					review.getEmail()).url()
					+ "?email=" + review.getEmail();
			List<String> CommentPhotosUrl = review.getCommentPhotosUrl();
			String commentVideoUrl = review.getCommentVideoUrl();
			Integer helpfulqty = review.getHelpfulqty();
			Integer nothelpfulqty = review.getNothelpfulqty();
			String ccode = "";
			reviewsInCountList.add(new ReviewsCountListInfo(commentId,
					ccomment, iprice, iquality, ishipping, iusefulness,
					foverallrating, dcreatedate, istate, bName, memberPhoto,
					CommentPhotosUrl, commentVideoUrl, helpfulqty,
					nothelpfulqty, ccode));
		}
		return reviewsInCountList;
	}
}
