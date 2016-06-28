package mapper.category;

import org.apache.ibatis.annotations.Select;

import dto.category.CategoryBackgroundImages;

public interface CategoryBackgroundImagesMapper {
    int insertSelective(CategoryBackgroundImages record);

    CategoryBackgroundImages selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(CategoryBackgroundImages record);
    
    @Select("select * from t_category_backgroundimages where icategorynameid = #{0}")
    CategoryBackgroundImages getBackgroundImagesByCategoryNameId(Integer categoryNameId);
}