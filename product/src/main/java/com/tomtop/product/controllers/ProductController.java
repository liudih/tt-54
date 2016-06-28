package com.tomtop.product.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.ProductBasePriceInfoBo;
import com.tomtop.product.models.dto.CategoryWebsiteWithNameDto;
import com.tomtop.product.models.vo.CategoryProductListVo;
import com.tomtop.product.models.vo.ProductBasePriceInfoVo;
import com.tomtop.product.models.vo.ProductBasePriceReviewInfoVo;
import com.tomtop.product.services.ICategoryService;
import com.tomtop.product.services.IProductBaseInfoService;
import com.tomtop.product.services.IProductCategoryMapperService;
import com.tomtop.product.utils.ProductBaseUtils;

/**
 *************************************************************** 
 * <p>
 * 
 * @DESCRIPTION : 商品相关的接口 供第三方系统统一调用
 * @AUTHOR : 文龙 13715116671
 * @DATE :2015-11-6 上午11:15:33
 *       </p>
 **************************************************************** 
 */
@RestController
public class ProductController {

	@Autowired
	private IProductBaseInfoService productBaseUtils;

	@Autowired
	private ProductBaseUtils baseUtils;

	@Autowired
	ICategoryService categoryService;

	@Autowired
	private IProductCategoryMapperService mapperService;

	/**
	 * 根具商品id获，商品的信息 (参数有,语言ID，站点ID获取某个商品信息)
	 * 
	 * @param listingId
	 * @param languageid
	 *            语言ID， 默认为1(英文)
	 * @param websiteid
	 *            站点ID 默认为1(www.tomtopwebsite.com)
	 * @return
	 */
	// @Cacheable(value = "product", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/products/{listingId}", method = RequestMethod.GET)
	public Result get(@PathVariable(value = "listingId") String listingId,
			@RequestParam(value = "lang") int languageid,
			@RequestParam(value = "client") int websiteid,
			@RequestParam(value = "currency") String currency) {
		if (StringUtils.isNotBlank(listingId)
				&& StringUtils.isNotBlank(currency)) {
			ProductBasePriceInfoBo bo = productBaseUtils
					.getProductBasePriceByListing(listingId, currency,
							websiteid, languageid);
			if (bo != null) {
				return new Result(Result.SUCCESS, BeanUtils.mapFromClass(bo,
						ProductBasePriceInfoVo.class));
			}
		}
		return new Result(Result.FAIL, null);
	}

	/**
	 * 根具商品id列表获取，商品的信息 (参数有,语言ID，站点ID获取某个商品信息)
	 * 
	 * @param listingIds
	 *            商品id列表，值以,隔开，如：1,2,3
	 * @param languageid
	 *            语言ID， 默认为1(英文)
	 * @param websiteid
	 *            站点ID 默认为1(www.tomtopwebsite.com)
	 * @return
	 */
	// @Cacheable(value = "product", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/products", method = RequestMethod.GET)
	public Result getListByListingIds(
			@RequestParam(value = "listingIds") List<String> listingIds,
			@RequestParam(value = "lang") int lang,
			@RequestParam(value = "client") int client,
			@RequestParam(value = "currency") String currency) {
		if (listingIds != null && listingIds.size() > 0) {
			return new Result(Result.SUCCESS, Lists.transform(
					productBaseUtils.getProductBasePriceReviewByListings(
							listingIds, currency, client, lang),
					p -> BeanUtils.mapFromClass(p,
							ProductBasePriceReviewInfoVo.class)).toArray());
		}
		return new Result(Result.FAIL, null);
	}

	/**
	 * 获取产品根具类目
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @param categoryId
	 * @param lang
	 * @param client
	 * @param currency
	 * @param attrId
	 *            属性值的id列表
	 * @return
	 */
	// @Cacheable(value = "product", keyGenerator = "customKeyGenerator",
	// cacheManager = "productPageCacheManager")
	@RequestMapping(value = "/ic/v1/productsByCategoryId", method = RequestMethod.GET)
	public Result getListByCategory(
			@RequestParam(value = "currentPage") int currentPage,
			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "categoryId") int categoryId,
			@RequestParam(value = "lang") int lang,
			@RequestParam(value = "client") int client,
			@RequestParam(value = "currency") String currency,
			@RequestParam(value = "attrIds", required = false) List<Integer> attrIds) {
		List<CategoryWebsiteWithNameDto> dtos = categoryService
				.getChildCategoriesAll(categoryId, lang, client);
		if (dtos != null && dtos.size() > 0) {
			baseUtils.getChildCategorys(dtos, lang, client);
		} else {
			dtos.add(categoryService.getCategoryByCategoryId(lang, client,
					categoryId));
		}
		List<Integer> cateskus = dtos.stream().filter(p -> p.getIlevel() == 3)
				.map(p -> p.getIcategoryid()).collect(Collectors.toList());
		if (cateskus != null && cateskus.size() > 0) {
			List<String> listingids = mapperService.getListingIdsByCategoryId(
					cateskus, pageSize, currentPage, client, 1, attrIds);
			if (listingids != null && listingids.size() > 0) {
				int count = mapperService.getListingIdCountByCategoryId(
						cateskus, client, 1, attrIds);
				return new Result(
						Result.SUCCESS,
						Lists.transform(
								productBaseUtils
										.getProductBasePriceReviewCollectByListings(
												listingids, currency, client,
												lang),
								p -> BeanUtils.mapFromClass(p,
										CategoryProductListVo.class)).toArray(),
						Page.getPage(currentPage, count, pageSize));
			}
		}
		return new Result(Result.FAIL, null);
	}
}
