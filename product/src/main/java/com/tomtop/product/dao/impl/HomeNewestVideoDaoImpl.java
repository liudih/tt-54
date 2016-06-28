package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeNewestVideoDao;
import com.tomtop.product.mappers.mysql.HomeNewestVideoMapper;
import com.tomtop.product.models.dto.HomeNewestVideoDto;

/**
 * 首页评论视频
 * 
 * @author liulj
 *
 */
@Repository("homeNewestVideoDao")
public class HomeNewestVideoDaoImpl implements IHomeNewestVideoDao {

	@Autowired
	private HomeNewestVideoMapper mapper;

	@Override
	public List<HomeNewestVideoDto> getListByClientLang(int client, int lang) {
		List<HomeNewestVideoDto> dto = mapper.getListByClientLang(client, lang);
		if (dto == null || dto.isEmpty()) {
			return mapper.getListByClientLang(client, 1);
		} else {
			return dto;
		}
	}

}