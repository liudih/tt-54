package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeFeaturedCategoryBannerDto;

/**
 * 首页特别类目产品的奥搜索广告dao接口
 * 
 * @author liulj
 *
 */
public interface IHomeFcategoryBannerDao {
	/**
	 * 根具特别类目的id获取list
	 * 
	 * @param fcategoryid
	 *            特别类目id
	 * @return
	 */
	public List<HomeFeaturedCategoryBannerDto> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid);
}
