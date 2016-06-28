package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeFeaturedCategoryBo;

/**
 * 首页特别类目产品service接口
 * 
 * @author liulj
 *
 */
public interface IHomeFcategoryService {
	/**
	 * 根具语言id和站点id获取首页特别类目
	 * 
	 * @param iclientid
	 * @param ilanguageid
	 * @return
	 */
	@Cacheable(value = { "home_featured_category", "home" }, keyGenerator = "customKeyGenerator")
	List<HomeFeaturedCategoryBo> getListClientLangua(int iclientid,
			int ilanguageid);
}
