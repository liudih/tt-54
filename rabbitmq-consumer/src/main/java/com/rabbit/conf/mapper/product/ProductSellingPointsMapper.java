package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.product.ProductSellingPoints;

public interface ProductSellingPointsMapper {

	int addBatch(List<ProductSellingPoints> list);

	@Delete("delete from t_product_selling_points WHERE clistingid = #{0} ")
	int deleteByListingId(String listingId);

	@Select("SELECT ccontent FROM t_product_selling_points WHERE clistingid = #{0} AND ilanguageid = #{1}")
	List<ProductSellingPoints> getProductSellingPointsByListingIdAndLanguage(
			String listingID, int lang);

	List<ProductSellingPoints> getProductSellingPointsByListingIds(
			@Param("list") List<String> listingids, Integer lang);

	@Select("SELECT ccontent,ilanguageid FROM t_product_selling_points WHERE clistingid = #{0}")
	List<ProductSellingPoints> getProductSellingPointsByListingId(
			String listingID);

	@Select("<script>"
			+ "SELECT clistingid,ccontent,ilanguageid FROM t_product_selling_points "
			+ "WHERE clistingid IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "</script>")
	List<ProductSellingPoints> getProductSellingPointsByListingIdsOnly(
			@Param("list") List<String> listingIDs);

	@Delete("delete from t_product_selling_points WHERE clistingid = #{0} AND ilanguageid = #{1}")
	int deleteByListingIdAndLanguage(String listingId, int languageId);

}