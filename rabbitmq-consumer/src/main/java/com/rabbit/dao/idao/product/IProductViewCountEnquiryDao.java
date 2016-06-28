package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dao.idao.IProductEnquiryDao;
import com.rabbit.dto.valueobjects.product.ProductViewCount;

public interface IProductViewCountEnquiryDao extends IProductEnquiryDao {

	List<ProductViewCount> getViewCountListByListingIds(List<String> listingIds);
}
