package services.product.inventory;

import javax.inject.Inject;

import dao.product.IProductBaseEnquiryDao;

public class InventoryEnquiryService implements IInventoryEnquiryService {
	@Inject
	IInventoryEnquiryDao inventoryEnquiryDao;
	@Inject
	IProductBaseEnquiryDao productBaseEnquityDao;

	/* (non-Javadoc)
	 * @see services.product.inventory.IInventoryEnquiryService#getInventoryByListingID(java.lang.String)
	 */
	@Override
	public int getInventoryByListingID(String listingID) {
		return inventoryEnquiryDao.getInventorySumByListingID(listingID);
	}

	/* (non-Javadoc)
	 * @see services.product.inventory.IInventoryEnquiryService#checkInventory(java.lang.String, java.lang.Integer)
	 */
	@Override
	public boolean checkInventory(String listingID, Integer qty) {
		int inv = productBaseEnquityDao.getProductQtyByListingID(listingID);
		boolean res = false;
		if (null == qty && inv > 0) {
			res = true;
		} else if (null != qty && inv >= qty) {
			res = true;
		}
		return res;
	}
}
