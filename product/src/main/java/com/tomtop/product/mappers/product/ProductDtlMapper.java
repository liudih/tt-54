package com.tomtop.product.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.ProductAttributeDto;
import com.tomtop.product.models.dto.ProductBase;
import com.tomtop.product.models.dto.ProductImageUrlDto;
import com.tomtop.product.models.dto.ProductSeoDto;

public interface ProductDtlMapper {

	/**
	 * 查询商品详情的基本
	 * @param listingID
	 * @param langId
	 * 
	 * @author renyy
	 */
	@Select("select tbase.clistingid,iwebsiteid,ilanguageid,tbase.csku,istatus,dnewformdate,dnewtodate,bspecial "
			+ ",cvideoaddress,iqty,fprice,fcostprice,ctitle,cdescription,cshortdescription,ckeyword "
			+ ",cmetatitle,cmetakeyword,cmetadescription "
			+ ",bmultiattribute,cparentsku,bvisible,bpulish,bmain FROM t_product_base tbase inner join t_product_translate "
			+ "translate on tbase.clistingid=translate.clistingid where tbase.clistingid=#{0} and ilanguageid=#{1} and tbase.iwebsiteid=#{2} ")
	ProductBase getProductBaseByListingId(String listingID, Integer langId,Integer siteId);
	
	/**
	 * 查询商品详情其他属性产品
	 * @param parentSku
	 * @param listingID
	 * @param langId
	 * 
	 * @author renyy
	 */
	List<ProductBase> getProductBaseByParentSku(String listingID,Integer langId,String parentSku,Integer siteId);
	
	/**
	 * 查询商品属性
	 * @param listingIds
	 * @param langId
	 * 
	 * @author renyy
	 */
	@Select({
		"<script>",
	    "select pem.clistingid,pem.csku,ivalue,tkey.ikeyid,tvalue.ivalueid,tkey.ckeyname,tvalue.cvaluename from t_product_entity_map pem ",
	    "left join t_attribute_key_name tkey on pem.ikey=tkey.ikeyid ",
	    "left join t_attribute_value_name tvalue on pem.ivalue=tvalue.ivalueid ",
	    "where tkey.ilanguageid=#{langId} and tvalue.ilanguageid=#{langId} and pem.iwebsiteid=#{siteId} and ",
		"pem.clistingid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
		"</script>" })
		List<ProductAttributeDto> getProductAttributeDtoByListingIds(
		@Param("list") List<String> listingIds,@Param("langId") Integer langId,@Param("siteId") Integer siteId);
	
	/**
	 * 查询商品的图片
	 * @param listingIds
	 * @param langId
	 * 
	 * @author renyy
	 */
	@Select({
		"<script>",
		"select iid,clistingid,csku,cimageurl,iorder,bthumbnail,bsmallimage,bbaseimage",
		"from t_product_image where bshowondetails=true and ",
		"clistingid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
		"</script>" })
		List<ProductImageUrlDto> getProductImageUrlDtoByListingIds(
		@Param("list") List<String> listingIds,@Param("langId") Integer langId);
	
	/**
	 * 查询商品的SEO
	 * @param listingIds
	 * @param langId
	 * 
	 * @author renyy
	 */
	@Select("select ckeyword,cmetatitle,cmetakeyword from t_product_translate "
			+ "where clistingid=#{0} and ilanguageid=#{1}")
		ProductSeoDto getProductSeoDto(String listingID,Integer langId);
	
	/**
	 * 查询商品的Desc
	 * @param listingIds
	 * @param langId
	 * 
	 * @author renyy
	 */
	@Select("select cdescription from t_product_translate "
			+ "where clistingid=#{0} and ilanguageid=#{1}")
		String getProductDescByListingId(String listingID,Integer langId);
	
	/**
	 * 查询商品基本信息
	 * @param listingIDs
	 * @param langId
	 * 
	 * @author renyy
	 */
	@Select("<script>select tbase.clistingid,iwebsiteid,ilanguageid,tbase.csku,istatus,dnewformdate,dnewtodate,bspecial "
			+ ",cvideoaddress,iqty,fprice,fcostprice,ctitle,cdescription,cshortdescription,ckeyword "
			+ ",cmetatitle,cmetakeyword,cmetadescription "
			+ ",bmultiattribute,cparentsku,bvisible,bpulish,bmain FROM t_product_base tbase inner join t_product_translate "
			+ "translate on tbase.clistingid=translate.clistingid where ilanguageid=#{langId} and tbase.iwebsiteid=#{siteId} and tbase.clistingid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach></script>")
	List<ProductBase> getProductBaseByListingIds(@Param("list") List<String> listingIDs,@Param("langId")  Integer langId,@Param("siteId") Integer siteId);
	
	/**
	 * 查询商品的图片为主图的
	 * @param listingIds
	 * @param langId
	 * 
	 * @author renyy
	 */
	@Select({
		"<script>",
		"select iid,clistingid,csku,cimageurl,iorder,bthumbnail,bsmallimage,bbaseimage",
		"from t_product_image where bshowondetails=true and bbaseimage=true and ",
		"clistingid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
		"</script>" })
		List<ProductImageUrlDto> getProductImageUrlDtoByListingIdsIsMain(
		@Param("list") List<String> listingIds,@Param("langId") Integer langId);
	
	/**
	 * 查询商品的库存
	 * @param listingIds
	 * 
	 * @author renyy
	 */
	@Select("select iqty from t_product_base where clistingid=#{0}")
		Integer getProductQtyByListingId(String listingID);
}
