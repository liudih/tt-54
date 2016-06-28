package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dao.idao.IProductUpdateDao;
import com.rabbit.dto.product.ProductSellingPoints;

public interface IProductSellingPointsUpdateDao extends IProductUpdateDao {

	int addBatch(List<ProductSellingPoints> list);

	int deleteByListingId(String listingId);

	int deleteByListingIdAndLanguageId(String listingId, int languageId);

}
