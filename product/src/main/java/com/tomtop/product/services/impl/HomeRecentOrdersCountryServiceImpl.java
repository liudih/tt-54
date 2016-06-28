package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeRecentOrdersCountryDao;
import com.tomtop.product.models.bo.HomeRecentOrdersCountryBo;
import com.tomtop.product.services.IHomeRecentOrdersCountryService;

/**
 * 首页特别类目产品dao
 * 
 * @author liulj
 *
 */
@Repository("homeRecentOrdersCountryService")
public class HomeRecentOrdersCountryServiceImpl implements
		IHomeRecentOrdersCountryService {

	@Resource(name = "homeRecentOrdersCountryDao")
	private IHomeRecentOrdersCountryDao dao;

	@Override
	public List<HomeRecentOrdersCountryBo> getCountryNameListByClientLang(
			int client, int lang) {
		// TODO Auto-generated method stub
		return Lists
				.newArrayList(Lists.transform(dao
						.getCountryNameListByClientLang(client, lang),
						p -> BeanUtils.mapFromClass(p,
								HomeRecentOrdersCountryBo.class)));
	}
}
