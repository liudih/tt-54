package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeNewestReviewDto;

/**
 * 首页评论分享
 * 
 * @author liulj
 *
 */
public interface IHomeNewestReviewDao {

	/**
	 * 获取List根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	List<HomeNewestReviewDto> getListByClientLang(int client, int lang);

}