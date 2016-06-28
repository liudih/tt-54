package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeFeaturedCategoryDto;

/**
 * 首页特别类目产品dao接口
 * 
 * @author liulj
 *
 */
public interface IHomeFcategoryDao {
	/**
	 * 根具语言id和站点id获取首页特别类目
	 * 
	 * @param iclientid
	 * @param ilanguageid
	 * @return
	 */
	List<HomeFeaturedCategoryDto> getListClientLangua(int iclientid, int ilanguageid);
}
