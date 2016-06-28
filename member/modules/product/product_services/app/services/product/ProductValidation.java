package services.product;

import java.util.List;

import javax.inject.Inject;

import mapper.base.WebsiteMapper;
import mapper.product.CategoryWebsiteMapper;
import mapper.product.ProductBaseMapper;
import mapper.product.ProductMultiattributeSkuMapper;
import services.base.StorageService;

import com.website.dto.product.MultiProduct;
import com.website.dto.product.Product;
import com.website.dto.product.TranslateItem;

import dao.product.IProductUrlEnquiryDao;
import dto.product.ProductBase;

public class ProductValidation {

	@Inject
	CategoryWebsiteMapper categoryWebsiteMapper;
	@Inject
	WebsiteMapper websiteMapper;
	@Inject
	ProductBaseMapper productBaseMapper;
	@Inject
	mapper.product.AttributeMapper attributeMapper;
	@Inject
	ProductMultiattributeSkuMapper productMultiattributeSkuMapper;
	@Inject
	StorageService storageEnquiryService;
	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;

	public boolean valStatus(String listingId) {
		Integer status = productBaseMapper
				.getProductStatusByListingId(listingId);
		if (null == status || 1 != status) {
			return false;
		}
		return true;
	}

	public String valProduct(Product pitem) {

		if (pitem.getWebsiteId() == null
				|| websiteMapper.selectByPrimaryKey(pitem.getWebsiteId()) == null) {
			return " invalid website id! " + System.lineSeparator();
		}

		if (pitem.getSku() == null) {
			return " invalid SkU!" + System.lineSeparator();
		}
		List<ProductBase> pbases = productBaseMapper
				.getProductBaseBySkuAndWebsiteId(pitem.getSku(),
						pitem.getWebsiteId());
		if (null != pbases && pbases.size() > 0) {
			return "duplicate SKU: " + pitem.getSku() + System.lineSeparator();
		}

		if (pitem.getItems() == null || pitem.getItems().size() == 0) {
			return " lack of Desc!" + System.lineSeparator();
		}

		String result = "";
		if (pitem.getCategoryIds() != null) {
			for (Integer cid : pitem.getCategoryIds())
				if (null == categoryWebsiteMapper.getPlatformCategories(cid,
						pitem.getWebsiteId())) {
					result += " invalid Category id: " + cid
							+ System.lineSeparator();
					break;
				}
		}
		/*
		 * else if (pitem.getCategoryIds() == null) { result +=
		 * " invalid Category id! " + System.lineSeparator(); }
		 */

		if (pitem.getPrice() == null || pitem.getPrice() <= 0) {
			result += " invalid Price!" + System.lineSeparator();
		}

		if (pitem.getWeight() == null || pitem.getWeight() <= 0) {
			result += " invalid weight!" + System.lineSeparator();
		}

		if (pitem.getQty() == null || pitem.getQty() <= 0) {
			result += " invalid qty!" + System.lineSeparator();
		}

		if (pitem.getStorages() == null || pitem.getStorages().size() == 0) {
			result += " lack of storages! " + System.lineSeparator();
		} else {
			for (Integer sid : pitem.getStorages()) {
				if (null == storageEnquiryService.getStorageForStorageId(sid)) {
					result += " invalid storage Id: " + sid
							+ System.lineSeparator();
					break;
				}
			}
		}

		if (pitem instanceof MultiProduct) {
			result += this.valMutilProduct((MultiProduct) pitem);
		}

		if (pitem.getItems() != null) {
			result += this
					.valProductUrl(pitem.getItems(), pitem.getWebsiteId());
		}
		return result;
	}

	private String valMutilProduct(MultiProduct mitilItem) {
		String result = "";
		if (mitilItem.getParentSku() == null) {
			result += "invalid parentSku! " + System.lineSeparator();
		}/* else {
			if (null != productMultiattributeSkuMapper.select(mitilItem
					.getSku())) {
				return "duplicate childSku: " + mitilItem.getSku()
						+ System.lineSeparator();
			}
		}*/
		return result;
	}

	// ~ 多属性产品的属性值要存在品类的属性
	// ~ 输入的属性是否重复
	// ~ 子sku 是否重复

	// ~ languageid not exist;
	private String valProductUrl(List<TranslateItem> items, Integer websiteid) {
		String result = "";
		try {
			for (TranslateItem item : items) {
				if (null != item.getUrl()
						&& this.productUrlEnquityDao.getProductUrlByUrl(
								item.getUrl(), websiteid, item.getLanguageId()) != null) {
					result = "duplicate Url" + System.lineSeparator();
				}
			}
		} catch (Exception ex) {
			result += "duplicate Url!" + ex.getMessage()
					+ System.lineSeparator();
		}
		return result;
	}
}
