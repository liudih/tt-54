package dao.product;

import dao.IProductEnquiryDao;

public interface IInventoryHistoryEnquiryDao extends IProductEnquiryDao {

	public Integer getQty(String listingid);

}
