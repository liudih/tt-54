package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.ILayoutmoduleContentDao;
import com.tomtop.product.mappers.mysql.BaseLayoutModuleContentMapper;
import com.tomtop.product.models.dto.LayoutModuleContentDto;

/**
 * 布局模块内容dao
 * 
 * @author liulj
 *
 */
@Repository("layoutmoduleContentDao")
public class LayoutmoduleContentDaoImpl implements ILayoutmoduleContentDao {

	@Autowired
	private BaseLayoutModuleContentMapper mapper;

	@Override
	public List<LayoutModuleContentDto> getListByLayoutModuleClinetLanguage(
			String layoutcode, String modulecode, int client, int language) {
		List<LayoutModuleContentDto> dto = mapper.getListByLayoutModuleClinetLanguage(layoutcode, modulecode,client, language);
		if (dto == null || dto.isEmpty()) {
			return mapper.getListByLayoutModuleClinetLanguage(layoutcode,modulecode, client, 1);
		} else {
			return dto;
		}
	}
}