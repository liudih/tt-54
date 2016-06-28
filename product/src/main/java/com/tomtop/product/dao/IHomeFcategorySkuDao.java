package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeFeaturedCategorySkuDto;

/**
 * 首页特别类目产品的奥搜索产品dao接口
 * 
 * @author liulj
 *
 */
public interface IHomeFcategorySkuDao {
	/**
	 * 根具特别类目的id获取list
	 * 
	 * @param fcategoryid
	 *            特别类目id
	 * @return
	 */
	public List<HomeFeaturedCategorySkuDto> getListByFcategoryClientLangua(int fcategoryid,int iclientid, int ilanguageid);
}
