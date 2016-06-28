package com.tomtop.product.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.ILayoutmoduleContentDao;
import com.tomtop.product.models.bo.BaseLayoutmoduleContentBo;
import com.tomtop.product.services.ILayoutmoduleContentService;

/**
 * 布局模块内容service实现
 * 
 * @author liulj
 *
 */
@Service("layoutmoduleContentService")
public class LayoutmoduleContentServiceImpl implements
		ILayoutmoduleContentService {

	@Resource(name = "layoutmoduleContentDao")
	private ILayoutmoduleContentDao dao;
	
	@Override
	public List<BaseLayoutmoduleContentBo> getListByLayoutModuleClinetLanguage(
			String layoutcode, String modulecode, int client, int language) {
		return Lists.newArrayList(dao
				.getListByLayoutModuleClinetLanguage(layoutcode, modulecode,client, language)
				.stream()
				.map(p -> BeanUtils.mapFromClass(p,
						BaseLayoutmoduleContentBo.class))
				.collect(Collectors.toList()));
	}

}