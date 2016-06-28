package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeNewestVideoDao;
import com.tomtop.product.models.bo.HomeNewestVideoBo;
import com.tomtop.product.services.IHomeNewestVideoService;

/**
 * 首页评论视频
 * 
 * @author liulj
 *
 */
@Service("homeNewestVideoService")
public class HomeNewestVideoServiceImpl implements IHomeNewestVideoService {

	@Resource(name = "homeNewestVideoDao")
	private IHomeNewestVideoDao dao;

	@Override
	public List<HomeNewestVideoBo> getListByClientLang(int client, int lang) {
		return Lists.newArrayList(Lists.transform(
				dao.getListByClientLang(client, lang),
				p -> BeanUtils.mapFromClass(p, HomeNewestVideoBo.class)));
	}

}