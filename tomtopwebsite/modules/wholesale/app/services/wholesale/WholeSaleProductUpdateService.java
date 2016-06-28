package services.wholesale;

import java.util.List;

import com.google.inject.Inject;

import dao.wholesale.IWholeSaleProductEnquiryDao;
import dao.wholesale.IWholeSaleProductUpdateDao;
import entity.wholesale.WholeSaleProduct;

public class WholeSaleProductUpdateService {
	@Inject
	IWholeSaleProductUpdateDao updateDao;

	@Inject
	IWholeSaleProductEnquiryDao enquirDao;

	public boolean deleteByIid(Integer iid, String email) {
		return updateDao.deleteByIid(iid, email) > 0;
	}

	public boolean batchDeleteByIid(List<Integer> ids, String email) {
		return updateDao.batchDeleteByIid(ids, email) > 0;
	}

	public boolean addWholeSaleProduct(WholeSaleProduct record) {
		WholeSaleProduct wholeasleProduct = enquirDao
				.getWholeSaleProductsByEmailAndSkuAndWebsite(
						record.getCemail(), record.getIwebsiteid(),
						record.getCsku());
		if (null != wholeasleProduct) {
			Integer iqty = wholeasleProduct.getIqty() + record.getIqty();
			return updateQtyByIid(wholeasleProduct.getIid(), iqty);
		}
		return updateDao.addWholeSaleProduct(record) > 0;
	}

	public boolean updateQtyByIid(Integer iid, Integer qty) {
		return updateDao.updateQtyByIid(iid, qty) > 0;
	}
}
