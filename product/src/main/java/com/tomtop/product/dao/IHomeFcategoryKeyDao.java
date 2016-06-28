package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeFeaturedCategoryKeyDto;

/**
 * 首页特别类目产品的奥搜索关键字dao接口
 * 
 * @author liulj
 *
 */
public interface IHomeFcategoryKeyDao {
	/**
	 * 根具特别类目的id获取list
	 * 
	 * @param fcategoryid
	 *            特别类目id
	 * @return
	 */
	public List<HomeFeaturedCategoryKeyDto> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid);
}
