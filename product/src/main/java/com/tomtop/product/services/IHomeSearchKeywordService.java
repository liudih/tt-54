package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeSearchKeywordBo;

/**
 * 首页搜索关键字
 * 
 * @author liulj
 *
 */
public interface IHomeSearchKeywordService {

	/**
	 * 获取关键字list
	 * 
	 * @param categoryId
	 *            类目id
	 * @param client
	 *            客户端id
	 * @param language
	 *            语言id
	 * @return
	 */
	@Cacheable(value = { "home_search_keyword", "home" }, keyGenerator = "customKeyGenerator")
	List<HomeSearchKeywordBo> getKeywordList(int categoryId, int client,
			int language);

}