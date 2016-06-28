package com.tomtop.advert.mysql.mappers;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.tomtop.advert.bo.BaseBannersBo;

public interface BaseBannersMapper {
	@Select({
			"select",
			"id, name, code, client_id, language_id, layout_code, position_id, is_enabled, ",
			"is_deleted, created_by, created_on, last_updated_by, last_updated_on",
			"from base_banners", "where id = #{id,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
			@Result(column = "code", property = "code", jdbcType = JdbcType.VARCHAR),
			@Result(column = "client_id", property = "clientId", jdbcType = JdbcType.INTEGER),
			@Result(column = "language_id", property = "languageId", jdbcType = JdbcType.INTEGER),
			@Result(column = "layout_code", property = "layoutCode", jdbcType = JdbcType.VARCHAR),
			@Result(column = "position_id", property = "positionId", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_enabled", property = "isEnabled", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_deleted", property = "isDeleted", jdbcType = JdbcType.INTEGER),
			@Result(column = "created_by", property = "createdBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "last_updated_by", property = "lastUpdatedBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP) })
	public BaseBannersBo selectByPrimaryKey(Integer id);
}