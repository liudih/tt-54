package com.tomtop.product.dao;

import java.util.Date;
import java.util.List;

import com.tomtop.product.models.dto.HomeDailyDealDto;

/**
 * 每日推荐
 * 
 * @author liulj
 *
 */
public interface IHomeDailyDealDao {

	/**
	 * 获取每日推荐
	 * 
	 * @param startDate
	 *            开始时间
	 * @param client
	 * @param language
	 * @return
	 */
	List<HomeDailyDealDto> getListByStartDate(Date startDate, int client,
			int language);
}