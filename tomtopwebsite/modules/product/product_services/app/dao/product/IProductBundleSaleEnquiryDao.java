package dao.product;

import dao.IProductEnquiryDao;

public interface IProductBundleSaleEnquiryDao extends IProductEnquiryDao {
	public boolean isExist(String listingid,String bundleListing);

}
