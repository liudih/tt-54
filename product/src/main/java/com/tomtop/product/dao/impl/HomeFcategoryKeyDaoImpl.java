package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeFcategoryKeyDao;
import com.tomtop.product.mappers.mysql.HomeFeaturedCategoryKeyMapper;
import com.tomtop.product.models.dto.HomeFeaturedCategoryKeyDto;

/**
 * 首页特别类目产品的奥搜索关键字dao接口
 * 
 * @author liulj
 *
 */
@Repository("homeFcategoryKeyDao")
public class HomeFcategoryKeyDaoImpl implements IHomeFcategoryKeyDao {
	@Autowired
	private HomeFeaturedCategoryKeyMapper mapper;

	@Override
	public List<HomeFeaturedCategoryKeyDto> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid) {
		// TODO Auto-generated method stub
		List<HomeFeaturedCategoryKeyDto> dto = mapper
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
