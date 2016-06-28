package mapper.google.category;

import org.apache.ibatis.annotations.Select;

import dto.product.google.category.AttributeValue;

public interface AttributeValueMapper {

	@Select("select * from t_google_attribute_value where iid = #{0}")
	AttributeValue getValueById(Integer iattributevalueid);

}
