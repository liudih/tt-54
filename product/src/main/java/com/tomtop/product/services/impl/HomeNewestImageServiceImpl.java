package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeNewestImageDao;
import com.tomtop.product.models.bo.HomeNewestImageBo;
import com.tomtop.product.services.IHomeNewestImageService;

/**
 * 首页图片分享
 * 
 * @author liulj
 * 
 */
@Service("homeNewestImageService")
public class HomeNewestImageServiceImpl implements IHomeNewestImageService {

	@Resource(name = "homeNewestImageDao")
	private IHomeNewestImageDao dao;

	@Override
	public List<HomeNewestImageBo> getListByClientLang(int client, int lang) {
		// TODO Auto-generated method stub
		return Lists.newArrayList(Lists.newArrayList(Lists.transform(
				dao.getListByClientLang(client, lang),
				p -> BeanUtils.mapFromClass(p, HomeNewestImageBo.class))));
	}
}