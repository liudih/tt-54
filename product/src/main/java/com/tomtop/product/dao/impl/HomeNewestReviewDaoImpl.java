package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeNewestReviewDao;
import com.tomtop.product.mappers.mysql.HomeNewestReviewMapper;
import com.tomtop.product.models.dto.HomeNewestReviewDto;

/**
 * 首页评论分享
 * 
 * @author liulj
 *
 */
@Repository("homeNewestReviewDao")
public class HomeNewestReviewDaoImpl implements IHomeNewestReviewDao {

	@Autowired
	private HomeNewestReviewMapper mapper;

	@Override
	public List<HomeNewestReviewDto> getListByClientLang(int client, int lang) {
		// TODO Auto-generated method stub
		List<HomeNewestReviewDto> dto = mapper
				.getListByClientLang(client, lang);
		if (dto == null || dto.isEmpty()) {
			return mapper.getListByClientLang(client, 1);
		} else {
			return dto;
		}
	}

}