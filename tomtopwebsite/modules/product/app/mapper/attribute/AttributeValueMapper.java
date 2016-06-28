package mapper.attribute;

import entity.attribute.AttributeValue;

public interface AttributeValueMapper {
    int deleteByPrimaryKey(Integer ivalueid);

    int insert(AttributeValue record);

    int insertSelective(AttributeValue record);

    AttributeValue selectByPrimaryKey(Integer ivalueid);

    int updateByPrimaryKeySelective(AttributeValue record);

    int updateByPrimaryKey(AttributeValue record);
}