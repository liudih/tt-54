package com.rabbit.dao.idao.product;

import java.util.Date;
import java.util.List;

import com.rabbit.dao.idao.IProductUpdateDao;
import com.rabbit.dto.product.ProductSalePrice;

public interface IProductSalePriceUpdateDao extends IProductUpdateDao {

	int addProductSalePrice(ProductSalePrice record);

	int updateByPrimaryKeySelective(ProductSalePrice record);

	int addBatch(List<ProductSalePrice> list);
	
	int deleteProductCurrentSalePrice(String listingId);
	
	int deleteProductSalePriceByDate(String listingId,
			Date start, Date end);
}
