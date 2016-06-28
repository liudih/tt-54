package com.tomtop.advert.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.advert.bo.BaseBannersContentBo;
import com.tomtop.advert.dao.IBaseBannersContentDao;
import com.tomtop.advert.services.IBaseBannersContentService;
import com.tomtop.framework.core.utils.BeanUtils;

/**
 * 广告组内容
 * 
 * @author liulj
 *
 */
@Service("baseBannersContentService")
public class BaseBannersContentServiceImpl implements
		IBaseBannersContentService {

	@Resource(name = "baseBannersContentDao")
	private IBaseBannersContentDao dao;

	@Override
	public List<BaseBannersContentBo> getListByLayoutBannercode(
			String layoutCode, String bannerCode, int client, int lang,
			Integer categoryId) {
		// TODO Auto-generated method stub
		return Lists.transform(dao.getListByLayoutBannercode(layoutCode,
				bannerCode, client, lang, categoryId), p -> BeanUtils
				.mapFromClass(p, BaseBannersContentBo.class));
	}
}