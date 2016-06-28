package com.rabbit.dao.daoImp.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductSalePriceMapper;
import com.rabbit.dao.idao.product.IProductSalePriceUpdateDao;
import com.rabbit.dto.product.ProductSalePrice;
@Component
public class ProductSalePriceUpdateDao implements IProductSalePriceUpdateDao {

	@Autowired
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
