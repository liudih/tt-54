package dao.product.impl;

import java.util.Date;
import java.util.List;

import mapper.product.ProductSalePriceMapper;

import com.google.inject.Inject;

import dao.product.IProductSalePriceUpdateDao;
import dto.product.ProductSalePrice;

public class ProductSalePriceUpdateDao implements IProductSalePriceUpdateDao {

	@Inject
	ProductSalePriceMapper productSalePriceMapper;

	@Override
	public int addProductSalePrice(ProductSalePrice record) {
		return this.productSalePriceMapper.addProductSalePrice(record);
	}

	@Override
	public int updateByPrimaryKeySelective(ProductSalePrice record) {
		return this.productSalePriceMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int addBatch(List<ProductSalePrice> list) {
		return this.productSalePriceMapper.addBatch(list);
	}

	@Override
	public int deleteProductCurrentSalePrice(String listingId) {
		return productSalePriceMapper.deleteProductCurrentSalePrice(listingId);
	}

	@Override
	public int deleteProductSalePriceByDate(String listingId, Date start,
			Date end) {
		return productSalePriceMapper.deleteProductSalePriceByDate(listingId,
				start, end);
	}

}
