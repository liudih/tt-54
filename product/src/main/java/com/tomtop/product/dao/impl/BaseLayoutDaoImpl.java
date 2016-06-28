package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IBaseLayoutDao;
import com.tomtop.product.mappers.mysql.BaseLayoutMapper;
import com.tomtop.product.models.dto.BaseLayoutDto;

/**
 * 布噼
 * 
 * @author liulj
 *
 */
@Repository("baseLayoutDao")
public class BaseLayoutDaoImpl implements IBaseLayoutDao {

	@Autowired
	private BaseLayoutMapper mapper;

	@Override
	public List<BaseLayoutDto> getListByCode(String code, int client, int lang) {
		List<BaseLayoutDto> dto = mapper.getListByCode(code, client, lang);
		if (dto == null || dto.isEmpty()) {
			return mapper.getListByCode(code, client, 1);
		} else {
			return dto;
		}
	}
}