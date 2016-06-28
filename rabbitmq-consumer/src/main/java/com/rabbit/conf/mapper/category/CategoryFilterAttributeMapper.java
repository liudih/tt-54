package com.rabbit.conf.mapper.category;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.category.CategoryFilterAttribute;
import com.rabbit.dto.transfer.category.CategoryAttribute;

public interface CategoryFilterAttributeMapper {
	int insert(CategoryFilterAttribute record);

	int insertSelective(CategoryFilterAttribute record);

	@Select("select kname.ckeyname attributeKey, vname.cvaluename attributeValue,a.iattributekeyid attributeKeyId,a.iattributevalueid attributeValueId from t_category_filter_attribute a "
			+ "inner join t_attribute_key_name kname on a.iattributekeyid=kname.ikeyid and kname.ilanguageid=#{1} "
			+ "inner join t_attribute_value_name vname on a.iattributevalueid=vname.ivalueid and vname.ilanguageid=#{1} "
			+ "where  a.icategoryid = (select iid from t_category_base where cpath = #{0} limit 1) order by kname.ikeyid")
	List<CategoryAttribute> getAttributeList(String cpath, Integer ilanguageid);

	@Select("select a.icategoryid categoryId,kname.ckeyname attributeKey,kname.ikeyid ikeyid, vname.cvaluename attributeValue, "
			+ "kname.iid attributeKeyId, vname.ikeyid attributeValueId from t_category_filter_attribute a "
			+ "inner join t_attribute_key_name kname on a.iattributekeyid=kname.ikeyid and kname.ilanguageid=#{1} "
			+ "inner join t_attribute_value_name vname on a.iattributevalueid=vname.ivalueid and vname.ilanguageid=#{1} "
			+ "where  a.icategoryid = #{0} order by kname.ikeyid")
	List<CategoryAttribute> getAttributeListByCategoryIdAndLanguageId(
			Integer icategoryid, Integer ilanguageid);

	@Select("select vname.ikeyid "
			+ "from t_category_filter_attribute a "
			+ "inner join t_attribute_key_name kname on a.iattributekeyid=kname.ikeyid and kname.ilanguageid=#{1} "
			+ "inner join t_attribute_value_name vname on a.iattributevalueid=vname.ivalueid and vname.ilanguageid=#{1} "
			+ "where  a.icategoryid = #{0} and a.iattributekeyid = #{2}")
	List<Integer> getAttributeValueIdsByCategoryIdAndLanguageId(
			Integer icategoryid, Integer ilanguageid, Integer ikeyid);

	@Delete("delete from t_category_filter_attribute a "
			+ "where a.icategoryid = #{0} and iattributekeyid = #{1}")
	boolean deleteCategoryFilterAttribute(Integer icategoryid,
			Integer iattributekeyid);

	@Select("select * from t_category_filter_attribute "
			+ "where icategoryid = #{0} and iattributekeyid = #{1} and iattributevalueid=#{2} ")
	CategoryFilterAttribute getCategoryFilterAttribute(Integer icategoryid,
			Integer iattributekeyid, Integer valueid);
	
	@Select("select * from t_category_filter_attribute "
			+ "where icategoryid = #{0} and iattributekeyid = #{1} ")
	List<CategoryFilterAttribute> getCategoryFilterAttributes(Integer icategoryid,
			Integer iattributekeyid);
}