package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeFcategoryBannerDao;
import com.tomtop.product.mappers.mysql.HomeFeaturedCategoryBannerMapper;
import com.tomtop.product.models.dto.HomeFeaturedCategoryBannerDto;

/**
 * 首页特别类目产品的奥搜索广告dao
 * 
 * @author liulj
 *
 */

@Repository("homeFcategoryBannerDao")
public class HomeFcategoryBannerDaoImpl implements IHomeFcategoryBannerDao {

	@Autowired
	private HomeFeaturedCategoryBannerMapper mapper;

	@Override
	public List<HomeFeaturedCategoryBannerDto> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid) {
		// TODO Auto-generated method stub
		List<HomeFeaturedCategoryBannerDto> dto = mapper
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
