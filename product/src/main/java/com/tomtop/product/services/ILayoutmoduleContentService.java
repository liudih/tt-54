package com.tomtop.product.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.BaseLayoutmoduleContentBo;

/**
 * 布局模块内容service
 * 
 * @author liulj
 *
 */
public interface ILayoutmoduleContentService {

	/**
	 * 根具布局标识，模块标识，语言，客户端获取模块内容列表
	 * 
	 * @param layoutcode
	 *            布局标识 HOME 主页
	 * @param modulecode
	 *            模块标识
	 * @param client
	 *            客户端: 1 TOMTOP-PC,2 TOMTOP-Mobile,3 TOMTOP-APP-IOS,4
	 *            TOMTOP-APP-Android
	 * @param language
	 *            语言 1 en
	 * @return list
	 */
	@Cacheable(value = { "base_layout_module_content", "base" }, keyGenerator = "customKeyGenerator")
	List<BaseLayoutmoduleContentBo> getListByLayoutModuleClinetLanguage(
			String layoutcode, String modulecode, int client, int language);
	
}