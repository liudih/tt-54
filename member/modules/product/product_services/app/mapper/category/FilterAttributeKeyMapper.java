package mapper.category;

import dto.category.FilterAttributeKey;

public interface FilterAttributeKeyMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(FilterAttributeKey record);

    int insertSelective(FilterAttributeKey record);

    FilterAttributeKey selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(FilterAttributeKey record);

    int updateByPrimaryKey(FilterAttributeKey record);
}