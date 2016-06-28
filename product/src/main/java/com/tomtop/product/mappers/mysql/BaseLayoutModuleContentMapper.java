package com.tomtop.product.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.tomtop.product.models.dto.LayoutModuleContentDto;

/**
 * 布局模块内容mapper
 * 
 * @author liulj
 *
 */
public interface BaseLayoutModuleContentMapper {

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
	@Select({
			"select",
			"id, layout_code,listing_id,layout_id, layout_module_code, layout_module_id, category_id, sku, sort",
			"from base_layout_module_content ",
			"where layout_code=#{layoutcode} and  is_deleted = 0 and  layout_module_code=#{modulecode} and client_id=#{client} and language_id=#{language} and is_show=1 and is_enabled=1 ORDER BY sort" })
	@Results({
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "layout_code", property = "layoutCode", jdbcType = JdbcType.VARCHAR),
			@Result(column = "layout_id", property = "layoutId", jdbcType = JdbcType.INTEGER),
			@Result(column = "layout_module_code", property = "layoutModuleCode", jdbcType = JdbcType.VARCHAR),
			@Result(column = "layout_module_id", property = "layoutModuleId", jdbcType = JdbcType.INTEGER),
			@Result(column = "client_id", property = "clientId", jdbcType = JdbcType.INTEGER),
			@Result(column = "language_id", property = "languageId", jdbcType = JdbcType.INTEGER),
			@Result(column = "category_id", property = "categoryId", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_show", property = "isShow", jdbcType = JdbcType.INTEGER),
			@Result(column = "listing_id", property = "listingId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "sku", property = "sku", jdbcType = JdbcType.VARCHAR),
			@Result(column = "sort", property = "sort", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_enabled", property = "isEnabled", jdbcType = JdbcType.INTEGER),
			@Result(column = "created_by", property = "createdBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "last_updated_by", property = "lastUpdatedBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "is_deleted", property = "isDeleted", jdbcType = JdbcType.INTEGER) })
	List<LayoutModuleContentDto> getListByLayoutModuleClinetLanguage(
			@Param("layoutcode") String layoutcode,
			@Param("modulecode") String modulecode,
			@Param("client") int client, @Param("language") int language);
	
	@Select({"select id, layout_code layoutCode,listing_id listingId,layout_id loyoutId, layout_module_code layoutModuleCode, layout_module_id layoutModuleId,"
			,"category_id categoryId, sku, sort from base_layout_module_content "
			,"where language_id=#{0} and client_id=#{1} and layout_code=#{2} and is_show=1 "
			,"and is_enabled=1 and is_deleted=0  order by layout_module_code,sort"})
	List<LayoutModuleContentDto> getListByLayoutModule(Integer language,Integer clientId,String layoutcode);
}