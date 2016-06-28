package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import  com.rabbit.dto.product.ProductUrl;
import  com.rabbit.dto.product.ProductUrlWithSmallImage;

public interface ProductUrlMapper {

	int addProductUrl(ProductUrl record);

	int addProductUrlList(@Param("list") List<ProductUrl> list);

	@Delete("delete from t_product_url where clistingid=#{0} ")
	int deleteByListingId(String listingId);

	@Select("select * from t_product_url where cListingid = #{0} and ilanguageid = #{1} limit 1")
	ProductUrl getProductUrlsByListingId(String listingId, Integer language);

	List<ProductUrlWithSmallImage> getProductUrlsByListingIds(
			@Param("list") List<String> listingIds,
			@Param("language") int language);

	// XXX limit 1 to be removed after unique constraint is effective
	@Select("select * from t_product_url where curl = #{0} and iwebsiteid=#{1} order by ilanguageid  limit 1")
	ProductUrl getProductUrlByUrl(String url, Integer websiteid, int languageid);

	List<ProductUrl> getProductUrlByClistingids(
			@Param("list") List<String> clistingids);

	@Select("select * from t_product_url where cListingid = #{0}")
	List<ProductUrl> getProductUrlByListingId(String clistingid);

	@Select({
			"<script>",
			"select curl,clistingid ,csku from t_product_url ",
			"where ilanguageid = #{languageid} and  cListingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	List<ProductUrl> getProductUrlByListingIdsAndLanguageId(
			@Param("list") List<String> listingids,
			@Param("languageid") Integer languageid);

	@Select("select * from t_product_url where curl = #{0} and ilanguageid = #{1}")
	ProductUrl getBaseUrlBylanguageAndUrl(String url, int language);

	@Select("select * from t_product_url where csku = #{0} and ilanguageid = #{1}")
	ProductUrl getProductBySkuAndLanguage(String sku, int languageid);
}