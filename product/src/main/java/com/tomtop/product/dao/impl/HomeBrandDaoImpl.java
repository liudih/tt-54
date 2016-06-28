package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeBrandDao;
import com.tomtop.product.mappers.mysql.HomeBrandMapper;
import com.tomtop.product.models.dto.HomeBrandDto;

/**
 * 首页品牌管理
 * 
 * @author liulj
 *
 */
@Repository("homeBrandDao")
public class HomeBrandDaoImpl implements IHomeBrandDao {

	@Autowired
	private HomeBrandMapper mapper;

	@Override
	public List<HomeBrandDto> getListByClientLang(int client, int lang) {
		List<HomeBrandDto> dto = mapper.getListByClientLang(client, lang);
		if (dto == null || dto.isEmpty()) {
			return mapper.getListByClientLang(client, 1);
		} else {
			return dto;
		}
	}
}