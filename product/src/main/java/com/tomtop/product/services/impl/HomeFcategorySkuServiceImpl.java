package com.tomtop.product.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeFcategorySkuDao;
import com.tomtop.product.models.bo.HomeFeaturedCategorySkuBo;
import com.tomtop.product.services.IHomeFcategorySkuService;

/**
 * 首页特别类目产品的奥搜索产品service
 * 
 * @author liulj
 *
 */
@Service("homeFcategorySkuService")
public class HomeFcategorySkuServiceImpl implements IHomeFcategorySkuService {

	@Resource(name = "homeFcategorySkuDao")
	private IHomeFcategorySkuDao dao;

	@Override
	public List<HomeFeaturedCategorySkuBo> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid) {
		// TODO Auto-generated method stub
		return Lists.newArrayList(dao
				.getListByFcategoryClientLangua(fcategoryid, iclientid,
						ilanguageid)
				.stream()
				.map(p -> BeanUtils.mapFromClass(p,
						HomeFeaturedCategorySkuBo.class))
				.collect(Collectors.toList()));
	}
}
