package com.tomtop.product.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.HomeDailyDealBo;
import com.tomtop.product.models.bo.ProductBasePriceInfoBo;
import com.tomtop.product.models.bo.ProductBasePriceReviewCollectInfoBo;
import com.tomtop.product.models.vo.HomeDailyDealVo;
import com.tomtop.product.services.IHomeDailyDealService;
import com.tomtop.product.services.IProductBaseInfoService;

/**
 * 每日撕推荐action
 * 
 * @author liulj
 *
 */
@RestController
public class HomeDailyDealController {

	@Resource(name = "homeDailyDealService")
	private IHomeDailyDealService service;

	@Autowired
	private IProductBaseInfoService productBaseUtils;

	/**
	 * 获取每日推荐的产品
	 * 
	 * @param startDate
	 *            开始时间
	 * @param client
	 * @param language
	 * @param currency
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/home/dailyDeal", method = RequestMethod.GET)
	public Result getListByStartDate(
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam("date") String date) {
		try{
			List<HomeDailyDealBo> bos = service.getListByStartDate(
					DateUtils.parseDate(date, "yyyy/MM/dd"), client, lang);
			if (bos != null && bos.size() > 0) {
				List<String> listings = Lists.transform(bos, p -> p.getListingId());
				if (listings != null && listings.size() > 0) {
					List<ProductBasePriceReviewCollectInfoBo> products = productBaseUtils
							.getProductBasePriceByListings(listings, currency,
									client, lang);
					if (products != null && products.size() > 0) {
						Map<String, ProductBasePriceReviewCollectInfoBo> proMap = Maps
								.uniqueIndex(products, p -> p.getListingId());
	
						return new Result(
								Result.SUCCESS,
								Lists.transform(
										bos,
										p -> {
											HomeDailyDealVo vo = new HomeDailyDealVo();
											ProductBasePriceInfoBo badge = proMap
													.get(p.getListingId());
											if (badge != null) {
												BeanUtils.copyPropertys(badge, vo);
											}
											vo.setDiscount(p.getDiscount());
											return vo;
										})
										.stream()
										.filter(f -> StringUtils.isNotBlank(f
												.getListingId()))
										.collect(Collectors.toList()).toArray());
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new Result(Result.SUCCESS, null);
	}
}
