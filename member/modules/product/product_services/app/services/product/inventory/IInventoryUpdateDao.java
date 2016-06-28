package services.product.inventory;

import dao.IProductEnquiryDao;
import dto.product.InventoryHistory;

public interface IInventoryUpdateDao extends IProductEnquiryDao {
	int insert(InventoryHistory ih);

	int updateEnabled(String remark, Integer siteId, String listingId,
			Boolean enabled);

}
