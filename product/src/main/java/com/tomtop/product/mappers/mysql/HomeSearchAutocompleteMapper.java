package com.tomtop.product.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.tomtop.product.models.dto.HomeSearchAutocompleteDto;

public interface HomeSearchAutocompleteMapper {

	@Select({
			"<script>",
			"<bind name=\"keyword\" value=\"'%'+_parameter.keyword+'%'\" /> ",
			"select keyword from home_search_autocomplete",
			"where keyword like #{keyword} and is_enabled = 1 and is_deleted = 0 and client_id=#{client} and language_id=#{language}",
			"order by keyword", "</script>" })
	@Results({
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "client_id", property = "clientId", jdbcType = JdbcType.INTEGER),
			@Result(column = "language_id", property = "languageId", jdbcType = JdbcType.INTEGER),
			@Result(column = "keyword", property = "keyword", jdbcType = JdbcType.VARCHAR),
			@Result(column = "is_enabled", property = "isEnabled", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_deleted", property = "isDeleted", jdbcType = JdbcType.INTEGER),
			@Result(column = "created_by", property = "createdBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "last_updated_by", property = "lastUpdatedBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP) })
	List<HomeSearchAutocompleteDto> getKeywordList(
			@Param("keyword") String keyword, @Param("client") int client,
			@Param("language") int language);
}