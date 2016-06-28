package com.tomtop.product.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.tomtop.product.models.dto.HomeSearchKeywordDto;

public interface HomeSearchKeywordMapper {

	@Select({
			"select sort, keyword",
			"from home_search_keyword",
			"where category_id = #{categoryId} and is_enabled = 1 and is_deleted = 0 and client_id=#{client} and language_id=#{language} ORDER BY sort" })
	@Results({
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "client_id", property = "clientId", jdbcType = JdbcType.INTEGER),
			@Result(column = "language_id", property = "languageId", jdbcType = JdbcType.INTEGER),
			@Result(column = "category_id", property = "categoryId", jdbcType = JdbcType.INTEGER),
			@Result(column = "sort", property = "sort", jdbcType = JdbcType.INTEGER),
			@Result(column = "keyword", property = "keyword", jdbcType = JdbcType.VARCHAR),
			@Result(column = "is_enabled", property = "isEnabled", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_deleted", property = "isDeleted", jdbcType = JdbcType.INTEGER),
			@Result(column = "created_by", property = "createdBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "last_updated_by", property = "lastUpdatedBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP) })
	List<HomeSearchKeywordDto> getKeywordList(
			@Param("categoryId") int categoryId, @Param("client") int client,
			@Param("language") int language);

}