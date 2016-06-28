package services.mobile.product;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.interaction.InteractionProductMemberPhotosMapper;
import mapper.order.OrderMapper;
import mapper.product.ProductBaseMapper;

import org.apache.ibatis.session.ExecutorType;
import org.mybatis.guice.transactional.Transactional;

import play.Logger;
import play.mvc.Http.MultipartFormData.FilePart;
import services.base.EmailAccountService;
import services.base.EmailTemplateService;
import services.base.utils.FileUtils;
import services.base.utils.StringUtils;
import services.interaciton.review.ProductReviewsService;
import services.interaction.ProductPhotosService;
import services.member.IMemberUpdateService;
import services.order.OrderDetailService;
import valuesobject.mobile.BaseResultType;
import base.util.mail.EmailUtil;

import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;

import dto.EmailAccount;
import dto.interaction.InteractionComment;
import dto.interaction.InteractionProductMemberPhotos;
import dto.interaction.InteractionProductMemberVideo;
import dto.order.OrderDetail;
import dto.product.ProductBase;
import email.util.EmailSpreadUtil;
import extensions.interaction.ReviewSubmitEmailModel;
import forms.mobile.AddReviewForm;

@Transactional
public class ProductReviewService {

	@Inject
	EmailAccountService emailAccountService;
	@Inject
	EmailTemplateService emailTemplateService;

	@Inject
	InteractionProductMemberPhotosMapper mapper;
	@Inject
	ProductReviewsService productReviewsService;
	@Inject
	OrderDetailService orderDetailService;
	@Inject
	ProductBaseMapper productBaseMapper;
	@Inject
	IMemberUpdateService memberUpdateService;
	@Inject
	ProductPhotosService photosService;
	@Inject
	OrderMapper orderMapper;
	@Inject
	EmailSpreadUtil emailSpread;

	public boolean sendEmailReviewSubmit(String toemail, Integer langid,
			Integer siteid) {
		Logger.debug("---------------*send Email*--------------------");

		EmailAccount emailaccount = emailAccountService.getEmailAccount(siteid);
		ReviewSubmitEmailModel reviewSubmitEmailModel = new ReviewSubmitEmailModel(
				email.model.EmailType.COMMENTS_SUCCEED.getType(), langid,
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
		return emailSpread.send(emailaccount.getCemail(), toemail, title,
				content);
	}

	/**
	 * 添加商品评论图片
	 * 
	 * @param listingId
	 * @return
	 */
	public boolean addphotos(String comment, String listingid, String csku,
			int siteid, Integer commentid, String email, List<FilePart> fileList) {

		List<InteractionProductMemberPhotos> list = Lists.newArrayList();

		for (FilePart part : fileList) {
			String contentType = part.getContentType();
			Logger.debug("contentType == " + contentType);
			InteractionProductMemberPhotos p = new InteractionProductMemberPhotos();
			p.setCmemberemail(email);
			p.setClistingid(listingid);
			p.setBfile(FileUtils.toByteArray(part.getFile()));
			p.setCcontenttype(contentType);
			p.setClabel(comment);
			p.setIcommentid(commentid);
			p.setCsku(csku);
			p.setIwebsiteid(siteid);

			p.setIauditorstatus(1);// 正式版本设置为0 TODO

			p.setDcreatedate(new Date());
			int flag = mapper.insertSelective(p);
			p.setCimageurl(controllers.mobile.member.routes.Photo.at2(
					p.getIid()).url());
			mapper.updateByPrimaryKeySelective(p);
			if (flag > 0) {
				list.add(p);
			}
		}

		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 添加商品评论、图片、视频链接
	 * 
	 * @param listingId
	 * @return
	 */

	@Transactional(executorType = ExecutorType.BATCH, rethrowExceptionsAs = Exception.class)
	public Map<String, Object> addReviewImgVideo(AddReviewForm reviewForm,
			String email, Integer siteId, Integer langId, List<FilePart> parts) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		Integer status = 1;// 正式版改为0 TODO
		String orderDetailId = productReviewsService.checkReview(email,
				reviewForm.getGid());

		if (orderDetailId == null) {
			objMap.put("re", BaseResultType.REVIEW_ADD_NOT_OREDER_ERROR_CODE);
			objMap.put("msg", BaseResultType.REVIEW_ADD_NOT_OREDER_ERROR_MSG);
			Logger.debug("addReviewImgVideo Goods can not comment");
			return objMap;
		}

		ProductBase productBase = productBaseMapper
				.getProductBaseByListingIdAndLanguage(reviewForm.getGid(),
						langId);
		if (productBase == null) {
			objMap.put("re",
					BaseResultType.REVIEW_ADD_PRODUCT_NOT_FIND_ERROR_CODE);
			objMap.put("msg",
					BaseResultType.REVIEW_ADD_PRODUCT_NOT_FIND_ERROR_MSG);
			Logger.debug("addReviewImgVideo product not find");
			return objMap;
		}
		/*
		 * MemberBase mb = new MemberBase(); if (mb.getCaccount() == null) {
		 * mb.setCaccount(reviewForm.getNickname());
		 * memberUpdateService.SaveMemberAccount(email); }
		 */

		Logger.debug("addReviewImgVideo reviewForm ================= ");
		Double foverallrating = (double) Math.round(reviewForm
				.getOverallrating());
		InteractionComment ic = new InteractionComment();
		Integer iid = productReviewsService.getMaxId();
		ic.setIid(iid);
		ic.setClistingid(reviewForm.getGid());
		ic.setCsku(productBase.getCsku());
		ic.setCmemberemail(email);
		ic.setCcomment(reviewForm.getComment());
		ic.setIprice(reviewForm.getPrice());
		ic.setIquality(reviewForm.getQuality());
		ic.setIshipping(reviewForm.getShipping());
		ic.setIusefulness(reviewForm.getUsefulness());
		ic.setFoverallrating(foverallrating);

		ic.setIstate(status);

		ic.setCplatform("tomtop");
		ic.setIwebsiteid(siteId);
		ic.setDcreatedate(new Date());
		boolean result = false;

		// 存入订单id
		if (StringUtils.notEmpty(orderDetailId)) {
			OrderDetail od = orderDetailService
					.getOrderDetailByCid(orderDetailId);
			if (od != null) {
				ic.setIorderid(od.getIorderid());
			}
			// 添加评论
			Integer rec = productReviewsService.doAddProductReview(ic);
			if (rec <= 0) {
				Logger.debug("addReviewImgVideo add review error");
			}

			// 订单详情 存入评论id
			Logger.debug("reviewId=====" + ic.getIid() + "detail cid====="
					+ orderDetailId);
			result = orderDetailService.updateDetailCommentId(ic.getIid(),
					orderDetailId);
			if (result == false) {
				Logger.debug("addReviewImgVideo update detail commentid error");
			}
		}

		// 添加视频
		if (reviewForm.getVideourl() != null
				&& !"".equals(reviewForm.getVideourl())) {
			InteractionProductMemberVideo video = new InteractionProductMemberVideo();
			video.setClistingid(reviewForm.getGid());
			video.setCsku(productBase.getCsku());
			video.setCmemberemail(email);
			video.setIcomment(ic.getIid());
			video.setCvideourl(reviewForm.getVideourl());
			video.setClabel(reviewForm.getVideotitle());

			video.setIauditorstatus(status);

			video.setIwebsiteid(siteId);
			video.setDcreatedate(new Date());
			result = productReviewsService.addVideoReview(video);

			if (result == false) {
				objMap.put("re", BaseResultType.REVIEW_ADD_VIDEO_ERROR_CODE);
				objMap.put("msg", BaseResultType.REVIEW_ADD_VIDEO_ERROR_MSG);
				Logger.debug("addReviewImgVideo add video error");
				return objMap;
			}
		}

		result = addphotos("comment", reviewForm.getGid(),
				productBase.getCsku(), siteId, ic.getIid(), email, parts);

		objMap.put("re", 1);
		objMap.put("msg", "");
		return objMap;
	}

}
