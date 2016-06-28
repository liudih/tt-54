package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeNewestVideoDto;

/**
 * 首页评论视频
 * 
 * @author liulj
 *
 */
public interface IHomeNewestVideoDao {

	/**
	 * 获取List根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	List<HomeNewestVideoDto> getListByClientLang(int client, int lang);

}