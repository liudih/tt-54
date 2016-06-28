package com.tomtop.product.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.product.mappers.mysql.HomeNewestMapper;
import com.tomtop.product.models.dto.HomeNewestDto;
import com.tomtop.product.models.vo.HomeNewestBaseVo;
import com.tomtop.product.models.vo.HomeNewestImageVo;
import com.tomtop.product.models.vo.HomeNewestReviewVo;
import com.tomtop.product.models.vo.HomeNewestVideoVo;
import com.tomtop.product.models.vo.HomeNewestVo;
import com.tomtop.product.services.IHomeNewestService;
import com.tomtop.product.services.ISearchProductService;
import com.tomtop.search.entiry.IndexEntity;

@Service
public class HomeNewestServiceImpl implements IHomeNewestService {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeNewestServiceImpl.class);
	
	@Autowired
	HomeNewestMapper homeNewestMapper;
	@Autowired
    ISearchProductService service;
	
	private final static String IMAGE_TYPE = "image";
	private final static String REVIEW_TYPE = "review";
	private final static String VIDEO_TYPE = "video";
	
	/**
	 * 获取首页 Customers Voices
	 * @return HomeNewestVo
	 */
	@Cacheable(value="customers_voices", keyGenerator = "customKeyGenerator")
	@Override
	public HomeNewestVo getCustomersVoices(Integer client, Integer lang) {
		try{
			List<HomeNewestDto> hndto = homeNewestMapper.getHomeNewest(client, lang);
			if(hndto == null || hndto.size() == 0){
				logger.info("Home Newest is null");
				return null;
			}
			Map<String,HomeNewestBaseVo> hnbvoMap = new HashMap<String, HomeNewestBaseVo>();
			String listingId = "";
			List<String> idList = new ArrayList<String>();
			hndto.forEach(ids ->{
				idList.add(ids.getListingId());
			});
			List<IndexEntity> ieList = service.getSearchProductList(JSON.toJSONString(idList), lang, 1);
			IndexEntity ie = null;
			HomeNewestBaseVo hnbvo = null;
			logger.info("find index entity soru");
			if(ieList != null && ieList.size() > 0){
				for (int i = 0; i < ieList.size(); i++) {
					ie = ieList.get(i);
					if(ie == null){
						logger.info("index entity is null");
					}else{
						listingId = ie.getListingId();
						hnbvo = new HomeNewestBaseVo();
						if (ie.getMutil() != null) {
							if(ie.getMutil().getUrl() != null){
								hnbvo.setSkuUrl(ie.getMutil().getUrl().get(0));
							}
							hnbvo.setSkuTitle(ie.getMutil().getTitle());
						}
						hnbvo.setSkuImageUrl(ie.getDefaultImgUrl());
						hnbvoMap.put(listingId, hnbvo);
					}
				}

				List<HomeNewestImageVo> image = new ArrayList<HomeNewestImageVo>();
				List<HomeNewestReviewVo> review = new ArrayList<HomeNewestReviewVo>();
				List<HomeNewestVideoVo> video = new ArrayList<HomeNewestVideoVo>();
				HomeNewestImageVo imageVo = null;
				HomeNewestReviewVo reviewVo = null;
				HomeNewestVideoVo videoVo = null;
				HomeNewestDto hn = null;
				String type = "";
				for (int i = 0; i < hndto.size(); i++) {
					hn = hndto.get(i);
					if(hn == null){
						logger.info("Home Newest Dto is null");
					}else{
						type = hn.getType();
						listingId = hn.getListingId();
						hnbvo = hnbvoMap.get(listingId);
						if(type.equals(IMAGE_TYPE)){
							imageVo = new HomeNewestImageVo();
							imageVo.setImgUrl(hn.getContent());
							imageVo.setTitle(hn.getTitle());
							imageVo.setListingId(listingId);
							imageVo.setSku(hn.getSku());
							imageVo.setImgBy(hn.getUserBy());
							imageVo.setCountry(hn.getCountry());
							imageVo.setSkuTitle(hnbvo.getSkuTitle());
							imageVo.setSkuUrl(hnbvo.getSkuUrl());
							imageVo.setSkuImageUrl(hnbvo.getSkuImageUrl());
							image.add(imageVo);
						}else if(type.equals(REVIEW_TYPE)){
							reviewVo = new HomeNewestReviewVo();
							reviewVo.setReviewContent(hn.getContent());
							reviewVo.setTitle(hn.getTitle());
							reviewVo.setListingId(listingId);
							reviewVo.setSku(hn.getSku());
							reviewVo.setReviewBy(hn.getUserBy());
							reviewVo.setCountry(hn.getCountry());
							reviewVo.setSkuTitle(hnbvo.getSkuTitle());
							reviewVo.setSkuUrl(hnbvo.getSkuUrl());
							reviewVo.setSkuImageUrl(hnbvo.getSkuImageUrl());
							review.add(reviewVo);
						}else if(type.equals(VIDEO_TYPE)){
							videoVo = new HomeNewestVideoVo();
							videoVo.setVideoUrl(hn.getContent());
							videoVo.setTitle(hn.getTitle());
							videoVo.setListingId(listingId);
							videoVo.setSku(hn.getSku());
							videoVo.setVideoBy(hn.getUserBy());
							videoVo.setCountry(hn.getCountry());
							videoVo.setSkuTitle(hnbvo.getSkuTitle());
							videoVo.setSkuUrl(hnbvo.getSkuUrl());
							videoVo.setSkuImageUrl(hnbvo.getSkuImageUrl());
							video.add(videoVo);
						}
					}
				}
				HomeNewestVo hnVo = new HomeNewestVo();
				hnVo.setImage(image);
				hnVo.setReview(review);
				hnVo.setVideo(video);
				
				return hnVo;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
