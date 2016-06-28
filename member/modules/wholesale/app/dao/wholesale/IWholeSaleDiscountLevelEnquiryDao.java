package dao.wholesale;

import java.util.List;

import dao.IWholeSaleEnquiryDao;
import entity.wholesale.WholeSaleDiscountLevel;

public interface IWholeSaleDiscountLevelEnquiryDao extends IWholeSaleEnquiryDao {
	WholeSaleDiscountLevel getByID(int id);

	WholeSaleDiscountLevel getBySiteAndPrice(int siteID, double price);

	List<WholeSaleDiscountLevel> getWholeSaleDiscountLevelByWebSiteId(int websiteId);
	
	List<WholeSaleDiscountLevel> getBySite(int siteID);
}
