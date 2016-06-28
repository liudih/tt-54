package com.rabbit.dao.daoImp.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.inventory.InventoryHistoryMapper;
import com.rabbit.dao.idao.product.IInventoryHistoryEnquiryDao;
@Component
public class InventoryHistoryEnquiryDao implements IInventoryHistoryEnquiryDao {

	@Autowired
	InventoryHistoryMapper inventoryHistoryMapper;

	@Override
	public Integer getQty(String listingid) {
		return inventoryHistoryMapper.sumByListingID(listingid);
	}
}
