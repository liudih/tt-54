package services.product.inventory;

import javax.inject.Inject;

import mapper.product.inventory.InventoryHistoryMapper;
import dto.product.InventoryHistory;

public class InventoryUpdateDao implements IInventoryUpdateDao {
	@Inject
	InventoryHistoryMapper inventoryMapper;

	@Override
	public int insert(InventoryHistory ih) {
		return inventoryMapper.insert(ih);
	}

	@Override
	public int updateEnabled(String remark, Integer siteId, String listingId,
			Boolean enabled) {
		return inventoryMapper
				.updateEnabled(remark, siteId, listingId, enabled);
	}

}
