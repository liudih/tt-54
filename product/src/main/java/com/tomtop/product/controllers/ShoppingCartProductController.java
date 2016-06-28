package com.tomtop.product.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.ListingIdNumBo;
import com.tomtop.product.models.bo.ProductBasePriceReviewCollectInfoBo;
import com.tomtop.product.models.vo.ShoppingCartProductSkuVo;
import com.tomtop.product.services.IProductBaseInfoService;

/**
 * 获取购物车产品信息
 * 
 * @author liulj
 *
 */
@RestController
public class ShoppingCartProductController {

	@Autowired
	private IProductBaseInfoService productBaseUtils;

	/**
	 * 获取产品list
	 * 
	 * @param listingIds
	 * @param lang
	 * @param client
	 * @param currency
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/product/shoppingCart", method = RequestMethod.GET)
	public Result getProducts(
			@RequestParam(value = "listings") String listings,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		if (StringUtils.isNotBlank(listings)
				&& StringUtils.isNotBlank(currency)) {
			List<ListingIdNumBo> list = JSON.parseArray(listings,
					ListingIdNumBo.class);
			if (list != null && list.size() > 0) {
				List<ProductBasePriceReviewCollectInfoBo> products = productBaseUtils
						.getProductBasePriceByListings(
								Lists.transform(list, p -> p.getListingId()),
								currency, client, lang);
				if (products != null && products.size() > 0) {
					Map<String, Integer> nums = list.stream().collect(
							Collectors.toMap(ListingIdNumBo::getListingId,
									ListingIdNumBo::getNum));
					return new Result(
							Result.SUCCESS,
							Lists.newArrayList(Lists.transform(
									products,
									p -> {
										ShoppingCartProductSkuVo skuVo = BeanUtils
												.mapFromClass(
														p,
														ShoppingCartProductSkuVo.class);
										skuVo.setNum(nums.get(p.getListingId()));
										return skuVo;
									})));
				}
			}
		}
		return new Result(Result.FAIL, null);
	}
}
