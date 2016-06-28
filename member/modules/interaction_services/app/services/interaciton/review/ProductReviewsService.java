package services.interaciton.review;

import interceptors.CacheResult;

import java.util.Comparator;
import java.util.Date;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.interaction.InteractionCommentMapper;
import mapper.interaction.InteractionProductMemberPhotosMapper;
import mapper.interaction.InteractionProductMemberVideoMapper;
import mapper.interaction.review.InteractionCommentHelpEvaluateMapper;
import mapper.interaction.review.InteractionCommentHelpQtyMapper;
import mapper.member.MemberBaseMapper;
import mapper.member.MemberEmailVerifyMapper;
import mapper.order.DetailMapper;
import mapper.product.ProductBaseMapper;
import mapper.product.ProductImageMapper;
import mapper.product.ProductUrlMapper;
import play.Logger;
import services.base.EmailAccountService;
import services.base.EmailTemplateService;
import services.base.FoundationService;
import services.base.utils.Utils;
import services.interaction.InteractionProductPhotosService;
import services.interaction.review.IProductReviewsService;
import services.member.IMemberEnquiryService;
import services.order.IOrderStatusService;
import session.ISessionService;
import valueobjects.base.Page;
import valueobjects.interaction.ReviewCountAndScore;
import base.util.mail.EmailUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import context.WebContext;
import dao.interaction.IReviewDao;
import dto.EmailAccount;
import dto.ReviewsCountList;
import dto.interaction.Foverallrating;
import dto.interaction.InteractionComment;
import dto.interaction.InteractionCommentHelpEvaluate;
import dto.interaction.InteractionCommentHelpQty;
import dto.interaction.InteractionProductMemberPhotos;
import dto.interaction.InteractionProductMemberVideo;
import dto.order.OrderDetail;
import dto.product.ProductBase;
import dto.product.ProductImage;
import email.util.EmailSpreadUtil;
import extensions.interaction.ReviewSubmitEmailModel;

public class ProductReviewsService implements IProductReviewsService {

	@Inject
	InteractionCommentMapper memberReviewsMapper;

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	ProductImageMapper productImageMapper;

	@Inject
	MemberBaseMapper memberBaseMapper;

	@Inject
	FoundationService foundation;

	@Inject
	InteractionProductMemberVideoMapper videoMapper;

	@Inject
	InteractionProductMemberPhotosMapper photoMapper;

	@Inject
	ISessionService sessionService;

	@Inject
	IOrderStatusService orderStatusService;

	@Inject
	DetailMapper detailMapper;

	@Inject
	EmailAccountService emailAccountService;

	@Inject
	EmailTemplateService emailTemplateService;

	@Inject
	MemberEmailVerifyMapper emailVerifiMapper;

	@Inject
	MemberReviewsServiece memberReviewsService;

	@Inject
	InteractionProductPhotosService interactionproductphotosservice;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	ProductUrlMapper producturlmapper;

	@Inject
	InteractionCommentHelpEvaluateMapper evaluateMapper;

	@Inject
	InteractionCommentHelpQtyMapper helpQtyMapper;

	@Inject
	IReviewDao reviewDao;
	
	@Inject
	EmailSpreadUtil emailSpread;

	final String guid = "TYPEIID";

	final String countid = "countid";

	// 保存错误
	public static final int SAVE_ERROR = -3;

	@Override
	@CacheResult("product.rating")
	public Map<String, ReviewCountAndScore> getAverageScores(
			final List<String> listingIds,
			final ReviewCountAndScore defaultIfNotFound) {
		List<ReviewCountAndScore> scores = memberReviewsMapper
				.getScoreByListingIds(Sets.newHashSet(listingIds));
		Map<String, ReviewCountAndScore> exists = Maps.uniqueIndex(scores,
				s -> s.getListingId());
		return Maps.toMap(listingIds, (String id) -> {
			ReviewCountAndScore score = exists.get(id);
			if (score != null) {
				return score;
			}
			return defaultIfNotFound;
		});
	}

	/**
	 * 获取用户名称
	 *
	 * @param userEmail
	 *            用户 邮箱
	 * @return 用户名称
	 */
	public String getUserNameByMemberEmail(String email) {
		return memberBaseMapper.getUserName(email);
	}

	/**
	 * 获取产品图片
	 * 
	 * @param listingId
	 *            广告ID
	 * @return 产品图片
	 */
	public String getProductBaseImageURL(String listingId) {
		return productImageMapper
				.getProductBaseImageForEntityMapByListingId(listingId);
	}

	/**
	 * 获取产品标题
	 * 
	 * @param listingId
	 * @return 产品标题
	 */
	public ProductBase getProductBaseByListingId(String listingId) {
		return productBaseMapper.getProductBaseByListingIdAndLanguage(
				listingId, foundation.getLanguage());
	}

	/**
	 * 进行用户对产品评论的分页查询详情
	 * 
	 * @param listingId
	 * @param page
	 * @param pageSize
	 * @return 产品评论详情
	 */
	@Override
	public Page<ReviewsCountList> getPages(String listingId, Integer page,
			Integer pageSize) {
		List<InteractionComment> reviews = Lists.newArrayList();
		int total = getReviewCount(listingId);
		reviews = memberReviewsMapper.getProductReviewsPage(listingId, page,
				pageSize);
		List<ReviewsCountList> reviewsList = getReviewsCountList(reviews);
		return new Page<ReviewsCountList>(reviewsList, total, page, pageSize);
	}

	@Override
	public List<ReviewsCountList> getReviewsCountList(
			List<InteractionComment> reviews) {
		if (null == reviews || reviews.size() < 1) {
			return Lists.newArrayList();
		}
		List<ReviewsCountList> reviewsInCountList = Lists.newArrayList();

		for (InteractionComment interactionComment : reviews) {
			Integer commentId = interactionComment.getIid();
			String listingId = interactionComment.getClistingid();
			String ccomment = interactionComment.getCcomment();
			Integer iprice = interactionComment.getIprice();
			Integer iquality = interactionComment.getIquality();
			Integer ishipping = interactionComment.getIshipping();
			Integer iusefulness = interactionComment.getIusefulness();
			Double foverallrating = interactionComment.getFoverallrating();
			Date dcreatedate = interactionComment.getDcreatedate();
			Integer istate = interactionComment.getIstate();
			String email = interactionComment.getCmemberemail();
			String title = interactionComment.getCtitle();

			String bName = Utils.getIncompleteEmail(interactionComment
					.getCmemberemail());
			List<String> CommentPhotosUrl = photoMapper
					.getCommentImgageByCommentId(commentId);
			String commentVideoUrl = videoMapper
					.getCommentVideoByCommentIdLimit1(commentId);

			Integer helpfulqty = helpQtyMapper
					.getHelpfulqtyCountByCommentid(commentId);
			Integer nothelpfulqty = helpQtyMapper
					.getNothelpfulqtyCountByCommentid(commentId);
			String ccode = null;
			reviewsInCountList.add(new ReviewsCountList(commentId, ccomment,
					iprice, iquality, ishipping, iusefulness, foverallrating,
					dcreatedate, istate, bName, CommentPhotosUrl,
					commentVideoUrl, helpfulqty, nothelpfulqty, ccode, email,
					title));
		}

		return reviewsInCountList;
	}

	/**
	 * 获取评论的平均分数
	 * 
	 * @param listingId
	 * @return 评论平均分数
	 */
	@Override
	@CacheResult("comment")
	public Double getAverageScore(String listingId) {
		Double scores = memberReviewsMapper.getScoreByListingId(listingId);
		if (scores == null) {
			scores = 0.0;
		}
		return scores;
	}

	/**
	 * 获取用户对产品评论的总次数
	 * 
	 * @param listingId
	 * @return 产品评论的总次数
	 */
	@Override
	@CacheResult("comment")
	public Integer getReviewCount(String listingId) {
		return memberReviewsMapper.getCountByListingId(listingId);
	}

	/**
	 * doAddReview
	 */
	/**
	 * @param form
	 * @return flag>0
	 */
	@Override
	public Integer doAddProductReview(InteractionComment review) {
		int flag = memberReviewsMapper.insertSelective(review);
		if (flag > 0) {
			return review.getIid();
		} else {
			return null;
		}
	}

	/**
	 * update product review
	 */
	public Integer updateProductReview(InteractionComment review) {
		int flag = memberReviewsMapper.updateByPrimaryKeySelective(review);
		if (flag > 0) {
			return review.getIid();
		} else {
			return null;
		}
	}

	public boolean addVideoReview(InteractionProductMemberVideo video) {
		return videoMapper.insertSelective(video) > 0 ? true : false;
	}

	public List<ProductImage> getImageUrlByClistingid(List<String> clistingids) {
		return productImageMapper.getImageUrlByClistingid(clistingids);
	}

	/**
	 * 产品评论单产品综合评分
	 * 
	 * @param listingId
	 * @return 遍歷出有幾種星類型，每種星類型的總數，百分比
	 * @author luoys
	 */
	@Override
	@CacheResult("comment")
	public List<Foverallrating> getFoverallratingsGroup(String listingId) {
		List<Foverallrating> list = memberReviewsMapper.countRatings(listingId);
		List<Foverallrating> ratings = Lists.newArrayList();
		List<Integer> typelist = Lists.newArrayList(5, 4, 3, 2, 1);
		List<Integer> types = Lists.newArrayList();

		Integer total = 0;
		if (null != list && list.size() > 0) {
			IntSummaryStatistics stats = list.stream()
					.mapToInt((x) -> x.getNum()).summaryStatistics();
			total = (int) stats.getSum();

			for (Foverallrating foverallrating : list) {
				Integer type = foverallrating.getType();
				// 评分为0的不显示
				if (null != type && 0 != type) {
					Integer num = foverallrating.getNum();
					Double percentage = total != 0 ? (double) (num * 1.0 / total)
							: 0.0;
					ratings.add(new Foverallrating(listingId, total, type, num,
							percentage));
				}
			}
			types = Lists.transform(list, i -> i.getType());

			for (Integer a : typelist) {
				if (!types.contains(a)) {
					ratings.add(new Foverallrating(listingId, total, a, 0, 0.0));
				}
			}
		}
		ratings.sort(new Comparator<Foverallrating>() {
			@Override
			public int compare(Foverallrating o1, Foverallrating o2) {
				return o2.getType().compareTo(o1.getType());
			}
		});
		return ratings;
	}

	/**
	 * @author luoys
	 * @date Apr.4.2015
	 * @description add review images
	 * @param file
	 * @param index
	 * @param contentType
	 */
	public boolean addphotos(
			Map<String, List<InteractionProductMemberPhotos>> map) {
		return photoMapper.batchInsert(map) > 0 ? true : false;
	}

	/**
	 * 验证此用户是否可以评论
	 * 
	 * @param email
	 * @return
	 */
	public String checkReview(String email, String listingId) {
		Integer status = orderStatusService
				.getIdByName(orderStatusService.COMPLETED);
		OrderDetail detail = detailMapper.getOrderDetailNotCommentByEmail(
				email, listingId, status);
		if (detail == null) {
			return null;
		}
		return detail.getCid();
	}

	public boolean sendEmailReviewSubmit(String toemail, WebContext context) {
		Logger.debug("---------------*send Email*--------------------");
		Integer language = foundation.getLanguage(context);
		int websiteid = foundation.getSiteID(context);
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(websiteid);
		ReviewSubmitEmailModel reviewSubmitEmailModel = new ReviewSubmitEmailModel(
				email.model.EmailType.COMMENTS_SUCCEED.getType(), language,
				toemail);
		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService
					.getEmailContent(reviewSubmitEmailModel);
			if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("title and content is null ,can not send email");
				return false;
			}
		} catch (Exception e) {
			Logger.error("can not deal with register success email content");
			e.printStackTrace();
		}
		if (emailaccount == null) {
			Logger.info("sendEmailRegSuccess  email server account null!");
			return false;
		}
		//return EmailUtil.send(title, content, emailaccount, toemail);
		return emailSpread.send(emailaccount.getCemail(), toemail, title, content);
	}

	/**
	 * 验证此用户是否可以上传图片
	 * 
	 * @param email
	 * @return
	 */
	public boolean checkUploadPhoto(String email, String listingId) {
		Integer status = orderStatusService
				.getIdByName(orderStatusService.COMPLETED);
		List<OrderDetail> detail = detailMapper.getOrderDetailsForPhotoByEmail(
				email, listingId, status);
		return detail.size() > 0;
	}

	/**
	 * @author luoys
	 * @date Apr.13.2015
	 * @param commentid
	 * @param email
	 * @description Comment on whether useful help and no help
	 * @return evalute
	 */
	public boolean checkExtist(Integer commentid, String email) {
		List<InteractionCommentHelpEvaluate> list = evaluateMapper
				.getEvaluteList(commentid, email);
		return list.size() > 0 ? true : false;
	}

	/**
	 * @author luoys
	 * @date Apr.13.2015
	 * @param commentid
	 * @param evalute
	 * @return helpQty
	 */
	public boolean addEvalute(InteractionCommentHelpEvaluate evalute) {
		boolean result = evaluateMapper.insert(evalute) > 0 ? true : false;
		if (!result) {
			return false;
		}
		int icommentid = evalute.getIcommentid();

		InteractionCommentHelpQty helpQty = helpQtyMapper
				.selectByCommentId(icommentid);

		if (helpQty == null) {
			helpQty = new InteractionCommentHelpQty();
			helpQty.setCommentid(icommentid);
			if (evalute.getHelpfulcode() == 1) {
				helpQty.setHelpfulqty(1);
				helpQty.setNothelpfulqty(0);
			} else if (evalute.getHelpfulcode() == 0) {
				helpQty.setNothelpfulqty(1);
				helpQty.setHelpfulqty(0);
			}

			return helpQtyMapper.insert(helpQty) > 0 ? true : false;
		} else {
			if (evalute.getHelpfulcode() == 1) {
				int qty = helpQty.getHelpfulqty();
				helpQty.setHelpfulqty(qty + 1);
			} else if (evalute.getHelpfulcode() == 0) {
				int qty = helpQty.getNothelpfulqty();
				helpQty.setNothelpfulqty(qty);
			}

		}
		return helpQtyMapper.updateByPrimaryKey(helpQty) > 0 ? true : false;
	}

	public List<InteractionComment> getReviews(Integer page, Integer pageSize,
			String sku, Integer status, String email, Date startDate,
			Date endDate, String content, Integer siteId) {
		return reviewDao.getReviewsByPageAndListingIdAndStatus(page, pageSize,
				sku, status, email, startDate, endDate, content, siteId);
	}

	@Override
	public List<InteractionComment> getReviews(Integer page, Integer pageSize,
			String sku, Integer status, String email, Date startDate,
			Date endDate, String content, WebContext context) {
		Integer siteId = foundation.getSiteID(context);
		return this.getReviews(page, pageSize, sku, status, email, startDate,
				endDate, content, siteId);
	}

	public Integer getReviewsCount(String sku, Integer status, String email,
			Date startDate, Date endDate, String content, Integer siteId) {
		return reviewDao.getReviewsCount(sku, status, email, startDate,
				endDate, content, siteId);
	}

	@Override
	public Integer getReviewsCount(String sku, Integer status, String email,
			Date startDate, Date endDate, String content, WebContext context) {
		Integer siteId = foundation.getSiteID(context);
		return this.getReviewsCount(sku, status, email, startDate, endDate,
				content, siteId);
	}

	@Override
	public InteractionComment getReviewById(Integer id) {
		return reviewDao.getReviewById(id);
	}

	@Override
	public Boolean updateReview(InteractionComment review) {
		return reviewDao.updateReviewStatus(review);
	}

	@Override
	@CacheResult("comment")
	public List<InteractionComment> getInteractionCommentsByListingId(
			String listingid) {
		return reviewDao.getInteractionCommentsByListingId(listingid);
	}

	public boolean batchVerify(List<Integer> idList, Integer status) {
		int result = reviewDao.batchVerify(idList, status);
		return result > 0 ? true : false;
	}

	public List<InteractionComment> getReviewsByIds(List<Integer> ids) {
		return memberReviewsMapper.getReviewsByIds(ids);
	}

	public Page<InteractionProductMemberVideo> getVideoPage(Integer page,
			Integer pageSize, int status) {
		int pageIndex = (page - 1) * pageSize;
		List<InteractionProductMemberVideo> list = videoMapper.getVideoPage(
				pageIndex, pageSize, status);
		int count = videoMapper.getVideoCount(status);
		return new Page<InteractionProductMemberVideo>(list, count, page,
				pageSize);
	}

	public boolean verifyVideo(Integer iid, Integer istatus, String verifyUser) {
		InteractionProductMemberVideo v = videoMapper.selectByPrimaryKey(iid);
		if (v != null) {
			v.setIauditorstatus(istatus);
			v.setDauditordate(new Date());
			v.setCauditoruser(verifyUser);
			int flag = videoMapper.updateByPrimaryKeySelective(v);
			if (flag > 0) {
				return true;
			}
		}
		return false;
	}

	public Page<InteractionProductMemberPhotos> getPhotoPage(Integer page,
			Integer pageSize, int status) {
		int pageIndex = (page - 1) * pageSize;
		List<InteractionProductMemberPhotos> list = photoMapper.getPhotoPage(
				pageIndex, pageSize, status);
		int count = photoMapper.getPhotoCount(status);
		return new Page<InteractionProductMemberPhotos>(list, count, page,
				pageSize);
	}

	public boolean verifyPhoto(Integer iid, Integer istatus, String verifyUser) {
		InteractionProductMemberPhotos v = photoMapper.selectByPrimaryKey(iid);
		if (v != null) {
			v.setIauditorstatus(istatus);
			v.setDauditordate(new Date());
			v.setCauditoruser(verifyUser);
			int flag = photoMapper.updateByPrimaryKeySelective(v);
			if (flag > 0) {
				return true;
			}
		}
		return false;
	}

	public Integer getMaxId() {
		return memberReviewsMapper.getReviewMaxId();
	}

}
