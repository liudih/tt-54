package services.mobile.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapper.interaction.InteractionCommentMapper;
import mapper.interaction.InteractionProductMemberPhotosMapper;
import mapper.interaction.InteractionProductMemberVideoMapper;
import mapper.interaction.review.InteractionCommentHelpQtyMapper;
import services.base.utils.Utils;
import services.interaciton.review.ProductReviewsService;
import utils.ValidataUtils;
import valueobjects.base.Page;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dto.interaction.Foverallrating;
import dto.interaction.InteractionComment;
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
	ProductReviewsService productReviewsService;

	@Inject
	InteractionCommentMapper memberReviewsMapper;

	@Inject
	InteractionProductMemberVideoMapper videoMapper;

	@Inject
	InteractionProductMemberPhotosMapper photoMapper;

	@Inject
	InteractionCommentHelpQtyMapper helpQtyMapper;

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
				map.put("imgurls",
						review.getCommentPhotosUrl() == null ? new ArrayList<String>()
								: review.getCommentPhotosUrl());
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
		List<InteractionComment> reviews = Lists.newArrayList();
		int total = productReviewsService.getReviewCount(listingId);
		page = page < 1 ? 1 : page;
		pageSize = pageSize < 0 ? 6 : pageSize;
		reviews = memberReviewsMapper.getProductReviewsPage(listingId, page,
				pageSize);
		List<ReviewsCountListInfo> reviewsList = getReviewsCountList(reviews);
		return new Page<ReviewsCountListInfo>(reviewsList, total, page,
				pageSize);
	}

	public List<ReviewsCountListInfo> getReviewsCountList(
			List<InteractionComment> reviews) {
		if (null == reviews || reviews.size() < 1) {
			return Lists.newArrayList();
		}
		List<ReviewsCountListInfo> reviewsInCountList = Lists.newArrayList();
		for (InteractionComment interactionComment : reviews) {
			Integer commentId = interactionComment.getIid();
			// String listingId = interactionComment.getClistingid();
			String ccomment = interactionComment.getCcomment();
			Integer iprice = interactionComment.getIprice();
			Integer iquality = interactionComment.getIquality();
			Integer ishipping = interactionComment.getIshipping();
			Integer iusefulness = interactionComment.getIusefulness();
			Double foverallrating = interactionComment.getFoverallrating();
			Long dcreatedate = interactionComment.getDcreatedate().getTime();
			Integer istate = interactionComment.getIstate();
			String bName = Utils.getIncompleteEmail(interactionComment
					.getCmemberemail());
			String memberPhoto = controllers.mobile.member.routes.Photo.at(
					interactionComment.getCmemberemail()).url()
					+ "?email=" + interactionComment.getCmemberemail();
			List<String> CommentPhotosUrl = photoMapper
					.getCommentImgageByCommentId(commentId);
			String commentVideoUrl = videoMapper
					.getCommentVideoByCommentIdLimit1(commentId);
			Integer helpfulqty = helpQtyMapper
					.getHelpfulqtyCountByCommentid(commentId);
			Integer nothelpfulqty = helpQtyMapper
					.getNothelpfulqtyCountByCommentid(commentId);
			String ccode = null;
			reviewsInCountList.add(new ReviewsCountListInfo(commentId,
					ccomment, iprice, iquality, ishipping, iusefulness,
					foverallrating, dcreatedate, istate, bName, memberPhoto,
					CommentPhotosUrl, commentVideoUrl, helpfulqty,
					nothelpfulqty, ccode));
		}
		return reviewsInCountList;
	}
}
