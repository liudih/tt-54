package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeBrandDao;
import com.tomtop.product.models.bo.HomeBrandBo;
import com.tomtop.product.services.IHomeBrandService;

/**
 * 首页品牌管理
 * 
 * @author liulj
 *
 */
@Service("homeBrandService")
public class HomeBrandServiceImpl implements IHomeBrandService {

	@Resource(name = "homeBrandDao")
	private IHomeBrandDao dao;

	@Override
	public List<HomeBrandBo> getListByClientLang(int client, int lang) {
		return Lists.newArrayList(Lists.transform(
				dao.getListByClientLang(client, lang),
				p -> BeanUtils.mapFromClass(p, HomeBrandBo.class)));
	}
}