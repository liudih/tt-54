package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeSearchKeywordDao;
import com.tomtop.product.models.bo.HomeSearchKeywordBo;
import com.tomtop.product.services.IHomeSearchKeywordService;

/**
 * 首页搜索关键字
 * 
 * @author liulj
 *
 */
@Service("homeSearchKeywordService")
public class HomeSearchKeywordServiceImpl implements IHomeSearchKeywordService {

	@Resource(name = "homeSearchKeywordDao")
	private IHomeSearchKeywordDao dao;

	@Override
	public List<HomeSearchKeywordBo> getKeywordList(int categoryId, int client,
			int language) {
		return Lists.newArrayList(Lists.transform(
				dao.getKeywordList(categoryId, client, language),
				p -> BeanUtils.mapFromClass(p, HomeSearchKeywordBo.class)));
	}
}