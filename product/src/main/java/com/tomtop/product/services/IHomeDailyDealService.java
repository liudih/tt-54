package com.tomtop.product.services;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeDailyDealBo;

/**
 * 每日推荐
 * 
 * @author liulj
 *
 */
public interface IHomeDailyDealService {

	/**
	 * 获取每日推荐
	 * 
	 * @param startDate
	 *            开始时间
	 * @param client
	 * @param language
	 * @return
	 */
	@Cacheable(value = { "home_daily_deal", "home" }, cacheManager = "dayCacheManager", keyGenerator = "customKeyGenerator")
	List<HomeDailyDealBo> getListByStartDate(Date startDate, int client,
			int language);
}