package dao.product.impl;

import java.util.List;

import mapper.product.ProductSellingPointsMapper;

import com.google.api.client.util.Lists;
import com.google.inject.Inject;

import dao.product.IProductSellingPointsEnquiryDao;
import dto.product.ProductSellingPoints;

public class ProductSellingPointsEnquiryDao implements
		IProductSellingPointsEnquiryDao {

	@Inject
	ProductSellingPointsMapper productSellingPointsMapper;

	@Override
	public List<ProductSellingPoints> getProductSellingPointsByListingIdAndLanguage(
			String listingID, int lang) {
		return this.productSellingPointsMapper
				.getProductSellingPointsByListingIdAndLanguage(listingID, lang);
	}

	@Override
	public List<ProductSellingPoints> getProductSellingPointsByListingIds(
			List<String> listingids, Integer lang) {
		if (listingids == null || listingids.size() == 0) {
			return Lists.newArrayList();
		}
		return this.productSellingPointsMapper
				.getProductSellingPointsByListingIds(listingids, lang);
	}

	@Override
	public List<ProductSellingPoints> getProductSellingPointsByListingId(
			String listingID) {
		return this.productSellingPointsMapper
				.getProductSellingPointsByListingId(listingID);
	}

	@Override
	public List<ProductSellingPoints> getProductSellingPointsByListingIdsOnly(
			List<String> listingIDs) {
		return this.productSellingPointsMapper
				.getProductSellingPointsByListingIdsOnly(listingIDs);
	}

}
