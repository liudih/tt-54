package com.tomtop.product.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeFcategoryBannerDao;
import com.tomtop.product.models.bo.HomeFeaturedCategoryBannerBo;
import com.tomtop.product.services.IHomeFcategoryBannerService;

/**
 * 首页特别类目产品的奥搜索广告dao
 * 
 * @author liulj
 *
 */

@Service("homeFcategoryBannerService")
public class HomeFcategoryBannerServiceImpl implements
		IHomeFcategoryBannerService {

	@Resource(name = "homeFcategoryBannerDao")
	private IHomeFcategoryBannerDao dao;

	@Override
	public List<HomeFeaturedCategoryBannerBo> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid) {
		// TODO Auto-generated method stub
		return Lists.newArrayList(dao
				.getListByFcategoryClientLangua(fcategoryid, iclientid,
						ilanguageid)
				.stream()
				.map(p -> BeanUtils.mapFromClass(p,
						HomeFeaturedCategoryBannerBo.class))
				.collect(Collectors.toList()));
	}
}
