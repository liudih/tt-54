package dao.product.impl;

import javax.inject.Inject;

import mapper.product.inventory.InventoryHistoryMapper;
import dao.product.IInventoryHistoryUpdateDao;
import dto.product.InventoryHistory;

public class InventoryHistoryUpdateDao implements IInventoryHistoryUpdateDao {

	@Inject
	InventoryHistoryMapper inventoryHistoryMapper;

	@Override
	public Integer add(InventoryHistory history) {
		return inventoryHistoryMapper.insert(history);
	}

}
