package com.rabbit.conf.basemapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.Website;

public interface WebsiteMapper {
	@Select("select * from t_website where iid = #{0} limit 1")
	Website selectByPrimaryKey(Integer iid);

	@Select("select * from t_website ORDER BY iid")
	List<Website> getAll();

	@Select("SELECT ws.iid, ws.iplatformid, ws.ccode, vh.cvhost as curl, "
			+ "CASE WHEN vh.ilanguageid IS NOT NULL THEN vh.ilanguageid ELSE ws.ilanguageid END, "
			+ "CASE WHEN vh.icurrencyid IS NOT NULL THEN vh.icurrencyid ELSE ws.icurrencyid END, "
			+ "vh.ccreateuser, vh.dcreatedate,"
			+ "ws.bfallback, ws.idefaultshippingcountry, ws.idefaultshippingstorage "
			+ "FROM t_website ws "
			+ "INNER JOIN t_vhost vh ON ws.iid = vh.iwebsiteid "
			+ "WHERE cvhost=#{0}")
	Website findByHostname(String hostname);

	@Select("select ws.* from t_website ws "
			+ "inner join t_country_website cws on cws.iwebsiteid = ws.iid and cws.iplatformid = ws.iplatformid "
			+ "inner join t_country c on cws.icountryid = c.iid "
			+ "where c.cshortname = #{0} and cws.iplatformid = #{1}")
	Website findCountryDefault(String countryCode, Integer platformId);

	@Select("select * from t_website where bfallback = true limit 1")
	Website selectDefaultWebsite();

	List<Website> getWebsitesByWebsiteIds(
			@Param("list") List<Integer> websiteIds);
}