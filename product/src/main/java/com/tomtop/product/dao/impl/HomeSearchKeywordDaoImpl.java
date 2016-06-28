package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeSearchKeywordDao;
import com.tomtop.product.mappers.mysql.HomeSearchKeywordMapper;
import com.tomtop.product.models.dto.HomeSearchKeywordDto;

@Repository("homeSearchKeywordDao")
public class HomeSearchKeywordDaoImpl implements IHomeSearchKeywordDao {

	@Autowired
	private HomeSearchKeywordMapper mapper;

	@Override
	public List<HomeSearchKeywordDto> getKeywordList(int categoryId,
			int client, int language) {
		List<HomeSearchKeywordDto> dto = mapper.getKeywordList(categoryId,
				client, language);
		if (categoryId > 0 && (dto == null || dto.size() <= 0)) {
			dto = mapper.getKeywordList(0, client, language);
		}
		return dto;
	}
}
