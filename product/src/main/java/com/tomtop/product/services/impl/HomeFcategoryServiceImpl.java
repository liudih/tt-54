package com.tomtop.product.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeFcategoryDao;
import com.tomtop.product.models.bo.HomeFeaturedCategoryBo;
import com.tomtop.product.services.IHomeFcategoryService;

/**
 * 首页特别类目产品service
 * 
 * @author liulj
 *
 */
@Service("homeFcategoryService")
public class HomeFcategoryServiceImpl implements IHomeFcategoryService {

	@Resource(name = "homeFcategoryDao")
	private IHomeFcategoryDao factegoryDao;

	@Override
	public List<HomeFeaturedCategoryBo> getListClientLangua(int iclientid,
			int ilanguageid) {
		// TODO Auto-generated method stub
		return Lists.newArrayList(factegoryDao
				.getListClientLangua(iclientid, ilanguageid)
				.stream()
				.map(p -> BeanUtils.mapFromClass(p,
						HomeFeaturedCategoryBo.class))
				.collect(Collectors.toList()));
	}
}
