package service.google.category;

import java.util.List;

import javax.inject.Inject;

import mapper.google.category.GoogleAttributeMapper;
import mapper.google.category.GoogleCategoryMapMapper;
import valueobjects.base.Page;
import dto.product.google.category.AddSkuForm;
import dto.product.google.category.GoogleAttributForm;
import dto.product.google.category.GoogleCategoryMapper;

public class GoogleCategoryService {
	@Inject
	GoogleCategoryMapMapper googleCategoryMapMapper;

	@Inject
	GoogleAttributeMapper attributeMapper;

	public Page<GoogleCategoryMapper> getCategory(int page, int pageSize,
			AddSkuForm addSkuForm) {
		List<GoogleCategoryMapper> categoryMappers = googleCategoryMapMapper
				.getSkuFilter(page, pageSize, addSkuForm.getIcategory(),
						addSkuForm.getCsku());
		int total = googleCategoryMapMapper.getTotal(addSkuForm.getIcategory(),
				addSkuForm.getCsku());
		return new Page<GoogleCategoryMapper>(categoryMappers, total, page,
				pageSize);
	}

	public Page<GoogleAttributForm> getAttr(int page, int pageSize,
			AddSkuForm addSkuForm) {
		List<GoogleAttributForm> attList = attributeMapper.getAttributeByGid(
				page, pageSize, addSkuForm.getCname());
		int total;
		if (null != attList && attList.size() != 0) {
			total = attributeMapper.getCount(addSkuForm.getCname(), attList
					.get(0).getIcategoryid());
		} else {
			total = attributeMapper.getTCount();
		}
		return new Page<GoogleAttributForm>(attList, total, page, pageSize);
	}
}
