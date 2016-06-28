package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.rabbit.conf.mapper.product.ProductUrlMapper;
/*import com.google.inject.Inject;*/
import com.rabbit.dao.idao.product.IProductUrlEnquiryDao;
import com.rabbit.dto.product.ProductUrl;
import com.rabbit.dto.product.ProductUrlWithSmallImage;
@Component
public class ProductUrlEnquiryDao implements IProductUrlEnquiryDao {
	@Autowired
	ProductUrlMapper productUrlMapper;

	
	public ProductUrl getProductUrlsByListingId(String listingId,
			Integer language) {
		return this.productUrlMapper.getProductUrlsByListingId(listingId,
				language);
	}

	
	public List<ProductUrlWithSmallImage> getProductUrlsByListingIds(
			List<String> listingIds, int language) {
		return this.productUrlMapper.getProductUrlsByListingIds(listingIds,
				language);
	}

	
	public ProductUrl getProductUrlByUrl(String url, int websiteid,
			int languageid) {
		return this.productUrlMapper.getProductUrlByUrl(url, websiteid,
				languageid);
	}

	
	public List<ProductUrl> getProductUrlByClistingids(List<String> clistingids) {
		return this.productUrlMapper.getProductUrlByClistingids(clistingids);
	}

	
	public List<ProductUrl> getProductUrlByListingId(String clistingid) {
		return this.productUrlMapper.getProductUrlByListingId(clistingid);
	}

	
	public List<ProductUrl> getProductUrlByListingIdsAndLanguageId(
			List<String> listingids, Integer languageid) {
		return productUrlMapper.getProductUrlByListingIdsAndLanguageId(
				listingids, languageid);
	}

	
	public ProductUrl getBaseUrlBylanguageAndUrl(String url, int language) {
		return productUrlMapper.getBaseUrlBylanguageAndUrl(url, language);
	}

	
	public ProductUrl getProductBySkuAndLanguage(String sku, int languageid) {
		return productUrlMapper.getProductBySkuAndLanguage(sku, languageid);
	}

	
	public String getUrlByListingIdAndLanugage(String listingId,
			Integer language) {
		ProductUrl productUrl = this.getProductUrlsByListingId(listingId,
				language);
		return null != productUrl ? productUrl.getCurl() : null;
	}
}
