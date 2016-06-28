package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeFeaturedCategorySkuBo;

/**
 * 首页特别类目产品的奥搜索产品dao接口
 * 
 * @author liulj
 *
 */
public interface IHomeFcategorySkuService {
	/**
	 * 根具特别类目的id获取list
	 * 
	 * @param fcategoryid
	 *            特别类目id
	 * @return
	 */
	@Cacheable(value = { "home_featured_category_sku", "home" }, keyGenerator = "customKeyGenerator")
	public List<HomeFeaturedCategorySkuBo> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid);
}
