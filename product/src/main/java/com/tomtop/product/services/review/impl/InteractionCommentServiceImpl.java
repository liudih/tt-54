package com.tomtop.product.services.review.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.interaction.mappers.ProductReviewsMapper;
import com.tomtop.product.dao.IProductReviewsDao;
import com.tomtop.product.models.bo.ProductReviewBo;
import com.tomtop.product.models.bo.ReviewStartNumBo;
import com.tomtop.product.models.bo.StartNum;
import com.tomtop.product.models.dto.ReviewCountAndScoreDto;
import com.tomtop.product.models.dto.ReviewTotalStartDto;
import com.tomtop.product.models.dto.review.InteractionCommentDto;
import com.tomtop.product.models.dto.review.ReviewPhotoVideoDto;
import com.tomtop.product.services.review.IInteractionCommentService;
import com.tomtop.product.utils.DateFormatUtils;

@Service
public class InteractionCommentServiceImpl implements
		IInteractionCommentService {

	@Autowired
	IProductReviewsDao productReviewsDao;
	
	@Autowired
	ProductReviewsMapper productReviewsMapper;
	
	private final static int state = 1;
	private final static int limit = 5;
	
	/**
	 * 获取商品评论星级和数量
	 * 
	 * @param listingId
	 * 
	 * 
	 * @return ReviewStartNumBo
	 * @author renyy
	 */
	@Override
	public ReviewStartNumBo getReviewStartNumBoById(String listingId) {
		ReviewStartNumBo rsnbo = new ReviewStartNumBo();
		if(listingId == null || "".equals(listingId.trim())){
			rsnbo.setRes(-50001);
			rsnbo.setMsg("listingid is null");
			return rsnbo;
		}
		ReviewCountAndScoreDto rc = productReviewsDao.getScoreByListingId(listingId, 1);
		Integer reviewCount = 0;
		if (rc == null) {
			rsnbo.setCount(reviewCount);
			rsnbo.setStart(0.00);
		}else{
			reviewCount = rc.getReviewCount();
			rsnbo.setCount(reviewCount);
			rsnbo.setStart(rc.getAvgScore());
			List<ReviewTotalStartDto> rtsdtoList = productReviewsDao.getFoverallratingStartNumByListingId(listingId, 1);
			if(rtsdtoList != null && rtsdtoList.size() > 0){
				List<StartNum> startPer = new ArrayList<StartNum>();
				ReviewTotalStartDto trs = null;
				StartNum sn = null;
				Integer totalPre = 0;//总百分比
				Integer start = 0;
				Integer num = 0;
				Integer pre = 0;
				double nr = 0d;
				for (int i = 0; i < rtsdtoList.size(); i++) {
					trs = rtsdtoList.get(i);
					start = trs.getStartNum();
					num = trs.getNum();
					if(start != 1){
						nr = (double) num / (double) reviewCount * 100;
						pre = (int) Math.rint(nr);
						sn = new StartNum();
						sn.setStartNum(start);
						sn.setPtage(pre);
						startPer.add(sn);
						totalPre += pre;
					}
				}
				pre = 100 - totalPre;
				if(pre != 0){
					sn = new StartNum();
					sn.setStartNum(0);
					sn.setPtage(pre);//星级对应的百分比
					startPer.add(sn);
				}
				rsnbo.setStartNum(startPer);
			}
		}
		rsnbo.setRes(1);
		return rsnbo;
	}
	
	/**
	 * 获取商品评论详情
	 * 
	 * @param listingId
	 * 
	 * @param siteId
	 * 
	 * @return List<ProductReviewBo>
	 * @author renyy
	 */
	@Cacheable(value = "product_review", keyGenerator = "customKeyGenerator")
	@Override
	public List<ProductReviewBo> getProductReviewBoList(String listingId,
			Integer siteId) {
		List<ProductReviewBo> prboList = new ArrayList<ProductReviewBo>();
		List<InteractionCommentDto> icList = productReviewsMapper.getInteractionCommentDtoByListingId(listingId, state, siteId, limit);
		Map<Integer, ProductReviewBo> prMap = new HashMap<Integer, ProductReviewBo>();
		if(icList != null && icList.size() > 0){
			List<Integer> commentIds = new ArrayList<Integer>();
			ProductReviewBo prbo = null;
			InteractionCommentDto icd = null;
			Integer id = 0;
			String email = null;
			Double overall = 0d;
			Integer usefulness = 0;
			Integer shipping = 0;
			Integer price = 0;
			Integer quality = 0;
			Date commentDate = null;
			String comment = null;
			for (int i = 0; i < icList.size(); i++) {
				icd = icList.get(i);
				id = icd.getIid();
				email = icd.getCmemberemail();
				overall = icd.getFoverallrating();
				usefulness = icd.getIusefulness();
				shipping = icd.getIshipping();
				price = icd.getIprice();
				quality = icd.getIquality();
				commentDate = icd.getDcreatedate();
				comment = icd.getCcomment();
				prbo = new ProductReviewBo();
				prbo.setEmail(email);
				prbo.setOverall(overall);
				prbo.setUsefulness(usefulness);
				prbo.setShipping(shipping);
				prbo.setPrice(price);
				prbo.setQuality(quality);
				prbo.setCommentDate(DateFormatUtils.getDate(commentDate, "YYYY-MM-DD hh:mm:ss"));
				prbo.setComment(comment);
				prMap.put(id, prbo);
				commentIds.add(id);
			}
			String url = null;
			String type = null;
			List<String> imgUrls = null;
			List<String> videos = null;
			List<ReviewPhotoVideoDto> rpvdtoList = productReviewsMapper.getReviewPhotoVideoDtoByCommentId(state, siteId, commentIds);
			if(rpvdtoList != null  && rpvdtoList.size() > 0){
				ReviewPhotoVideoDto rpvdto = null;
				for (int i = 0; i < rpvdtoList.size(); i++) {
					rpvdto = rpvdtoList.get(i);
					id = rpvdto.getCommentid();
					type = rpvdto.getCode();
					url = rpvdto.getUrl();
					prbo = prMap.get(id);
					if("photo".equals(type)){
						imgUrls = prbo.getImgUrls();
						if(imgUrls == null){
							imgUrls = new ArrayList<String>();
						}
						imgUrls.add(url);
						prbo.setImgUrls(imgUrls);
						prMap.put(id, prbo);
					}
					if("video".equals(type)){
						videos = prbo.getVideos();
						if(videos == null){
							videos = new ArrayList<String>();
						}
						videos.add(url);
						prbo.setVideos(videos);
						prMap.put(id, prbo);
					}
				}
			}
		}
		if(prMap.size() > 0){
			ProductReviewBo prb = null;
			Iterator iter = prMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next(); 
				prb = (ProductReviewBo) entry.getValue();
				prboList.add(prb);
			}
		}
		return prboList;
	}

}
