package com.tomtop.product.mappers.mysql;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.tomtop.product.models.dto.HomeDailyDealDto;

public interface HomeDailyDealMapper {
	@Select({
			"select listing_id,discount ",
			"from home_daily_deal",
			"where start_date=#{startDate, jdbcType =DATE} and client_id=#{client} and language_id=#{language} and is_enabled = 1 and is_deleted=0" })
	@Results({
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "client_id", property = "clientId", jdbcType = JdbcType.INTEGER),
			@Result(column = "language_id", property = "languageId", jdbcType = JdbcType.INTEGER),
			@Result(column = "listing_id", property = "listingId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "sku", property = "sku", jdbcType = JdbcType.VARCHAR),
			@Result(column = "start_date", property = "startDate", jdbcType = JdbcType.DATE),
			@Result(column = "discount", property = "discount", jdbcType = JdbcType.DECIMAL),
			@Result(column = "is_enabled", property = "isEnabled", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_deleted", property = "isDeleted", jdbcType = JdbcType.INTEGER),
			@Result(column = "created_by", property = "createdBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "last_updated_by", property = "lastUpdatedBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP) })
	List<HomeDailyDealDto> getListByStartDate(
			@Param("startDate") Date startDate, @Param("client") int client,
			@Param("language") int language);
}