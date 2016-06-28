package com.tomtop.product.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.HomeRecentOrdersCountryBo;
import com.tomtop.product.models.bo.HomeRecentOrdersSkuBo;
import com.tomtop.product.models.bo.ProductBasePriceReviewCollectInfoBo;
import com.tomtop.product.models.vo.HomeRecentOrdersVo;
import com.tomtop.product.services.IHomeRecentOrdersCountryService;
import com.tomtop.product.services.IHomeRecentOrdersSkuService;
import com.tomtop.product.services.IProductBaseInfoService;

/**
 * 获取首页最近订单
 * 
 * @author liulj
 * 
 */
@RestController
public class HomeRecentOrdersController {

	@Autowired
	private IProductBaseInfoService productService;

	@Resource(name = "homeRecentOrdersSkuService")
	private IHomeRecentOrdersSkuService skuService;

	@Resource(name = "homeRecentOrdersCountryService")
	private IHomeRecentOrdersCountryService countryService;

	/**
	 * 获取首页最近订单接口
	 * 
	 * @param client
	 * @param lang
	 * @param currency
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/home/recent_orders", method = RequestMethod.GET)
	public Result getInfo(@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency) {
		List<HomeRecentOrdersSkuBo> bos = skuService.getListByClientLang(
				client, lang);
		if (bos != null && bos.size() > 0) {
			List<String> listings = Lists.transform(bos, p -> p.getListingId());
			List<ProductBasePriceReviewCollectInfoBo> products = productService
					.getProductBasePriceByListings(listings, currency, client,
							lang);
			List<HomeRecentOrdersCountryBo> countryBos = countryService
					.getCountryNameListByClientLang(client, lang);
			if (countryBos != null && countryBos.size() > 0 && products != null
					&& products.size() > 0) {
				return new Result(Result.SUCCESS, products
						.stream()
						.map(e -> {
							HomeRecentOrdersVo vo = BeanUtils.mapFromClass(e,
									HomeRecentOrdersVo.class);
							vo.setCountry(countryBos.get(
									RandomUtils.nextInt(countryBos.size()))
									.getName());
							return vo;
						}).collect(Collectors.toList()).toArray());
			}
		}
		return new Result(Result.FAIL, null);
	}
}
