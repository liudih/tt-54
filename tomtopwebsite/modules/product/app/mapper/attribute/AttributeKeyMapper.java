package mapper.attribute;

import entity.attribute.AttributeKey;

public interface AttributeKeyMapper {
    int deleteByPrimaryKey(Integer ikeyid);

    int insert(AttributeKey record);

    int insertSelective(AttributeKey record);

    AttributeKey selectByPrimaryKey(Integer ikeyid);

    int updateByPrimaryKeySelective(AttributeKey record);

    int updateByPrimaryKey(AttributeKey record);
}