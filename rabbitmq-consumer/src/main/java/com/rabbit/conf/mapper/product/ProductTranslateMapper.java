package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.product.ProductTranslate;

public interface ProductTranslateMapper {
	int deleteByIid(Integer iid);

	int addProductTranslate(ProductTranslate record);

	int addSelectiveProductTranslate(ProductTranslate record);

	int alterSelectiveProductTranslate(ProductTranslate record);

	int alterProductTranslate(ProductTranslate record);

	int addProductTranslateList(List<ProductTranslate> list);

	@Delete("delete from t_product_translate where iid = #{0} and clistingid=#{1}")
	int deleteByIdvalidListingId(Integer id, String listingId);

	int alterByListingIdAndLuanguage(ProductTranslate recorde);

	@Delete("delete from t_product_translate where clistingid=#{0} ")
	int deleteByListingId(String listingId);

	@Select("select ilanguageid from t_product_translate where clistingid=#{0} ")
	List<ProductTranslate> getLanguageIdByListingid(String clisting);

	@Select("select * from t_product_translate where clistingid=#{0} ")
	List<ProductTranslate> getProductTranslatesByListingid(String clisting);

	@Select("select ctitle from t_product_translate where clistingid=#{0} and ilanguageid={1} limit 1 ")
	String getTitleByListingid(String clisting, Integer lan);

	List<ProductTranslate> getTitleByClistings(
			@Param("list") List<String> clistings);

	List<ProductTranslate> getTitleByClistingsAndLanguage(Integer languageId,
			@Param("list") List<String> clistings);

	ProductTranslate getProductTranslateByIid(Integer iid);

	@Select("select * from t_product_translate where clistingid = #{0} and ilanguageid = #{1} limit 1")
	ProductTranslate getTranslateByListingidAndLanguage(String listingid,
			Integer language);

	@Select({
			"<script>",
			"select ctitle,clistingid ,cdescription from t_product_translate ",
			"where ilanguageid = #{languageid} and  cListingid in",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	List<ProductTranslate> getProductTranslateByListingIdsAndLanuageId(
			@Param("list") List<String> listingids,
			@Param("languageid") Integer languageid);
	
	@Select("select * from t_product_translate where csku = #{0}")
	List<ProductTranslate> getProductTranslateBySku(String sku);

}