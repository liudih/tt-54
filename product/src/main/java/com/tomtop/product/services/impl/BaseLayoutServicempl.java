package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IBaseLayoutDao;
import com.tomtop.product.models.bo.BaseLayoutBo;
import com.tomtop.product.models.dto.BaseLayoutDto;
import com.tomtop.product.services.IBaseLayoutService;

/**
 * 布噼
 * 
 * @author liulj
 *
 */
@Service("baseLayoutService")
public class BaseLayoutServicempl implements IBaseLayoutService {

	@Resource(name = "baseLayoutDao")
	private IBaseLayoutDao dao;

	@Override
	public List<BaseLayoutBo> getListByCode(String code, int client, int lang) {
		List<BaseLayoutDto> bldList = dao.getListByCode(code, client, lang);
		List<BaseLayoutBo> blboList = Lists.newArrayList(Lists.transform(bldList,p -> BeanUtils.mapFromClass(p, BaseLayoutBo.class)));
		return blboList;
	}
}