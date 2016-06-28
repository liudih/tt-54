package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeNewestImageDao;
import com.tomtop.product.mappers.mysql.HomeNewestImageMapper;
import com.tomtop.product.models.dto.HomeNewestImageDto;

/**
 * 首页图片分享
 * 
 * @author liulj
 *
 */
@Repository("homeNewestImageDao")
public class HomeNewestImageDaoImpl implements IHomeNewestImageDao {
	@Autowired
	private HomeNewestImageMapper mapper;

	@Override
	public List<HomeNewestImageDto> getListByClientLang(int client, int lang) {
		// TODO Auto-generated method stub
		List<HomeNewestImageDto> dto = mapper.getListByClientLang(client, lang);
		if (dto == null || dto.isEmpty()) {
			return mapper.getListByClientLang(client, 1);
		} else {
			return dto;
		}
	}
}