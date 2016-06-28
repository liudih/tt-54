package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeRecentOrdersSkuBo;

/**
 * 首页最近销售订单的国家产品
 * 
 * @author liulj
 *
 */
public interface IHomeRecentOrdersSkuService {

	/**
	 * 获取List根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	@Cacheable(value = { "home_recent_orders_sku", "home" }, keyGenerator = "customKeyGenerator", cacheManager = "recentOrdersCacheManager")
	List<HomeRecentOrdersSkuBo> getListByClientLang(int client, int lang);

}