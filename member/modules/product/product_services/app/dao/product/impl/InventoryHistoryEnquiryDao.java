package dao.product.impl;

import javax.inject.Inject;

import mapper.product.inventory.InventoryHistoryMapper;
import dao.product.IInventoryHistoryEnquiryDao;

public class InventoryHistoryEnquiryDao implements IInventoryHistoryEnquiryDao {

	@Inject
	InventoryHistoryMapper inventoryHistoryMapper;

	@Override
	public Integer getQty(String listingid) {
		return inventoryHistoryMapper.sumByListingID(listingid);
	}
}
