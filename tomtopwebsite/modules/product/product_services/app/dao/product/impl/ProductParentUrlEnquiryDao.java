package dao.product.impl;

import mapper.product.ProductParentUrlMapper;

import com.google.inject.Inject;

import dao.product.IProductParentUrlEnquiryDao;
import dto.product.ProductParentUrl;

public class ProductParentUrlEnquiryDao implements IProductParentUrlEnquiryDao {
	@Inject
	ProductParentUrlMapper productParentUrlMapper;

	@Override
	public ProductParentUrl getProductParentUrlByUrlAndLanguageId(String url,
			Integer languageId) {
		return this.productParentUrlMapper.getProductParentUrlByUrlAndLanguageId(
				url, languageId);
	}

}
