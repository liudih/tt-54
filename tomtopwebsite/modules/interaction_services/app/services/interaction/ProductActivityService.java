package services.interaction;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import valueobjects.base.Page;

import com.google.api.client.util.Lists;

import dao.product.IProductActivityRelationEnquiryDao;
import dao.product.IProductActivityRelationUpdateDao;
import dto.product.ProductActivityRelation;

public class ProductActivityService {

	@Inject
	IProductActivityRelationEnquiryDao activityenquiryDao;

	@Inject
	IProductActivityRelationUpdateDao activityupdateDao;

	public Page<ProductActivityRelation> getProductByDate(Date date,
			int pageSize, int page) {
		int pageIndex = (page - 1) * pageSize;
		List<ProductActivityRelation> relations = activityenquiryDao
				.getProductByDate(date,pageSize, pageIndex);
		if (relations != null) {
			int count = activityenquiryDao.getCount(date);
			return new Page<ProductActivityRelation>(relations, count, page,
					pageSize);
		}
		return new Page<ProductActivityRelation>(Lists.newArrayList(), 0, page,
				pageSize);
	}

	public int addProductRelation(Map<String, Object> param) {
		return activityupdateDao.addProductRelation(param);
	}
}
