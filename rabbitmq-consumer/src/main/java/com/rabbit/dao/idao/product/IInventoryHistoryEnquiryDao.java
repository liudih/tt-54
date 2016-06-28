package com.rabbit.dao.idao.product;

import com.rabbit.dao.idao.IProductEnquiryDao;

public interface IInventoryHistoryEnquiryDao extends IProductEnquiryDao {

	public Integer getQty(String listingid);

}
