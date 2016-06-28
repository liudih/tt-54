package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeSearchAutocompleteDao;
import com.tomtop.product.mappers.mysql.HomeSearchAutocompleteMapper;
import com.tomtop.product.models.dto.HomeSearchAutocompleteDto;

@Repository("homeSearchAutocompleteDao")
public class HomeSearchAutocompleteDaoImpl implements
		IHomeSearchAutocompleteDao {

	@Autowired
	private HomeSearchAutocompleteMapper mapper;

	@Override
	public List<HomeSearchAutocompleteDto> getKeywordList(String keyword,
			int client, int language) {
		// TODO Auto-generated method stub
		return mapper.getKeywordList(keyword, client, language);
	}

}
