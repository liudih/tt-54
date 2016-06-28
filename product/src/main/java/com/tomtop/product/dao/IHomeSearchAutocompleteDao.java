package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeSearchAutocompleteDto;

/**
 * 关键字自动补全
 * 
 * @author liulj
 *
 */

public interface IHomeSearchAutocompleteDao {

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
	List<HomeSearchAutocompleteDto> getKeywordList(String keyword, int client,
			int language);
}