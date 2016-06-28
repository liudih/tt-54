package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeRecentOrdersCountryDto;

/**
 * 首页最近销售订单的国家信息
 * 
 * @author liulj
 *
 */
public interface IHomeRecentOrdersCountryDao {
	/**
	 * 获取国家的名称List根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	List<HomeRecentOrdersCountryDto> getCountryNameListByClientLang(int client,
			int lang);
}