package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeNewestImageDto;

/**
 * 首页图片分享
 * 
 * @author liulj
 *
 */
public interface IHomeNewestImageDao {
	/**
	 * 获取List根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	List<HomeNewestImageDto> getListByClientLang(int client, int lang);
}