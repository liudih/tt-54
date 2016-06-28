package mapper.attribute;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.AttributeKeyItem;
import dto.AttributeValueItem;


public interface AttributeItemMapper {
	@Select(" select * from t_attribute_key_name")
	List<AttributeKeyItem> getAllAttributeKey();
	
	@Select("select vname.ikeyid as iid, vname.ilanguageid, vname.ivalueid, vname.cvaluename "
		  + "from t_attribute_value v "
		  + "inner join t_attribute_value_name vname on vname.ivalueid = v.ivalueid and vname.ilanguageid = #{1} "
		  + "where v.ikeyid = #{0}  ")
	List<AttributeValueItem> getAllAttributeValueByKeyIdAndLanguageId(Integer ikeyid, Integer languageid);
	
	@Select("select * " 
		  + "from t_attribute_key_name key "
		  + "where key.ikeyid = #{0} and key.ilanguageid = #{1}")
	AttributeKeyItem getAllAttributeKeyByKeyIdAndLanguageId(Integer ikeyid, Integer languageid);
	
	@Select(" select * from t_attribute_key_name where ilanguageid = #{0}")
	List<AttributeKeyItem> getAllAttributeKeyByLanguageId(Integer languageid);
	
	@Select("select * " 
			  + "from t_attribute_key_name key "
			  + "where key.ikeyid = #{0}")
	List<AttributeKeyItem> getAllAttributeKeyByKeyId(Integer ikeyid);

	@Select(" select * from t_attribute_key_name where ilanguageid = #{0} and ikeyid in (select iattributekeyid from "
			+ "t_category_filter_attribute where icategoryid = #{1})")
	List<AttributeKeyItem> getAllAttributeKeyByLanguageIdAndCategoryId(
			Integer ilanguageid, Integer categoryId);
}
