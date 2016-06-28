package com.tomtop.advert.mysql.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.tomtop.advert.dto.BaseBannersContentDto;

public interface BaseBannersContentMapper {
	@Select({
			"<script>",
			"select name, url, img_url, title, sort,category_id",
			"from base_banners_content",
			"where layout_code=#{layoutCode} and banner_code=#{bannerCode} and client_id=#{client}",
			"<if test=\"categoryId != null\">and category_id =#{categoryId} </if>",
			" and language_id=#{lang} and is_enabled = 1 and is_deleted = 0 order BY sort",
			"</script>" })
	@Results({
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "category_id", property = "categoryId", jdbcType = JdbcType.INTEGER),
			@Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
			@Result(column = "url", property = "url", jdbcType = JdbcType.VARCHAR),
			@Result(column = "img_url", property = "imgUrl", jdbcType = JdbcType.VARCHAR),
			@Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
			@Result(column = "sort", property = "sort", jdbcType = JdbcType.INTEGER),
			@Result(column = "layout_code", property = "layoutCode", jdbcType = JdbcType.VARCHAR),
			@Result(column = "banner_code", property = "bannerCode", jdbcType = JdbcType.VARCHAR),
			@Result(column = "client_id", property = "clientId", jdbcType = JdbcType.INTEGER),
			@Result(column = "language_id", property = "languageId", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_enabled", property = "isEnabled", jdbcType = JdbcType.INTEGER),
			@Result(column = "is_deleted", property = "isDeleted", jdbcType = JdbcType.INTEGER),
			@Result(column = "created_by", property = "createdBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "last_updated_by", property = "lastUpdatedBy", jdbcType = JdbcType.VARCHAR),
			@Result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP) })
	public List<BaseBannersContentDto> getListByLayoutBannercode(
			@Param("layoutCode") String layoutCode,
			@Param("bannerCode") String bannerCode,
			@Param("client") int client, @Param("lang") int lang,
			@Param("categoryId") Integer categoryId);
}