package com.tomtop.product.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.tomtop.product.models.dto.HomeFeaturedCategoryDto;

public interface HomeFeaturedCategoryMapper {
	@Select({
			"select",
			"id, img_url, number, client_id, language_id, category_id, sort ",
			" from home_featured_category where  is_deleted = 0 and is_enabled=1 and client_id=#{0} and language_id=#{1} ORDER BY sort" })
	@Results({
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "img_url", property = "imgUrl", jdbcType = JdbcType.VARCHAR),
			@Result(column = "number", property = "number", jdbcType = JdbcType.INTEGER),
			@Result(column = "client_id", property = "clientId", jdbcType = JdbcType.INTEGER),
			@Result(column = "language_id", property = "languageId", jdbcType = JdbcType.INTEGER),
			@Result(column = "category_id", property = "categoryId", jdbcType = JdbcType.INTEGER),
			@Result(column = "sort", property = "sort", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_enabled", property = "isEnabled", jdbcType = JdbcType.INTEGER),
			@Result(column = "created_by", property = "createdBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "last_updated_by", property = "lastUpdatedBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "is_deleted", property = "isDeleted", jdbcType = JdbcType.INTEGER) })
	List<HomeFeaturedCategoryDto> getListClientLangua(int iclientid,
			int ilanguageid);
}