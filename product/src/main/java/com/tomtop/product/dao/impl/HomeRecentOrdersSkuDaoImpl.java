package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IHomeRecentOrdersSkuDao;
import com.tomtop.product.mappers.mysql.HomeRecentOrdersSkuMapper;
import com.tomtop.product.models.dto.HomeRecentOrdersSkuDto;

/**
 * 首页特别类目产品的奥搜索关键字dao接口
 * 
 * @author liulj
 *
 */
@Repository("homeRecentOrdersSkuDao")
public class HomeRecentOrdersSkuDaoImpl implements IHomeRecentOrdersSkuDao {

	@Autowired
	private HomeRecentOrdersSkuMapper mapper;

	@Override
	public List<HomeRecentOrdersSkuDto> getListByClientLang(int client, int lang) {
		// TODO Auto-generated method stub
		List<HomeRecentOrdersSkuDto> dto = mapper.getListByClientLang(client,
				lang);
		if (dto == null || dto.isEmpty()) {
			return mapper.getListByClientLang(client, 1);
		} else {
			return dto;
		}
	}
}
