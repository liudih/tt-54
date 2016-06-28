package dao.product.impl;

import java.util.List;

import mapper.product.ProductTranslateMapper;

import com.google.inject.Inject;

import dao.product.IProductTranslateEnquiryDao;
import dto.product.ProductTranslate;

public class ProductTranslateEnquiryDao implements IProductTranslateEnquiryDao {

	@Inject
	ProductTranslateMapper productTranslateMapper;

	@Override
	public ProductTranslate getProductTranslateByIid(Integer iid) {
		return this.productTranslateMapper.getProductTranslateByIid(iid);
	}

	@Override
	public List<ProductTranslate> getLanguageIdByListingid(String clisting) {
		return this.productTranslateMapper.getLanguageIdByListingid(clisting);
	}

	@Override
	public List<ProductTranslate> getProductTranslatesByListingid(
			String clisting) {
		return this.productTranslateMapper
				.getProductTranslatesByListingid(clisting);
	}

	@Override
	public String getTitleByListingid(String clisting, Integer lan) {
		return this.productTranslateMapper.getTitleByListingid(clisting, lan);
	}

	@Override
	public List<ProductTranslate> getTitleByClistings(List<String> clistings) {
		return this.productTranslateMapper.getTitleByClistings(clistings);
	}

	@Override
	public List<ProductTranslate> getTitleByClistingsAndLanguage(
			Integer languageId, List<String> clistings) {
		return this.productTranslateMapper.getTitleByClistingsAndLanguage(
				languageId, clistings);
	}

	@Override
	public ProductTranslate getTranslateByListingidAndLanguage(
			String listingid, Integer language) {
		return this.productTranslateMapper.getTranslateByListingidAndLanguage(
				listingid, language);
	}

	@Override
	public List<ProductTranslate> getProductTranslateByListingIdsAndLanuageId(
			List<String> listingids, int languageid) {
		return productTranslateMapper
				.getProductTranslateByListingIdsAndLanuageId(listingids,
						languageid);
	}

	@Override
	public List<ProductTranslate> getProductTranslateBySku(String sku) {
		return productTranslateMapper.getProductTranslateBySku(sku);
	}

}
