package services.wholesale;

import java.util.List;

import javax.inject.Inject;

import dao.wholesale.IWholeSaleDiscountLevelEnquiryDao;
import entity.wholesale.WholeSaleDiscountLevel;

public class WholeSaleDiscountLevelEnquiryService {
	@Inject
	IWholeSaleDiscountLevelEnquiryDao enquiryDao;

	public WholeSaleDiscountLevel getByID(int id) {
		return enquiryDao.getByID(id);
	}

	public WholeSaleDiscountLevel getBySiteAndPrice(int siteID, double price) {
		return enquiryDao.getBySiteAndPrice(siteID, price);
	}

	public List<WholeSaleDiscountLevel> getBySite(int siteID) {
		return enquiryDao.getBySite(siteID);
	}
}
