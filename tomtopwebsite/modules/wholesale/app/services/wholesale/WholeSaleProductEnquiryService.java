package services.wholesale;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dao.wholesale.IWholeSaleProductEnquiryDao;
import entity.wholesale.WholeSaleProduct;

public class WholeSaleProductEnquiryService {
	@Inject
	IWholeSaleProductEnquiryDao enquiryDao;

	public List<WholeSaleProduct> getWholeSaleProductsByEmail(String email,
			Integer websiteId) {
		return enquiryDao.getWholeSaleProductsByEmail(email, websiteId);
	}

	public WholeSaleProduct getWholeSaleProductsByEmailAndSkuAndWebsite(
			String email, Integer websiteId, String sku) {
		return enquiryDao.getWholeSaleProductsByEmailAndSkuAndWebsite(email,
				websiteId, sku);
	}

	public List<WholeSaleProduct> getByIDs(List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
			return Lists.newArrayList();
		}
		return enquiryDao.getByIDs(ids);
	}
}
