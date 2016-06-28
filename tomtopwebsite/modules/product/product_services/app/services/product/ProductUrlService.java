package services.product;

import java.util.List;

import javax.inject.Inject;

import services.base.FoundationService;
import context.WebContext;
import dao.product.IProductParentUrlEnquiryDao;
import dao.product.IProductUrlEnquiryDao;
import dto.product.ProductParentUrl;
import dto.product.ProductUrl;
import dto.product.ProductUrlWithSmallImage;

public class ProductUrlService implements IProductUrlService {

	@Inject
	IProductParentUrlEnquiryDao productParentUrlEnquiryDao;

	@Inject
	IProductUrlEnquiryDao productUrlEnquiryDao;

	@Inject
	FoundationService foundationService;

	public ProductUrl getProductUrlsByListingId(String listingId,
			Integer language) {
		return productUrlEnquiryDao.getProductUrlsByListingId(listingId,
				language);
	}

	@Override
	public ProductUrl getProductUrlsByListingId(String listingId,
			WebContext context) {
		int language = foundationService.getLanguage(context);
		return this.getProductUrlsByListingId(listingId, language);
	}

	public List<ProductUrlWithSmallImage> getProductUrlsByListingIds(
			List<String> listingIds, int language) {
		return productUrlEnquiryDao.getProductUrlsByListingIds(listingIds,
				language);
	}

	@Override
	public List<ProductUrlWithSmallImage> getProductUrlsByListingIds(
			List<String> listingIds, WebContext context) {
		int language = foundationService.getLanguage(context);
		return this.getProductUrlsByListingIds(listingIds, language);
	}

	@Override
	public ProductUrl getProductUrlByUrl(String url, WebContext context) {
		int language = foundationService.getLanguage(context);
		int site = foundationService.getSiteID(context);
		ProductUrl productUrl = productUrlEnquiryDao.getProductUrlByUrl(url,
				site, language);
		return productUrl;
	}

	@Override
	public List<ProductUrl> getProductUrlByClistingids(List<String> clistingids) {
		return productUrlEnquiryDao.getProductUrlByClistingids(clistingids);
	}

	@Override
	public List<ProductUrl> getProductUrlByListingId(String listingid) {
		return productUrlEnquiryDao.getProductUrlByListingId(listingid);
	}

	public List<ProductUrl> getProductUrlByListingIdsAndLanguageId(
			List<String> listingids, int languageid) {
		return productUrlEnquiryDao.getProductUrlByListingIdsAndLanguageId(
				listingids, languageid);
	}

	@Override
	public List<ProductUrl> getProductUrlByListingIdsAndLanguageId(
			List<String> listingids, WebContext context) {
		int languageid = foundationService.getLanguage(context);
		return this.getProductUrlByListingIdsAndLanguageId(listingids,
				languageid);
	}

	public ProductUrl getBaseUrlBylanguageAndUrl(String url, int language) {
		return productUrlEnquiryDao.getBaseUrlBylanguageAndUrl(url, language);
	}

	@Override
	public ProductUrl getBaseUrlBylanguageAndUrl(String url, WebContext context) {
		int language = foundationService.getLanguage(context);
		return this.getBaseUrlBylanguageAndUrl(url, language);

	}

	public ProductUrl getProductBySkuAndLanguage(String sku, int languageid) {
		return productUrlEnquiryDao.getProductBySkuAndLanguage(sku, languageid);
	}

	@Override
	public ProductUrl getProductBySkuAndLanguage(String sku, WebContext context) {
		int languageid = foundationService.getLanguage(context);
		return this.getProductBySkuAndLanguage(sku, languageid);
	}

	public ProductParentUrl getProductParentUrlByUrl(String url,
			Integer languageId) {
		return this.productParentUrlEnquiryDao
				.getProductParentUrlByUrlAndLanguageId(url, languageId);
	}

	@Override
	public ProductParentUrl getProductParentUrlByUrl(String url,
			WebContext context) {
		int languageId = foundationService.getLanguage(context);
		return this.productParentUrlEnquiryDao
				.getProductParentUrlByUrlAndLanguageId(url, languageId);
	}

}
