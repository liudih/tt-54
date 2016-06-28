package dao.product.impl;

import mapper.product.ProductInterceptUrlMapper;

import com.google.inject.Inject;

import dao.product.IProductInterceptUrlEnquiryDao;
import dto.product.ProductInterceptUrl;

public class ProductInterceptUrlEnquiryDao implements IProductInterceptUrlEnquiryDao{
	@Inject
	ProductInterceptUrlMapper productInterceptUrlMapper;
	
	@Override
	public ProductInterceptUrl getProductInterceptUrlByUrl(String url,int languageid) {
		return productInterceptUrlMapper.getProductInterceptUrlByUrl(url,languageid);
	}
	
	@Override
	public ProductInterceptUrl getUrlByLanuageidAndListingid(int language,String listingid){
		return productInterceptUrlMapper.getUrlByLanuageidAndListingid(language, listingid);
	}
	
	@Override
	public ProductInterceptUrl getProductBySkuAndLanguage(String sku,int languageid){
		return productInterceptUrlMapper.getProductBySkuAndLanguage(sku,languageid);
	}
}
