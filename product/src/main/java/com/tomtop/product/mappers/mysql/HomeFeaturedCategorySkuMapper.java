package com.tomtop.product.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.tomtop.product.models.dto.HomeFeaturedCategorySkuDto;

public interface HomeFeaturedCategorySkuMapper {
	@Select({
			"select",
			"id, client_id, language_id, featured_category_id, listing_id, sku, sort",
			"from home_featured_category_sku",
			"where featured_category_id = #{0} and is_deleted = 0 and  is_enabled=1 and client_id=#{1} and language_id=#{2} ORDER BY sort" })
	@Results({
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "client_id", property = "clientId", jdbcType = JdbcType.INTEGER),
			@Result(column = "language_id", property = "languageId", jdbcType = JdbcType.INTEGER),
			@Result(column = "featured_category_id", property = "featuredCategoryId", jdbcType = JdbcType.INTEGER),
			@Result(column = "listing_id", property = "listingId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "sku", property = "sku", jdbcType = JdbcType.VARCHAR),
			@Result(column = "sort", property = "sort", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_enabled", property = "isEnabled", jdbcType = JdbcType.INTEGER),
			@Result(column = "created_by", property = "createdBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "last_updated_by", property = "lastUpdatedBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "is_deleted", property = "isDeleted", jdbcType = JdbcType.INTEGER) })
	List<HomeFeaturedCategorySkuDto> getListByFcategoryClientLangua(
			int fcategoryid, int iclientid, int ilanguageid);
}