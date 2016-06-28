package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.product.CategoryLabelType;

public interface CategoryLabelTypeMapper {
    @Select("select * from t_category_label_type ")
    List<CategoryLabelType> getAllCategoryLabelTypes();
}