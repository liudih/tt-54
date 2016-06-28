package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dao.idao.IProductEnquiryDao;

import com.rabbit.dto.product.ProductUrl;
import com.rabbit.dto.product.ProductUrlWithSmallImage;

public interface IProductUrlEnquiryDao extends IProductEnquiryDao {

	public ProductUrl getProductUrlsByListingId(String listingId,
			Integer language);

	public String getUrlByListingIdAndLanugage(String listingId, Integer language);

	public List<ProductUrlWithSmallImage> getProductUrlsByListingIds(
			List<String> listingIds, int language);

	public ProductUrl getProductUrlByUrl(String url, int websiteid,
			int languageid);

	public List<ProductUrl> getProductUrlByClistingids(List<String> clistingids);

	public List<ProductUrl> getProductUrlByListingId(String listingid);

	public List<ProductUrl> getProductUrlByListingIdsAndLanguageId(
			List<String> listingids, Integer languageid);

	public ProductUrl getBaseUrlBylanguageAndUrl(String url, int language);

	public ProductUrl getProductBySkuAndLanguage(String sku, int languageid);
}
