package dao.wholesale;

import java.util.List;

import dao.IWholeSaleEnquiryDao;
import entity.wholesale.WholeSaleProduct;

public interface IWholeSaleProductEnquiryDao extends IWholeSaleEnquiryDao {
	public WholeSaleProduct getWholeSaleProductsByEmailAndSkuAndWebsite(
			String email, Integer websiteId, String sku);

	public List<WholeSaleProduct> getWholeSaleProductsByEmail(String email,
			Integer websiteId);

	public List<WholeSaleProduct> getByIDs(List<Integer> ids);
}
