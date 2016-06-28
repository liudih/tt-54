package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.rabbit.conf.mapper.product.ProductSellingPointsMapper;
import com.rabbit.dao.idao.product.IProductSellingPointsEnquiryDao;
import com.rabbit.dto.product.ProductSellingPoints;
@Component
public class ProductSellingPointsEnquiryDao implements
		IProductSellingPointsEnquiryDao {

	@Autowired
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
