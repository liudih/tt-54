package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.HomeBrandDto;

/**
 * 首页品牌管理
 * 
 * @author liulj
 *
 */
public interface IHomeBrandDao {
	/**
	 * 获取list，根具客户端和语言
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	List<HomeBrandDto> getListByClientLang(int client, int lang);
}