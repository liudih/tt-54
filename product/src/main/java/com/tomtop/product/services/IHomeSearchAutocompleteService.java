package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.HomeSearchAutocompleteBo;

/**
 * 关键字自动补全
 * 
 * @author liulj
 *
 */

public interface IHomeSearchAutocompleteService {

	/**
	 * 获取关键字补全list
	 * 
	 * @param keyword
	 *            要搜索的关键字
	 * @param client
	 *            客户端id
	 * @param language
	 *            语言id
	 * @return
	 */
	@Cacheable(value = { "home_search_autocomplete", "home" }, keyGenerator = "customKeyGenerator")
	List<HomeSearchAutocompleteBo> getKeywordList(String keyword, int client,
			int language);
}