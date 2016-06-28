package mapper.category;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.category.FilterAttributeValue;

public interface FilterAttributeValueMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(FilterAttributeValue record);

	int insertSelective(FilterAttributeValue record);

	FilterAttributeValue selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(FilterAttributeValue record);

	int updateByPrimaryKey(FilterAttributeValue record);

	@Select(" select m.icategoryid as categoryid,m.iattributekeyid as keyid,m.iattributevalueid as valueid,"
			+ " b.ckeyname as key,a.cvaluename as value,a.ilanguageid as languageid "
			+ " from t_category_filter_attribute m  inner join t_attribute_value_name a "
			+ " on m.iattributekeyid=a.ikeyid and m.iattributevalueid=a.ivalueid  "
			+ " inner join t_attribute_key_name b on a.ikeyid=b.ikeyid and a.ilanguageid=b.ilanguageid")
	List<com.website.dto.category.CategoryAttribute> selectAll();
}