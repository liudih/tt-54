package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeNewestImageBo;

/**
 * 首页图片分享
 * 
 * @author liulj
 *
 */
public interface IHomeNewestImageService {
	/**
	 * 获取List根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	@Cacheable(value = { "home_newest_image", "home" }, keyGenerator = "customKeyGenerator")
	List<HomeNewestImageBo> getListByClientLang(int client, int lang);
}