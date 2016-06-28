package com.tomtop.product.controllers;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.services.ICategoryService;
import com.tomtop.product.services.IProductBaseInfoService;
import com.tomtop.product.services.IProductCategoryMapperService;
import com.tomtop.product.services.IProductEsDataService;

/**
 * 你可能喜欢的产品
 * 
 * @author liulj
 *
 */
@RestController
public class YouMayLikeProduct {

	@Resource(name = "categoryService")
	private ICategoryService categoryService;

	@Resource(name = "productCategoryMapperService")
	private IProductCategoryMapperService mapperService;

	@Autowired
	private IProductBaseInfoService productBaseUtils;
	
	@Autowired
	private IProductEsDataService productEsDataService;

	/**
	 * 也查看的产品
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/product/alsoViewed", method = RequestMethod.GET)
	public Result getYouAlsoViewedProucts(
			@RequestParam("categoryId") int category,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang) {
		return new Result(Result.SUCCESS, productBaseUtils
				.getSortListByCategoryId(category, 2, client, lang,
						currency));
	}

	/**
	 * 也买过的产品
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/product/alsoBought", method = RequestMethod.GET)
	public Result getYouAlsoBoughtProucts(
			@RequestParam("categoryId") int category,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang) {
		return new Result(Result.SUCCESS, productBaseUtils
				.getSortListByCategoryId(category, 1, client, lang,
						currency));
	}
	
	/**
	 * 可能喜欢的产品
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/product/youMayLike", method = RequestMethod.GET)
	public Result getYouMayLikeProucts(
			@RequestParam("listingId") String listingId,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang) {
		return new Result(Result.SUCCESS,
				productBaseUtils.getSearchProductLikeList(listingId, client,
						lang, currency));
	}
	
	/**
	 * 也查看的产品 AlsoViewed (第二版)
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/product/alsoViewed", method = RequestMethod.GET)
	public Result getYouAlsoViewedProucts2(
			@RequestParam("listingId") String listingId,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang) {
		return new Result(Result.SUCCESS, productEsDataService
				.getViewedProduct(listingId, client, lang,currency));
	}
	
	/**
	 * 也买过的产品 AlsoBought (第二版)
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/product/alsoBought", method = RequestMethod.GET)
	public Result getYouAlsoBoughtProucts2(
			@RequestParam("listingId") String listingId,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang) {
		return new Result(Result.SUCCESS, productEsDataService
				.getBouthtProduct(listingId, client, lang,currency));
	}
	
	/**
	 * 可能喜欢的产品youMayLike Recommend (第二版)
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/product/youMayLike", method = RequestMethod.GET)
	public Result getYouMayLikeProucts2(
			@RequestParam("listingId") String listingId,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang) {
		return new Result(Result.SUCCESS,
				productEsDataService.getRecommendProduct(listingId, client,lang, currency));
	}
}
