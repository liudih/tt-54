package services.product.inventory;

import javax.inject.Inject;

import mapper.product.inventory.InventoryHistoryMapper;

public class InventoryEnquiryDao implements IInventoryEnquiryDao {
	@Inject
	InventoryHistoryMapper inventoryMapper;

	@Override
	public int getInventorySumByListingID(String listingID) {
		Integer i = inventoryMapper.sumByListingID(listingID);
		return i != null ? i : 0;
	}

}
