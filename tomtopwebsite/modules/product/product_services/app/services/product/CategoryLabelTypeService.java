package services.product;

import java.util.List;

import javax.inject.Inject;

import mapper.product.CategoryLabelTypeMapper;
import dto.product.CategoryLabelType;

public class CategoryLabelTypeService {
	@Inject
	CategoryLabelTypeMapper categoryLabelTypeMapper;
	
	public List<CategoryLabelType> getAllCategoryLabelTypes() {
		return categoryLabelTypeMapper.getAllCategoryLabelTypes();
	}
}
