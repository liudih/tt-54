package com.rabbit.dao.idao.product;

import com.rabbit.dao.idao.IProductEnquiryDao;

public interface IProductBundleSaleEnquiryDao extends IProductEnquiryDao {
	public boolean isExist(String listingid,String bundleListing);

}
