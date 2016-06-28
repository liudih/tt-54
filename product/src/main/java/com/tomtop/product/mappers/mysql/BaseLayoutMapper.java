package com.tomtop.product.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.tomtop.product.models.dto.BaseLayoutDto;

public interface BaseLayoutMapper {
	@Select({
			"select name, url, keyword, description,title, remark",
			"from base_layout where code=#{0} and client_id=#{1} and language_id=#{2} and is_enabled = 1 and is_deleted=0" })
	@Results({
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "code", property = "code", jdbcType = JdbcType.VARCHAR),
			@Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
			@Result(column = "client_id", property = "clientId", jdbcType = JdbcType.INTEGER),
			@Result(column = "language_id", property = "languageId", jdbcType = JdbcType.INTEGER),
			@Result(column = "url", property = "url", jdbcType = JdbcType.VARCHAR),
			@Result(column = "is_enabled", property = "isEnabled", jdbcType = JdbcType.INTEGER),
			@Result(column = "keyword", property = "keyword", jdbcType = JdbcType.VARCHAR),
			@Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
			@Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
			@Result(column = "remark", property = "remark", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_by", property = "createdBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "last_updated_by", property = "lastUpdatedBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "is_deleted", property = "isDeleted", jdbcType = JdbcType.INTEGER) })
	List<BaseLayoutDto> getListByCode(String code, int client, int lang);
}