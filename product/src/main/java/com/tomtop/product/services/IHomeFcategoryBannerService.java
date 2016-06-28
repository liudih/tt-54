package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeFeaturedCategoryBannerBo;

/**
 * 首页特别类目产品的奥搜索广告service接口
 * 
 * @author liulj
 *
 */
public interface IHomeFcategoryBannerService {
	/**
	 * 根具特别类目的id获取list
	 * 
	 * @param fcategoryid
	 *            特别类目id
	 * @return
	 */
	@Cacheable(value = { "home_featured_category_banner", "home" }, keyGenerator = "customKeyGenerator")
	public List<HomeFeaturedCategoryBannerBo> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid);
}
