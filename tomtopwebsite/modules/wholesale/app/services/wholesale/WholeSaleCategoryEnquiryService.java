package services.wholesale;

import java.util.List;

import play.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dao.wholesale.IWholeSaleCategoryEnquiryDao;
import entity.wholesale.WholeSaleCategory;

public class WholeSaleCategoryEnquiryService {
	@Inject
	IWholeSaleCategoryEnquiryDao wholeSaleCategoryEnquiryDao;

	public List<WholeSaleCategory> getWholeSaleCategoryByWholeSaleId(
			Integer wholeSaleId) {
		return wholeSaleCategoryEnquiryDao
				.getWholeSaleCategoryByWholeSaleId(wholeSaleId);
	};

	public List<Integer> getCategoryIdByWholeSaleId(Integer wholeSaleId) {
		List<WholeSaleCategory> wholeSaleCategorys = wholeSaleCategoryEnquiryDao
				.getWholeSaleCategoryByWholeSaleId(wholeSaleId);
		if (null != wholeSaleCategorys) {
			List<Integer> categoryIds = categoryIds = Lists.transform(
					wholeSaleCategorys, e -> {
						return e.getIcategoryid();
					});
			Logger.debug("categoryIds:{}", categoryIds);
			return categoryIds;
		}
		return null;
	};
}
