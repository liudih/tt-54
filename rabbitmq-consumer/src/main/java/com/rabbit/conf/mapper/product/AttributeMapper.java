package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.category.AttributeKey;
import com.rabbit.dto.category.AttributeKeyName;
import com.rabbit.dto.category.AttributeValue;
import com.rabbit.dto.category.AttributeValueName;
import com.rabbit.dto.transfer.Attribute;
import com.rabbit.dto.valueobjects.product.ProductAttrName;

public interface AttributeMapper {

	@Select(" select  m.ikeyid as keyid,a.ilanguageid as languageid,m.ivalueid as valueid, "
			+ " a.cvaluename as value,b.ckeyname as key  from t_attribute_value m "
			+ " inner join t_attribute_value_name a on m.ivalueid=a.ivalueid "
			+ " inner join t_attribute_key_name b on m.ikeyid=b.ikeyid and a.ilanguageid=b.ilanguageid where a.ilanguageid=#{0} ")
	List<Attribute> getAllAttibutes(Integer languageid);

	@Select(" select m.ikeyid as keyid,m.ivalueid as valueid "
			+ " from t_attribute_value m "
			+ " where m.ikeyid=#{0} and m.ivalueid=#{1} ")
	Attribute select(Integer keyid, Integer valueId);

	int insertKey(AttributeKey attributeKey);

	int insertKeyName(AttributeKeyName attributeKeyName);

	@Select("select iid,ikeyid,ilanguageid,ckeyname from t_attribute_key_name where ckeyname=#{0} and ilanguageid=#{1} limit 1 ")
	AttributeKeyName selectBykeyName(String keyName, int lang);

	int insertValue(AttributeValue attributevalue);

	int insertValueName(AttributeValueName attrValueName);

	@Select("select ivalueid,ikeyid,ccreateuser,dcreatedate from t_attribute_value where keyid=#{0} and ivalueid=#{1} ")
	AttributeValue selectAttributeValueById(Integer keyid, Integer valueid);

	@Select("select a.ivalueid,m.ikeyid,a.ilanguageid,a.cvaluename from t_attribute_value m "
			+ " inner join t_attribute_value_name a on m.ivalueid=a.ivalueid "
			+ " where a.ilanguageid=#{0} and m.ikeyid=#{1} and a.cvaluename=#{2}")
	AttributeValueName selectbyAttributeValueName(Integer langid,
			Integer keyid, String valueName);

	@Select({
			"<script>",
			"select m.clistingid,a.ckeyname ",
			"from t_product_entity_map m ",
			"inner join t_attribute_key_name a on a.ikeyid=m.ikey and a.ckeyname=#{1} ",
			"where m.bshow = true and m.clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	public List<ProductAttrName> getAttrByListingIds(
			@Param("list") List<String> listingIds, String keyname);

	/*
	 * @Select(
	 * "select min(ivalueid) as id from t_attribute_value_name where cvaluename=#{0} and ilanguageid=#{1} "
	 * ) Integer getValueMinId(String valueName, int lang);
	 */

}
