package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeNewestVideoBo;

/**
 * 首页评论视频
 * 
 * @author liulj
 *
 */
public interface IHomeNewestVideoService {

	/**
	 * 获取List根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	@Cacheable(value = { "home_newest_video", "home" }, keyGenerator = "customKeyGenerator")
	List<HomeNewestVideoBo> getListByClientLang(int client, int lang);

}