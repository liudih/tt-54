package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeRecentOrdersSkuDao;
import com.tomtop.product.models.bo.HomeRecentOrdersSkuBo;
import com.tomtop.product.services.IHomeRecentOrdersSkuService;

/**
 * 首页特别类目产品的奥搜索关键字dao接口
 * 
 * @author liulj
 *
 */
@Repository("homeRecentOrdersSkuService")
public class HomeRecentOrdersSkuDaoImpl implements IHomeRecentOrdersSkuService {

	@Resource(name = "homeRecentOrdersSkuDao")
	private IHomeRecentOrdersSkuDao dao;

	@Override
	public List<HomeRecentOrdersSkuBo> getListByClientLang(int client, int lang) {
		// TODO Auto-generated method stub
		return Lists.newArrayList(Lists.transform(
				dao.getListByClientLang(client, lang),
				p -> BeanUtils.mapFromClass(p, HomeRecentOrdersSkuBo.class)));
	}
}
