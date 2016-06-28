package dao.product.impl;

import java.util.List;

import mapper.product.ProductUrlMapper;

import com.google.inject.Inject;

import dao.product.IProductUrlEnquiryDao;
import dto.product.ProductUrl;
import dto.product.ProductUrlWithSmallImage;

public class ProductUrlEnquiryDao implements IProductUrlEnquiryDao {
	@Inject
	ProductUrlMapper productUrlMapper;

	@Override
	public ProductUrl getProductUrlsByListingId(String listingId,
			Integer language) {
		return this.productUrlMapper.getProductUrlsByListingId(listingId,
				language);
	}

	@Override
	public List<ProductUrlWithSmallImage> getProductUrlsByListingIds(
			List<String> listingIds, int language) {
		return this.productUrlMapper.getProductUrlsByListingIds(listingIds,
				language);
	}

	@Override
	public ProductUrl getProductUrlByUrl(String url, int websiteid,
			int languageid) {
		return this.productUrlMapper.getProductUrlByUrl(url, websiteid,
				languageid);
	}

	@Override
	public List<ProductUrl> getProductUrlByClistingids(List<String> clistingids) {
		return this.productUrlMapper.getProductUrlByClistingids(clistingids);
	}

	@Override
	public List<ProductUrl> getProductUrlByListingId(String clistingid) {
		return this.productUrlMapper.getProductUrlByListingId(clistingid);
	}

	@Override
	public List<ProductUrl> getProductUrlByListingIdsAndLanguageId(
			List<String> listingids, Integer languageid) {
		return productUrlMapper.getProductUrlByListingIdsAndLanguageId(
				listingids, languageid);
	}

	@Override
	public ProductUrl getBaseUrlBylanguageAndUrl(String url, int language) {
		return productUrlMapper.getBaseUrlBylanguageAndUrl(url, language);
	}

	@Override
	public ProductUrl getProductBySkuAndLanguage(String sku, int languageid) {
		return productUrlMapper.getProductBySkuAndLanguage(sku, languageid);
	}

	@Override
	public String getUrlByListingIdAndLanugage(String listingId,
			Integer language) {
		ProductUrl productUrl = this.getProductUrlsByListingId(listingId,
				language);
		return null != productUrl ? productUrl.getCurl() : null;
	}
}
