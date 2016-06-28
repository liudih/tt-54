package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeRecentOrdersCountryDao;
import com.tomtop.product.mappers.mysql.HomeRecentOrdersCountryMapper;
import com.tomtop.product.models.dto.HomeRecentOrdersCountryDto;

/**
 * 首页特别类目产品dao
 * 
 * @author liulj
 *
 */
@Repository("homeRecentOrdersCountryDao")
public class HomeRecentOrdersCountryDaoImpl implements
		IHomeRecentOrdersCountryDao {
	@Autowired
	private HomeRecentOrdersCountryMapper mapper;

	@Override
	public List<HomeRecentOrdersCountryDto> getCountryNameListByClientLang(
			int client, int lang) {
		// TODO Auto-generated method stub
		List<HomeRecentOrdersCountryDto> dto = mapper
				.getCountryNameListByClientLang(client, lang);
		if (dto == null || dto.isEmpty()) {
			return mapper.getCountryNameListByClientLang(client, 1);
		} else {
			return dto;
		}
	}
}
