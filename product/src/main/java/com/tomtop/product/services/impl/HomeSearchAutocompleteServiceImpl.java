package com.tomtop.product.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.dao.IHomeSearchAutocompleteDao;
import com.tomtop.product.models.bo.HomeSearchAutocompleteBo;
import com.tomtop.product.services.IHomeSearchAutocompleteService;

/**
 * 关键字自动补全
 * 
 * @author liulj
 *
 */
@Service("homeSearchAutocompleteService")
public class HomeSearchAutocompleteServiceImpl implements
		IHomeSearchAutocompleteService {

	@Resource(name = "homeSearchAutocompleteDao")
	private IHomeSearchAutocompleteDao dao;

	@Override
	public List<HomeSearchAutocompleteBo> getKeywordList(String keyword,
			int client, int language) {
		// TODO Auto-generated method stub
		return Lists
				.newArrayList(Lists.transform(dao.getKeywordList(keyword,
						client, language), p -> BeanUtils.mapFromClass(p,
						HomeSearchAutocompleteBo.class)));
	}
}