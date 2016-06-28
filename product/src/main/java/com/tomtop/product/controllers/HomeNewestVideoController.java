package com.tomtop.product.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.HomeNewestVideoBo;
import com.tomtop.product.models.bo.ProductBasePriceReviewCollectInfoBo;
import com.tomtop.product.models.vo.HomeNewestVideoVo;
import com.tomtop.product.services.IHomeNewestVideoService;
import com.tomtop.product.services.IProductBaseInfoService;

/**
 * 最新视频分享
 * 
 * @author liulj
 *
 */
@RestController
public class HomeNewestVideoController {

	@Autowired
	private IProductBaseInfoService productService;

	@Resource(name = "homeNewestVideoService")
	private IHomeNewestVideoService service;

	/**
	 * 获取最新视频
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/home/newest/video", method = RequestMethod.GET)
	public Result getListByClientLang(@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
		List<HomeNewestVideoBo> bos = service.getListByClientLang(client, lang);
		if (bos != null && bos.size() > 0) {
			List<String> listings = Lists.transform(bos, p -> p.getListingId());
			Map<String, ProductBasePriceReviewCollectInfoBo> products = Maps
					.uniqueIndex(productService.getProductBaseByListings(
							listings, lang, client), p -> p.getListingId());
			if (products != null && products.size() > 0) {
				return new Result(
						Result.SUCCESS,
						Lists.transform(
								bos,
								p -> {
									HomeNewestVideoVo vo = new HomeNewestVideoVo();
									vo.setCountry(p.getCountry());
									vo.setTitle(p.getTitle());
									vo.setVideoBy(p.getVideoBy());
									vo.setVideoUrl(p.getVideoUrl());
									ProductBasePriceReviewCollectInfoBo badge = products
											.get(p.getListingId());
									if (badge != null) {
										vo.setSkuImageUrl(badge.getImageUrl());
										vo.setSkuTitle(badge.getTitle());
										vo.setSkuUrl(badge.getUrl());
										vo.setListingId(badge.getListingId());
										vo.setSku(badge.getSku());
									}
									return vo;
								})
								.stream()
								.filter(f -> StringUtils.isNotBlank(f
										.getListingId()))
								.collect(Collectors.toList()).toArray());
			}
		}
		return new Result(Result.FAIL, null);
	}
}
