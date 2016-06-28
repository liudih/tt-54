package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeRecentOrdersCountryBo;

/**
 * 首页最近销售订单的国家信息
 * 
 * @author liulj
 *
 */
public interface IHomeRecentOrdersCountryService {
	/**
	 * 获取国家的名称List根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	@Cacheable(value = { "home_recent_orders_country", "home" }, keyGenerator = "customKeyGenerator", cacheManager = "recentOrdersCacheManager")
	List<HomeRecentOrdersCountryBo> getCountryNameListByClientLang(int client,
			int lang);
}