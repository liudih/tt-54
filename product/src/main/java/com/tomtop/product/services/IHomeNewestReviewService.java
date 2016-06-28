package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeNewestReviewBo;

/**
 * 首页评论分享
 * 
 * @author liulj
 *
 */
public interface IHomeNewestReviewService {

	/**
	 * 获取List根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	@Cacheable(value = { "home_newest_review", "home" }, keyGenerator = "customKeyGenerator")
	List<HomeNewestReviewBo> getListByClientLang(int client, int lang);

}