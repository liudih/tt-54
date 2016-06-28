package services.product.inventory;

import javax.inject.Inject;

import dao.product.IProductBaseUpdateDao;
import dto.product.InventoryHistory;

public class InventoryUpdateService {
	@Inject
	IInventoryUpdateDao inventoryUpdateDao;
	@Inject
	IInventoryEnquiryService inventoryEnquiryService;
	@Inject
	IProductBaseUpdateDao productBaseUpdateDao;

	public boolean insert(String listingID, String remark, int changQty,
			int websiteID, InventoryTypeEnum type) {
		InventoryHistory ih = new InventoryHistory();
		ih.setClistingid(listingID);
		ih.setCreference(remark);
		ih.setIqty(changQty);
		ih.setIwebsiteid(websiteID);
		ih.setCtype(type.getValue());
		return insert(ih);
	}

	public boolean insert(InventoryHistory ih) {
		int sum = inventoryEnquiryService.getInventoryByListingID(ih
				.getClistingid());
		ih.setBenabled(true);
		ih.setIbeforechangeqty(sum);
		ih.setIafterchangeqty(sum + ih.getIqty());
		int i = inventoryUpdateDao.insert(ih);
		if (1 == i) {
			productBaseUpdateDao.updateQtyByListing(ih.getIafterchangeqty(),
					ih.getClistingid());
			return true;
		}
		return false;
	}

	public boolean updateEnabled(String remark, Integer siteId,
			String listingID, Boolean enabled) {
		int i = inventoryUpdateDao.updateEnabled(remark, siteId, listingID,
				enabled);
		if (1 == i) {
			int sum = inventoryEnquiryService
					.getInventoryByListingID(listingID);
			productBaseUpdateDao.updateQtyByListing(sum, listingID);
			return true;
		}
		return false;
	}
}
