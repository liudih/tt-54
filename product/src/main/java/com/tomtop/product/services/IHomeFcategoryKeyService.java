package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeFeaturedCategoryKeyBo;

/**
 * 首页特别类目产品的奥搜索关键字service接口
 * 
 * @author liulj
 *
 */
public interface IHomeFcategoryKeyService {
	/**
	 * 根具特别类目的id获取list
	 * 
	 * @param fcategoryid
	 *            特别类目id
	 * @return
	 */
	@Cacheable(value = { "home_featured_category_key", "home" }, keyGenerator = "customKeyGenerator")
	public List<HomeFeaturedCategoryKeyBo> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid);
}
