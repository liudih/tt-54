package com.rabbit.services.iservice.product;

import java.util.List;

import com.rabbit.dto.transfer.product.ProductBack;

public interface IProductEnquiryService {

	List<ProductBack> getProductsByListingIds(List<String> listingids);
}
