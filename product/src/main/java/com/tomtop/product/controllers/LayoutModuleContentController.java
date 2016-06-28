package com.tomtop.product.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.BaseLayoutmoduleContentBo;
import com.tomtop.product.models.bo.ProductBasePriceReviewCollectInfoBo;
import com.tomtop.product.models.vo.BaseLayoutmoduleContenthProductVo;
import com.tomtop.product.services.ILayoutService;
import com.tomtop.product.services.ILayoutmoduleContentService;
import com.tomtop.product.services.IProductBaseInfoService;

/**
 * 布局模块内容action
 * 
 * @author liulj
 *
 */
@RestController
public class LayoutModuleContentController {

	@Autowired
	private ILayoutmoduleContentService contentService;

	@Autowired
	private IProductBaseInfoService productBaseUtils;
	
	@Autowired
	ILayoutService layoutService;

	/**
	 * 根具布局标识，模块标识，语言，客户端获取模块内容列表
	 * 
	 * @param layoutcode
	 *            布局标识 HOME 主页
	 * @param modulecode
	 *            模块标识 HOME下的模块标识:TOP-SELLERS， NEW-ARRIVALS，
	 *            MOREITEM-TO-CONSIDER
	 * 
	 *            FEATURED-CATEGORIES
	 * @param currency
	 *            贷币: USD,EUR,RUB,JPY,GBP,BRL,AUD,CNY
	 * @param client
	 *            客户端: 1 TOMTOP-PC,2 TOMTOP-Mobile,3 TOMTOP-APP-IOS,4
	 *            TOMTOP-APP-Android
	 * @param language
	 *            语言 1 en
	 * @return 返回结果对象，包括了所有sku的信息内容
	 */
	@RequestMapping(value = "/ic/v1/layout/module/contents", method = RequestMethod.GET)
	public Result getListByLayoutModuleClinetLanguage(
			@RequestParam("layoutcode") String layoutcode,
			@RequestParam("modulecode") String modulecode,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
		List<BaseLayoutmoduleContentBo> contents = contentService
				.getListByLayoutModuleClinetLanguage(layoutcode, modulecode,
						client, lang);
		if (contents != null && contents.size() > 0) {
			List<String> listingIds = contents.stream()
					.map(BaseLayoutmoduleContentBo::getListingId)
					.collect(Collectors.toList());
			if (listingIds != null && listingIds.size() > 0) {
				Map<String, ProductBasePriceReviewCollectInfoBo> skuMap = Maps
						.uniqueIndex(
								productBaseUtils
										.getProductBasePriceReviewByListings(
												listingIds, currency, client,
												lang), p -> p
										.getListingId());
				List<BaseLayoutmoduleContenthProductVo> contenthVos = contents
						.stream()
						.map(p -> {
							BaseLayoutmoduleContenthProductVo vo = new BaseLayoutmoduleContenthProductVo();
							vo.setSort(p.getSort());
							ProductBasePriceReviewCollectInfoBo bo = skuMap
									.get(p.getListingId());
							if (bo != null) {
								BeanUtils.copyPropertys(bo, vo);
							}
							return vo;
						})
						.filter(f -> StringUtils.isNotBlank(f.getListingId()))
						.collect(Collectors.toList());
				return new Result(Result.SUCCESS, contenthVos.toArray());
			}
		}
		return new Result(Result.SUCCESS, null);
	}
	
	@RequestMapping(value = "/ic/v2/layout/module/contents", method = RequestMethod.GET)
	public Result getListByLayoutModule2(
			@RequestParam(value="layoutcode",required = false, defaultValue = "HOME") String layoutcode,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang) {
		HashMap<String,List<BaseLayoutmoduleContenthProductVo>> layoutMap = layoutService.getBaseLayoutmoduleContenth(lang, client,layoutcode, currency);
		
		if(layoutMap == null || layoutMap.size() == 0){
			return new Result(Result.FAIL, "layout not find");
		}
		
		return new Result(Result.SUCCESS, layoutMap);
	}
}
