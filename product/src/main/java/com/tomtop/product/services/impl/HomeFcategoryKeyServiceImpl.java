package com.tomtop.product.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeFcategoryKeyDao;
import com.tomtop.product.models.bo.HomeFeaturedCategoryKeyBo;
import com.tomtop.product.services.IHomeFcategoryKeyService;

/**
 * 首页特别类目产品的奥搜索关键字
 * 
 * @author liulj
 *
 */
@Service("homeFcategoryKeyService")
public class HomeFcategoryKeyServiceImpl implements IHomeFcategoryKeyService {
	@Resource(name = "homeFcategoryKeyDao")
	private IHomeFcategoryKeyDao dao;

	@Override
	public List<HomeFeaturedCategoryKeyBo> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid) {
		// TODO Auto-generated method stub
		return Lists.newArrayList(dao
				.getListByFcategoryClientLangua(fcategoryid, iclientid,
						ilanguageid)
				.stream()
				.map(p -> BeanUtils.mapFromClass(p,
						HomeFeaturedCategoryKeyBo.class))
				.collect(Collectors.toList()));
	}
}
