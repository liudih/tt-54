package com.rabbit.services.iservice.product;

import java.util.List;

import com.rabbit.dto.product.CategoryWebsiteWithName;
import com.rabbit.dto.transfer.category.WebsiteCategory;

public interface ICategoryEnquiryService {
	public List<WebsiteCategory> getAllCategories(int siteid, int languageid);
	
	public List<CategoryWebsiteWithName> getCategoriesByCategoryIds(
			List<Integer> ids, int languageid, int websiteid);
}
