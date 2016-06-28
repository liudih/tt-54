package services.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.inject.Inject;

import dao.product.IProductSalePriceEnquiryDao;
import dao.product.IProductSalePriceUpdateDao;
import dto.product.ProductSalePrice;

public class ProductSalePriceService {
	@Inject
	IProductSalePriceEnquiryDao priceEnquiryDao;

	@Inject
	IProductSalePriceUpdateDao priceUpdateDao;

	public boolean insertProductSalePrice(ProductSalePrice price) {
		List<ProductSalePrice> productSalePrices = priceEnquiryDao
				.getProductSalePriceByDate(price.getClistingid(),
						price.getDbegindate(), price.getDenddate());

		if (productSalePrices.size() <= 0) {
			return priceUpdateDao.addProductSalePrice(price) > 0;
		} else {
			// 时间存在重叠
			Date srcStartDate = price.getDbegindate();
			Date srcEndDate = price.getDenddate();
			for (ProductSalePrice productSalePrice : productSalePrices) {
				Date desStartDate = productSalePrice.getDbegindate();
				Date desEndDate = productSalePrice.getDenddate();
				if (srcStartDate.before(desStartDate)
						&& srcEndDate.after(desEndDate)) {
					productSalePrice.setDbegindate(srcEndDate);

					priceUpdateDao
							.updateByPrimaryKeySelective(productSalePrice);
				} else if (srcStartDate.after(srcStartDate)
						&& srcEndDate.after(desEndDate)) {
					productSalePrice.setDenddate(srcStartDate);

					priceUpdateDao
							.updateByPrimaryKeySelective(productSalePrice);
				} else if (srcStartDate.after(desStartDate)
						&& srcEndDate.before(desEndDate)) {
					productSalePrice.setDenddate(srcStartDate);
					priceUpdateDao
							.updateByPrimaryKeySelective(productSalePrice);

					ProductSalePrice newProductSalePrice = new ProductSalePrice();
					BeanUtils.copyProperties(price, newProductSalePrice);
					newProductSalePrice.setDbegindate(srcEndDate);
					newProductSalePrice.setDenddate(desEndDate);
					priceUpdateDao.addProductSalePrice(newProductSalePrice);
				} else if (srcStartDate.before(srcStartDate)
						&& srcEndDate.before(desEndDate)) {
					price.setIid(productSalePrice.getIid());
				}

			}

			if (null != price.getIid()) {
				return priceUpdateDao.updateByPrimaryKeySelective(price) > 0;
			} else {
				return priceUpdateDao.addProductSalePrice(price) > 0;
			}
		}
	}

	/**
	 * 该listingId商品是否是折扣商品
	 * @author lijun
	 * @param listingId
	 * @return true：折扣商品  false：非折扣商品
	 */
	public boolean isOffPrice(String listingId) {
		if (listingId == null || listingId.length() == 0) {
			return false;
		}
		return this.priceEnquiryDao.isOffPrice(listingId) > 0 ? true : false;
	}
	
	/**
	 * 通过listid，获取产品销售价格信息
	 * @param listingIDs
	 * @return
	 */
	public List<ProductSalePrice> getAllProductSalePriceByListingIds(String listingId){
		return priceEnquiryDao.getAllProductSalePriceByListingId(listingId);
		
	}
}
