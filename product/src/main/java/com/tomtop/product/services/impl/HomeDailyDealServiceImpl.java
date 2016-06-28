package com.tomtop.product.services.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeDailyDealDao;
import com.tomtop.product.models.bo.HomeDailyDealBo;
import com.tomtop.product.services.IHomeDailyDealService;

@Service("homeDailyDealService")
public class HomeDailyDealServiceImpl implements IHomeDailyDealService {

	@Resource(name = "homeDailyDealDao")
	private IHomeDailyDealDao dao;

	@Override
	public List<HomeDailyDealBo> getListByStartDate(Date startDate, int client,
			int language) {
		return Lists.newArrayList(Lists.newArrayList(Lists.transform(
				dao.getListByStartDate(startDate, client, language),
				p -> BeanUtils.mapFromClass(p, HomeDailyDealBo.class))));
	}
}
