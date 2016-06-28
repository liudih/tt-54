package mapper.attribute;

import org.apache.ibatis.annotations.Update;

import entity.attribute.AttributeKeyName;

public interface AttributeKeyNameMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(AttributeKeyName record);

    int insertSelective(AttributeKeyName record);

    AttributeKeyName selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(AttributeKeyName record);

    int updateByPrimaryKey(AttributeKeyName record);
    
    @Update("UPDATE t_attribute_key_name SET ckeyname = #{1} WHERE iid = #{0}")
	boolean updateAttributeKey(Integer iid, String ckeyname);
}