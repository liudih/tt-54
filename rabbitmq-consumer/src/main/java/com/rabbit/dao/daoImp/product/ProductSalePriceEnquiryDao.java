package com.rabbit.dao.daoImp.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductSalePriceMapper;
import com.rabbit.dao.idao.product.IProductSalePriceEnquiryDao;
import com.rabbit.dto.ProductSalePriceLite;
import com.rabbit.dto.product.ProductSalePrice;
@Component
public class ProductSalePriceEnquiryDao implements IProductSalePriceEnquiryDao {

	@Autowired
	ProductSalePriceMapper productSalePriceMapper;
	
	@Override
	public List<ProductSalePrice> getAllProductSalePriceByListingId(
			String listingId) {
		return this.productSalePriceMapper
				.getAllProductSalePriceByListingId(listingId);
	}

	@Override
	public List<ProductSalePrice> getAllProductSalePriceByListingIds(
			List<String> listingIDs) {
		return this.productSalePriceMapper
				.getAllProductSalePriceByListingIds(listingIDs);
	}

	@Override
	public ProductSalePriceLite getProductSalePriceLite(String listingId) {
		return this.productSalePriceMapper.getProductSalePriceLite(listingId);
	}

	@Override
	public List<ProductSalePrice> getProductSalePrice(Date now,
			List<String> listingIds) {
		return this.productSalePriceMapper.getProductSalePrice(now, listingIds);
	}

	@Override
	public List<ProductSalePrice> getProductSaleByDenddate(Date beginDate,
			Date endDate) {
		return this.productSalePriceMapper.getProductSaleByDenddate(beginDate,
				endDate);
	}

	@Override
	public List<String> getExistsListings(List<String> listingIds) {
		return this.productSalePriceMapper.getExistsListings(listingIds);
	}

	@Override
	public List<String> getSaleListings() {
		return this.productSalePriceMapper.getSaleListings();
	}

	@Override
	public List<ProductSalePrice> getProductSalePriceAfterCurrentDate(
			Map<String, Object> paras) {
		return this.productSalePriceMapper
				.getProductSalePriceAfterCurrentDate(paras);
	}

	@Override
	public List<ProductSalePrice> getProductSalePriceByDate(String listingId,
			Date start, Date end) {
		return this.productSalePriceMapper.getProductSalePriceByDate(listingId,
				start, end);
	}

	@Override
	public int isOffPrice(String listingId) {
		return this.productSalePriceMapper.isOffPrice(listingId);
	}
}
