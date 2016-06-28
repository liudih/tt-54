package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeFcategoryDao;
import com.tomtop.product.mappers.mysql.HomeFeaturedCategoryMapper;
import com.tomtop.product.models.dto.HomeFeaturedCategoryDto;

/**
 * 首页特别类目产品dao
 * 
 * @author liulj
 *
 */
@Repository("homeFcategoryDao")
public class HomeFcategoryDaoImpl implements IHomeFcategoryDao {
	@Autowired
	private HomeFeaturedCategoryMapper mapper;

	@Override
	public List<HomeFeaturedCategoryDto> getListClientLangua(int iclientid,
			int ilanguageid) {
		// TODO Auto-generated method stub
		List<HomeFeaturedCategoryDto> dto = mapper.getListClientLangua(
				iclientid, ilanguageid);
		if (dto == null || dto.isEmpty()) {
			return mapper.getListClientLangua(iclientid, 1);
		} else {
			return dto;
		}
	}
}
