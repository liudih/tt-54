package com.tomtop.product.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.tomtop.product.models.dto.HomeRecentOrdersCountryDto;

public interface HomeRecentOrdersCountryMapper {
	@Select({
			"select t2.name",
			"from home_recent_orders_country t1 inner join base_country t2 on t2.id=t1.country_id ",
			" and t1.language_id=t2.language_id and t2.is_enabled = 1 and t2.is_deleted = 0",
			"where t1.client_id=#{0} and t1.language_id=#{1} and t1.is_enabled = 1 and t1.is_deleted = 0" })
	@Results({
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "client_id", property = "clientId", jdbcType = JdbcType.INTEGER),
			@Result(column = "language_id", property = "languageId", jdbcType = JdbcType.INTEGER),
			@Result(column = "country_id", property = "countryId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "is_enabled", property = "isEnabled", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_deleted", property = "isDeleted", jdbcType = JdbcType.INTEGER),
			@Result(column = "created_by", property = "createdBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "last_updated_by", property = "lastUpdatedBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP) })
	List<HomeRecentOrdersCountryDto> getCountryNameListByClientLang(int client,
			int lang);
}