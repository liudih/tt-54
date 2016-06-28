package valueobjects.product.category;

import java.util.List;

import valueobjects.product.CategoryLabelBase;
import valueobjects.product.ProductBadge;
import dto.product.CategoryName;

public class CategoryClearance {
	
	CategoryName categoryName;
	
	CategoryLabelBase categoryLabelBase;
	
	List<ProductBadge> productList;

	public CategoryName getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(CategoryName categoryName) {
		this.categoryName = categoryName;
	}

	public List<ProductBadge> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductBadge> productList) {
		this.productList = productList;
	}

	public CategoryLabelBase getCategoryLabelBase() {
		return categoryLabelBase;
	}

	public void setCategoryLabelBase(CategoryLabelBase categoryLabelBase) {
		this.categoryLabelBase = categoryLabelBase;
	}
	
}
