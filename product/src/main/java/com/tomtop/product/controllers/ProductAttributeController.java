package com.tomtop.product.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.ProductAttributeBo;
import com.tomtop.product.models.dto.CategoryWebsiteWithNameDto;
import com.tomtop.product.models.vo.ProductAttributeValueVo;
import com.tomtop.product.services.ICategoryService;
import com.tomtop.product.services.IProductBaseInfoService;
import com.tomtop.product.services.IProductCategoryMapperService;
import com.tomtop.product.services.IProductDetailService;
import com.tomtop.product.utils.ProductBaseUtils;

/**
 * 产品属性controller
 * 
 * @author liulj
 *
 */
@RestController
public class ProductAttributeController {

	@Autowired
	private IProductBaseInfoService productBaseUtils;

	@Autowired
	private ProductBaseUtils baseUtils;

	@Autowired
	ICategoryService categoryService;

	@Autowired
	IProductDetailService productDetailService;

	@Autowired
	private IProductCategoryMapperService mapperService;

	// @Cacheable(value = "product", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/productAttrByCategoryId", method = RequestMethod.GET)
	public Result getListByCategoryId(
			@RequestParam(value = "categoryId") int categoryId,
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
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
					cateskus, client, 1);
			Map<String, List<String>> destAttrs = Maps.newHashMap();
			Map<String, List<ProductAttributeBo>> attrs = productDetailService
					.getProductAttributeDtoByListingIds(listingids, lang,
							client)
					.stream()
					.collect(
							Collectors
									.groupingBy(ProductAttributeBo::getCkeyname));

			attrs.forEach((k, v) -> {
				destAttrs.put(k, Lists.newArrayList(v
						.stream()
						.map(p -> JSON
								.toJSONString(new ProductAttributeValueVo(p
										.getIvalueid(), p.getCvaluename())))
						.distinct().collect(Collectors.toList())));
			});
			return new Result(Result.SUCCESS, destAttrs);
		}
		return new Result(Result.FAIL, null);
	}
}
