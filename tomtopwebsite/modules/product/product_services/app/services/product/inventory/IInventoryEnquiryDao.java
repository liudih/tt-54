package services.product.inventory;

import dao.IProductEnquiryDao;

public interface IInventoryEnquiryDao extends IProductEnquiryDao {
	int getInventorySumByListingID(String listingID);

}
