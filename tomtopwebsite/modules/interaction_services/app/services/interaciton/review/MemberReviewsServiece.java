package services.interaciton.review;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import mapper.interaction.InteractionCommentMapper;
import mapper.interaction.InteractionProductMemberPhotosMapper;
import mapper.interaction.InteractionProductMemberVideoMapper;
import mapper.product.ProductImageMapper;
import services.base.FoundationService;
import services.interaction.InteractionProductPhotosService;
import services.interaction.review.IMemberReviewsServiece;
import valueobjects.base.Page;
import valueobjects.interaction.ReviewsInMemberCenter;

import com.google.common.collect.Lists;

import context.WebContext;
import dao.product.IProductUrlEnquiryDao;
import dto.interaction.InteractionComment;
import dto.product.ProductUrl;

public class MemberReviewsServiece implements IMemberReviewsServiece {

	@Inject
	InteractionCommentMapper memberReviewsMapper;

	@Inject
	ProductImageMapper productImageMapper;

	@Inject
	InteractionProductPhotosService interactionproductphotosservice;

	@Inject
	InteractionProductMemberPhotosMapper productMemberPhotosMapper;

	@Inject
	InteractionProductMemberVideoMapper interactionproductmembervideomapper;

	@Inject
	FoundationService foundationservice;

	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;

	public List<ReviewsInMemberCenter> getReviewsInMemberCenterList(
			List<InteractionComment> reviews) {
		if (null == reviews || reviews.size() < 1) {
			return Lists.newArrayList();
		}
		List<ReviewsInMemberCenter> reviewsInMemberCenterList = Lists
				.newArrayList();
		for (InteractionComment interactionComment : reviews) {
			String listingId = interactionComment.getClistingid();
			Integer commentId = interactionComment.getIid();
			String ccomment = interactionComment.getCcomment();
			Integer iprice = interactionComment.getIprice();
			Integer iquality = interactionComment.getIquality();
			Integer ishipping = interactionComment.getIshipping();
			Integer iusefulness = interactionComment.getIusefulness();
			Double foverallrating = interactionComment.getFoverallrating();
			Date dcreatedate = interactionComment.getDcreatedate();
			Integer istate = interactionComment.getIstate();
			String productUrl = "";
			ProductUrl url = this.productUrlEnquityDao
					.getProductUrlsByListingId(listingId,
							foundationservice.getLanguage());
			if (null != url) {
				productUrl = url.getCurl();
			}
			String productSmallImgUrl = productImageMapper
					.getProductSmallImageForMemberViewsByListingId(listingId);
			List<String> CommentPhotosUrl = productMemberPhotosMapper
					.getCommentImgageByCommentIdLimit5(commentId);
			String commentVideoUrl = interactionproductmembervideomapper
					.getCommentVideoByCommentIdLimit1(commentId);
			reviewsInMemberCenterList.add(new ReviewsInMemberCenter(commentId,
					ccomment, iprice, iquality, ishipping, iusefulness,
					foverallrating, dcreatedate, istate, productSmallImgUrl,
					CommentPhotosUrl, commentVideoUrl, productUrl));
		}
		return reviewsInMemberCenterList;
	}

	public Integer getTotalReviewsCountByMemberEmailAndSiteId(String email,
			Integer siteId) {
		return memberReviewsMapper.getTotalReviewsCountByMemberEmailAndSiteId(
				email, siteId);
	}

	@Override
	public Integer getTotalReviewsCountByMemberEmailAndSiteId(String email,
			WebContext context) {
		Integer siteId = foundationservice.getSiteID(context);
		return this.getTotalReviewsCountByMemberEmailAndSiteId(email, siteId);
	}

	public Integer getPendingReviewsCountByMemberEmailAndSiteId(String email,
			Integer siteId) {
		return memberReviewsMapper
				.getPendingReviewsCountByMemberEmailAndSiteId(email, siteId);
	}

	@Override
	public Integer getPendingReviewsCountByMemberEmailAndSiteId(String email,
			WebContext context) {
		Integer siteId = foundationservice.getSiteID(context);
		return this.getPendingReviewsCountByMemberEmailAndSiteId(email, siteId);
	}

	public Integer getApprovedReviewsCountByMemberEmailAndSiteId(String email,
			Integer siteId) {
		return memberReviewsMapper
				.getApprovedReviewsCountByMemberEmailAndSiteId(email, siteId);
	}

	@Override
	public Integer getApprovedReviewsCountByMemberEmailAndSiteId(String email,
			WebContext context) {
		Integer siteId = foundationservice.getSiteID(context);
		return this
				.getApprovedReviewsCountByMemberEmailAndSiteId(email, siteId);
	}

	public Integer getFailedReviewsCountByMemberEmailAndSiteId(String email,
			Integer siteId) {
		return memberReviewsMapper.getFailedReviewsCountByMemberEmailAndSiteId(
				email, siteId);
	}

	@Override
	public Integer getFailedReviewsCountByMemberEmailAndSiteId(String email,
			WebContext context) {
		Integer siteId = foundationservice.getSiteID(context);
		return this.getFailedReviewsCountByMemberEmailAndSiteId(email, siteId);
	}

	public Page<ReviewsInMemberCenter> getMyReviewsPageByEmail(String email,
			Integer total, Integer pageIndex, Integer pageSize, Integer status,
			Integer dateType, Integer siteId) {
		List<InteractionComment> reviews = Lists.newArrayList();
		Date start = null;
		Date end = null;
		if (dateType != 0) {
			end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (dateType * (-1)));
			start = calendar.getTime();
		}
		reviews = memberReviewsMapper.getMyReviewsPageByEmail(email, pageIndex,
				pageSize, status, start, end, siteId);

		List<ReviewsInMemberCenter> reviewsInMemberCenters = getReviewsInMemberCenterList(reviews);
		return new Page<ReviewsInMemberCenter>(reviewsInMemberCenters, total,
				pageIndex, pageSize);
	}

	@Override
	public Page<ReviewsInMemberCenter> getMyReviewsPageByEmail(String email,
			Integer total, Integer pageIndex, Integer pageSize, Integer status,
			Integer dateType, WebContext context) {
		Integer siteId = foundationservice.getSiteID(context);
		return this.getMyReviewsPageByEmail(email, total, pageIndex, pageSize,
				status, dateType, siteId);
	}

}
