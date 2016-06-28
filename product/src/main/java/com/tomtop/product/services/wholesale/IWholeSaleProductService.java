package com.tomtop.product.services.wholesale;

import com.tomtop.product.models.bo.BaseBo;

public interface IWholeSaleProductService {

	public BaseBo addWholeSaleProduct(String email,String sku,Integer qty,Integer siteId);
}
