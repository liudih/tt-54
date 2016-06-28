package com.tomtop.advert.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.advert.dao.IBaseBannersContentDao;
import com.tomtop.advert.dto.BaseBannersContentDto;
import com.tomtop.advert.mysql.mappers.BaseBannersContentMapper;

/**
 * 广告组内容
 * 
 * @author liulj
 *
 */
@Repository("baseBannersContentDao")
public class BaseBannersContentDaoImpl implements IBaseBannersContentDao {

	@Autowired
	BaseBannersContentMapper mapper;

	@Override
	public List<BaseBannersContentDto> getListByLayoutBannercode(
			String layoutCode, String bannerCode, int client, int lang,
			Integer categoryId) {
		// TODO Auto-generated method stub
		List<BaseBannersContentDto> bo = mapper.getListByLayoutBannercode(
				layoutCode, bannerCode, client, lang, categoryId);
		if (bo == null || bo.size() <= 0) {
			return mapper.getListByLayoutBannercode(layoutCode, bannerCode,
					client, 1, categoryId);
		} else {
			return bo;
		}
	}
}