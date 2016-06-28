package dao.product;

import dao.IProductUpdateDao;
import dto.product.InventoryHistory;

public interface IInventoryHistoryUpdateDao extends IProductUpdateDao{

	public Integer add(InventoryHistory history);
	
}
