package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.ProductAttributeItem;
import com.rabbit.dto.product.ProductAttributeMessage;
import com.rabbit.dto.product.ProductEntityMap;

public interface ProductEntityMapMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(ProductEntityMap record);

	int batchInsert(List<ProductEntityMap> list);

	int insertSelective(ProductEntityMap record);

	@Select("SELECT tentity.iid,tentity.clistingid,tentity.csku,tentity.bshow, "
		+ "max(case when tkey.ckeyname is null then key_de.ckeyname else tkey.ckeyname end) ckeyname, "
		+ "max(case when tvalue.cvaluename is null then value_de.cvaluename else tvalue.cvaluename end) cvaluename "
		+ "FROM  t_product_entity_map tentity "
		+ "left join t_attribute_key_name tkey on tentity.ikey=tkey.ikeyid and tkey.ilanguageid=#{1} "
		+ "left join t_attribute_value_name tvalue on tentity.ivalue= tvalue.ivalueid and tvalue.ilanguageid=#{1} "
		+ "left join t_attribute_key_name key_de on tentity.ikey=key_de.ikeyid and key_de.ilanguageid=1 "
		+ "left join t_attribute_value_name value_de on tentity.ivalue= value_de.ivalueid and value_de.ilanguageid=1 "
		+ "WHERE tentity.bshow = true and tentity.clistingid = #{0} "
		+ "group by tentity.iid,tentity.clistingid,tentity.csku,tentity.bshow "
		)
	List<ProductEntityMap> getProductEntityMapByListingId(String listingID,
			Integer langid);

	List<ProductEntityMap> getProductEntityMapListByListingIds(
			@Param("list") List<String> listingIds, Integer languageid,Integer websiteId);

	@Delete("delete from t_product_entity_map where clistingid=#{0} ")
	int deleteByListingId(String listingId);

	@Select("SELECT * FROM  t_product_entity_map tentity "
			+ "WHERE clistingid = #{0} and tentity.bshow = true")
	List<ProductEntityMap> getProductEntityMapByListingid(String listingID);

	@Select("<script>"
			+ "SELECT * FROM  t_product_entity_map tentity "
			+ "WHERE tentity.bshow = true and clistingid IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "</script>")
	List<ProductEntityMap> getProductEntityMapByListingids(
			@Param("list") List<String> listingIDs);

	@Delete("delete from t_product_entity_map where clistingid =#{0} and ikey = #{1}")
	int deleteByListingIdAndKeyId(String listingId, Integer keyId);

	@Select("<script>"
			+ "SELECT tvalue.ilanguageid as languageId, clistingid as listingId,csku as sku,ikey as keyId,ivalue as valueId,iwebsiteid as websiteId,bshow as visible,multiattribute,ckeyname as key,cvaluename as value  FROM  t_product_entity_map tentity "
			+ "inner join t_attribute_key_name tkey on tentity.ikey=tkey.ikeyid  "
			+ "inner join t_attribute_value_name  tvalue on tentity.ivalue= tvalue.ivalueid and tkey.ilanguageid=tvalue.ilanguageid "
			+ "WHERE tentity.bshow = true and clistingid IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "</script>")
	List<ProductAttributeItem> getProductAttributeByListingids(
			@Param("list") List<String> listingIDs);

	@Select("select ikey as keyId,ivalue as valueId,iwebsiteid as websiteId,bshow as visible,multiattribute,ckeyname as key,cvaluename as value  FROM  t_product_entity_map tentity "
			+ "inner join t_attribute_key_name tkey on tentity.ikey=tkey.ikeyid and tkey.ilanguageid=#{1} "
			+ "inner join t_attribute_value_name  tvalue on tentity.ivalue= tvalue.ivalueid and tkey.ilanguageid=tvalue.ilanguageid and tvalue.ilanguageid=#{1}"
			+ "where tentity.bshow = true and clistingid = #{0} ")
	List<ProductAttributeItem> getProductItemsByListingAndLanguage(
			String clistingid, int languageid);

	@Select("select tentity.clistingid as clistingid, tentity.csku as csku, min(tvalue.cvaluename) as cvaluename, tkey.ckeyname as ckeyname FROM t_product_entity_map tentity "
			+ "inner join t_attribute_key_name tkey on tentity.ikey=tkey.ikeyid and tkey.ilanguageid=#{1} and tkey.ckeyname = #{2} "
			+ "inner join t_attribute_value_name tvalue on tentity.ivalue= tvalue.ivalueid and tvalue.ilanguageid = #{1} "
			+ "where clistingid in (select distinct m.clistingid "
			+ "from t_product_entity_map m "
			+ "where m.bshow = true and m.csku in ( "
			+ "select distinct ts.csku FROM t_product_base tb2 "
			+ "inner join t_product_multiattribute_sku ts on ts.cparentsku = tb2.cparentsku "
			+ "where tb2.clistingid = #{0} and tb2.iwebsiteid = #{3} "
			+ ") and m.iwebsiteid = #{3}) group by tentity.clistingid, tentity.csku, tkey.ckeyname")
	List<ProductEntityMap> getEntityMaps(String listingID, Integer langid,
			String keyname, Integer websiteid);

	@Select("<script>"
			+ "select bas.cparentsku as cparentsku, tentity.clistingid as clistingid, tentity.csku as csku, min(tvalue.cvaluename) as cvaluename, tkey.ckeyname as ckeyname "
			+ "from t_product_entity_map tentity "
			+ "inner join t_product_base bas on bas.clistingid = tentity.clistingid "
			+ "inner join t_attribute_key_name tkey on tentity.ikey=tkey.ikeyid and tkey.ilanguageid=#{1} and tkey.ckeyname = #{2} "
			+ "inner join t_attribute_value_name tvalue on tentity.ivalue= tvalue.ivalueid and tvalue.ilanguageid = #{1} "
			+ "where tentity.bshow = true and tentity.clistingid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "group by tentity.clistingid, tentity.csku, tkey.ckeyname, bas.cparentsku </script>")
	List<ProductAttributeMessage> getProductAttributeMessages(
			@Param("list") List<String> listingID, Integer langid, String keyname,
			Integer websiteid);
}