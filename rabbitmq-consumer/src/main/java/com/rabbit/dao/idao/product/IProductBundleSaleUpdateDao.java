package com.rabbit.dao.idao.product;

import com.rabbit.dao.idao.IProductUpdateDao;
import com.rabbit.dto.product.ProductBundleSale;

public interface IProductBundleSaleUpdateDao extends IProductUpdateDao {
	public int deleteAuto();
	
	public int alterAutoBundleSaleVisible();
	
	int activeBundle(String listing,String bundListing);
	
	int insert(ProductBundleSale record);
	
}
