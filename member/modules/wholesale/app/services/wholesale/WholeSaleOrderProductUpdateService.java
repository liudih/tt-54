package services.wholesale;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import dao.wholesale.IWholeSaleOrderProductUpdateDao;
import entity.wholesale.WholeSaleOrderProduct;
import entity.wholesale.WholeSaleProduct;

public class WholeSaleOrderProductUpdateService {
	@Inject
	IWholeSaleOrderProductUpdateDao updateDao;
	@Inject
	WholeSaleProductEnquiryService productEnquiryService;

	public boolean insert(WholeSaleOrderProduct product) {
		return updateDao.insert(product) == 1;
	}

	public boolean batchInsert(List<WholeSaleOrderProduct> list) {
		return updateDao.batchInsert(list) == list.size();
	}

	public boolean saveProductToOrderProduct(List<Integer> productIDs,
			Integer orderID) {
		List<WholeSaleProduct> products = productEnquiryService
				.getByIDs(productIDs);
		return saveWholeSaleProduct(products, orderID);
	}

	public boolean saveWholeSaleProduct(List<WholeSaleProduct> products,
			Integer orderID) {
		List<WholeSaleOrderProduct> list = Lists.transform(products, p -> {
			WholeSaleOrderProduct product = new WholeSaleOrderProduct();
			try {
				BeanUtils.copyProperties(product, p);
			} catch (Exception e) {
				Logger.error("BeanUtils.copyProperties error: ", e);
				return null;
			}
			product.setIorderid(orderID);
			return product;
		});
		list = Lists.newArrayList(Collections2.filter(list, p -> p != null));
		return batchInsert(list);
	}
}
