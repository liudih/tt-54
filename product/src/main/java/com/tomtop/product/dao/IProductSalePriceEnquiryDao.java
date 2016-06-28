package com.tomtop.product.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tomtop.product.models.dto.ProductSalePrice;
import com.tomtop.product.models.dto.ProductSalePriceLite;

public interface IProductSalePriceEnquiryDao extends IProductEnquiryDao {

	List<ProductSalePrice> getAllProductSalePriceByListingId(String listingId);

	List<ProductSalePrice> getAllProductSalePriceByListingIds(
			List<String> listingIDs);

	ProductSalePriceLite getProductSalePriceLite(String listingId);

	List<ProductSalePrice> getProductSalePrice(Date now, List<String> listingIds);

	List<ProductSalePrice> getProductSaleByDenddate(Date beginDate, Date endDate);

	List<String> getExistsListings(List<String> listingIds);

	List<String> getSaleListings();

	List<ProductSalePrice> getProductSalePriceAfterCurrentDate(
			Map<String, Object> paras);

	List<ProductSalePrice> getProductSalePriceByDate(String listingId,
			Date beginDate, Date endDate);

	/**
	 * 该listingId商品是否是折扣商品
	 * 
	 * @author lijun
	 * @param listingId
	 * @return 1：折扣商品 0：非折扣商品
	 */
	int isOffPrice(String listingId);
}
