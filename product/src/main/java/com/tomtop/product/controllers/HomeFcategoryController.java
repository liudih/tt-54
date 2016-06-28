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
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.HomeFeaturedCategoryBo;
import com.tomtop.product.models.bo.HomeFeaturedCategorySkuBo;
import com.tomtop.product.models.bo.ProductBasePriceReviewCollectInfoBo;
import com.tomtop.product.models.dto.CategoryWebsiteWithNameDto;
import com.tomtop.product.models.vo.HomeFeaturedCategoryBannerVo;
import com.tomtop.product.models.vo.HomeFeaturedCategoryContentVo;
import com.tomtop.product.models.vo.HomeFeaturedCategoryKeyVo;
import com.tomtop.product.models.vo.HomeFeaturedCategorySkuVo;
import com.tomtop.product.models.vo.HomeFeaturedCategoryVo;
import com.tomtop.product.services.ICategoryService;
import com.tomtop.product.services.IHomeFcategoryBannerService;
import com.tomtop.product.services.IHomeFcategoryKeyService;
import com.tomtop.product.services.IHomeFcategoryService;
import com.tomtop.product.services.IHomeFcategorySkuService;
import com.tomtop.product.services.IProductBaseInfoService;

/**
 * 首页面特别类目action
 * 
 * @author liulj
 *
 */
@RestController
public class HomeFcategoryController {

	@Autowired
	private IProductBaseInfoService productBaseUtils;

	@Resource(name = "homeFcategoryService")
	private IHomeFcategoryService fcategoryService;

	@Resource(name = "homeFcategoryKeyService")
	private IHomeFcategoryKeyService fcategoryKeyService;

	@Resource(name = "homeFcategorySkuService")
	private IHomeFcategorySkuService fcategorySkuService;

	@Resource(name = "homeFcategoryBannerService")
	private IHomeFcategoryBannerService fcategoryBannerService;

	@Resource(name = "categoryService")
	private ICategoryService categoryService;

	/**
	 * 根具语言id，和客户端id获取首页特别类目
	 * 
	 * @param client
	 *            客户端: 1 TOMTOP-PC,2 TOMTOP-Mobile,3 TOMTOP-APP-IOS,4
	 *            TOMTOP-APP-Android
	 * @param language
	 *            语言 1 en
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/home/fcategory", method = RequestMethod.GET)
	public Result getListByClientLanguage(@RequestParam("client") int client,
			@RequestParam("lang") int language) {
		List<HomeFeaturedCategoryBo> bos = fcategoryService
				.getListClientLangua(client, language);
		List<Integer> categoryids = Lists.newArrayList(Lists.transform(bos,
				p -> p.getCategoryId()));
		Map<Integer, String> categorys = categoryService
				.getCategoryByCategoryIds(language, client, categoryids)
				.stream()
				.collect(
						Collectors.toMap(
								CategoryWebsiteWithNameDto::getIcategoryid,
								CategoryWebsiteWithNameDto::getCname));

		return new Result(Result.SUCCESS, Lists.transform(
				bos,
				p -> {
					HomeFeaturedCategoryVo vo = BeanUtils.mapFromClass(p,
							HomeFeaturedCategoryVo.class);
					vo.setName(categorys.get(p.getCategoryId()));
					return vo;
				}).toArray());
	}

	/**
	 * 根具布局标识，模块标识，语言，客户端获取模块内容列表
	 * 
	 * @param fcategoryid
	 *            特别类目id
	 * @param currency
	 *            贷币: USD,EUR,RUB,JPY,GBP,BRL,AUD,CNY
	 * @param client
	 *            客户端: 1 TOMTOP-PC,2 TOMTOP-Mobile,3 TOMTOP-APP-IOS,4
	 *            TOMTOP-APP-Android
	 * @param language
	 *            语言 1 en
	 * @return 返回特别类目的内容
	 */
	@RequestMapping(value = "/ic/v1/home/fcategorycontents", method = RequestMethod.GET)
	public Result getFactegoryContext(
			@RequestParam("fcategoryid") int fcategoryid,
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency) {
		HomeFeaturedCategoryContentVo vo = new HomeFeaturedCategoryContentVo();
		vo.setBanners(Lists.newArrayList(fcategoryBannerService
				.getListByFcategoryClientLangua(fcategoryid, client, lang)
				.stream()
				.map(p -> BeanUtils.mapFromClass(p,
						HomeFeaturedCategoryBannerVo.class))
				.collect(Collectors.toList())));
		vo.setKeys(Lists.newArrayList(fcategoryKeyService
				.getListByFcategoryClientLangua(fcategoryid, client, lang)
				.stream()
				.map(p -> BeanUtils.mapFromClass(p,
						HomeFeaturedCategoryKeyVo.class))
				.collect(Collectors.toList())));
		List<HomeFeaturedCategorySkuBo> skuBos = fcategorySkuService
				.getListByFcategoryClientLangua(fcategoryid, client, lang);
		List<String> listingIds = Lists
				.transform(skuBos, p -> p.getListingId());
		if (listingIds != null && listingIds.size() > 0) {
			Map<String, ProductBasePriceReviewCollectInfoBo> bos = Maps
					.uniqueIndex(productBaseUtils
							.getProductBasePriceByListings(listingIds,
									currency, client, lang), p -> p
							.getListingId());
			vo.setSkus(Lists
					.newArrayList(Lists
							.transform(
									skuBos,
									p -> {
										HomeFeaturedCategorySkuVo skuvo = new HomeFeaturedCategorySkuVo();
										skuvo.setSort(p.getSort());
										ProductBasePriceReviewCollectInfoBo bo = bos
												.get(p.getListingId());
										if (bo != null) {
											BeanUtils.copyPropertys(bo, skuvo);
										}
										return skuvo;
									})
							.stream()
							.filter(f -> StringUtils.isNotBlank(f
									.getListingId()))
							.collect(Collectors.toList())));
		}
		return new Result(Result.SUCCESS, vo);
	}
}
