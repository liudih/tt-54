package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeSearchKeywordDto;

/**
 * 首页搜索关键字
 * 
 * @author liulj
 *
 */
public interface IHomeSearchKeywordDao {

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
	List<HomeSearchKeywordDto> getKeywordList(int categoryId, int client,
			int language);

}