package dao.product.impl;

import java.util.List;

import mapper.product.ProductViewCountMapper;
import valueobjects.product.ProductViewCount;

import com.google.inject.Inject;

import dao.product.IProductViewCountEnquiryDao;

public class ProductViewCountEnquiryDao implements IProductViewCountEnquiryDao {

	@Inject
	ProductViewCountMapper productViewCountMapper;
	
	@Override
	public List<ProductViewCount> getViewCountListByListingIds(List<String> listingIds) {
		 
		return this.productViewCountMapper.getViewCountListByListingIds(listingIds);
	}

}
