package mapper.attribute;

import org.apache.ibatis.annotations.Update;

import entity.attribute.AttributeValueName;

public interface AttributeValueNameMapper {
    int deleteByPrimaryKey(Integer ikeyid);

    int insert(AttributeValueName record);

    int insertSelective(AttributeValueName record);

    AttributeValueName selectByPrimaryKey(Integer ikeyid);

    int updateByPrimaryKeySelective(AttributeValueName record);

    int updateByPrimaryKey(AttributeValueName record);
    
    @Update("UPDATE t_attribute_value_name SET cvaluename = #{1} WHERE ikeyid= #{0}")
	boolean updateAttributeValue(Integer iid, String cvaluename);
}