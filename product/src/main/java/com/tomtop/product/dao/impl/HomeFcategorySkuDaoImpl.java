package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeFcategorySkuDao;
import com.tomtop.product.mappers.mysql.HomeFeaturedCategorySkuMapper;
import com.tomtop.product.models.dto.HomeFeaturedCategorySkuDto;

/**
 * 首页特别类目产品的奥搜索产品dao接口
 * 
 * @author liulj
 *
 */
@Repository("homeFcategorySkuDao")
public class HomeFcategorySkuDaoImpl implements IHomeFcategorySkuDao {
	@Autowired
	private HomeFeaturedCategorySkuMapper mapper;

	@Override
	public List<HomeFeaturedCategorySkuDto> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid) {
		// TODO Auto-generated method stub
		List<HomeFeaturedCategorySkuDto> dto = mapper
				.getListByFcategoryClientLangua(fcategoryid, iclientid,
						ilanguageid);
		if (dto == null || dto.isEmpty()) {
			return mapper.getListByFcategoryClientLangua(fcategoryid,
					iclientid, 1);
		} else {
			return dto;
		}
	}
}
