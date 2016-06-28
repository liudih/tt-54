package com.rabbit.dao.idao.product;

import com.rabbit.dao.idao.IProductUpdateDao;
import com.rabbit.dto.product.InventoryHistory;

public interface IInventoryHistoryUpdateDao extends IProductUpdateDao{

	public Integer add(InventoryHistory history);
	
}
