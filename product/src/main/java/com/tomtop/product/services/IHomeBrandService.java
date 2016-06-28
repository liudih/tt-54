package com.tomtop.product.services;

import java.util.List;

import com.tomtop.product.models.bo.HomeBrandBo;

/**
 * 首页品牌管理
 * 
 * @author liulj
 *
 */
public interface IHomeBrandService {
	/**
	 * 获取list，根具客户端和语言
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	List<HomeBrandBo> getListByClientLang(int client, int lang);
}