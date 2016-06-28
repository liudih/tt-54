package com.rabbit.dao.daoImp.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.inventory.InventoryHistoryMapper;
import com.rabbit.dao.idao.product.IInventoryHistoryUpdateDao;
import com.rabbit.dto.product.InventoryHistory;
@Component
public class InventoryHistoryUpdateDao implements IInventoryHistoryUpdateDao {

	@Autowired
	InventoryHistoryMapper inventoryHistoryMapper;

	@Override
	public Integer add(InventoryHistory history) {
		return inventoryHistoryMapper.insert(history);
	}

}
