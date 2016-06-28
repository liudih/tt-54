package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeRecentOrdersSkuDto;

/**
 * 首页最近销售订单的国家产品
 * 
 * @author liulj
 *
 */
public interface IHomeRecentOrdersSkuDao {

	/**
	 * 获取List根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	List<HomeRecentOrdersSkuDto> getListByClientLang(int client, int lang);

}