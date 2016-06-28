package services.product;

import javax.inject.Inject;

import services.base.FoundationService;
import context.WebContext;
import dao.product.IProductInterceptUrlEnquiryDao;
import dto.product.ProductInterceptUrl;

public class ProductInterceptUrlService implements
		IProductInterceptUrlService {

	@Inject
	FoundationService foundationService;

	@Inject
	IProductInterceptUrlEnquiryDao productInterceptUrlEnquiryDao;

	public ProductInterceptUrl getProductInterceptUrlByUrl(String url,
			int languageid) {
		return productInterceptUrlEnquiryDao.getProductInterceptUrlByUrl(url,
				languageid);
	};

	@Override
	public ProductInterceptUrl getProductInterceptUrlByUrl(String url,
			WebContext context) {
		int languageid = foundationService.getLanguage();
		return this.getProductInterceptUrlByUrl(url, languageid);
	};

	public ProductInterceptUrl getUrlByLanuageidAndListingid(int language,
			String listingid) {
		return productInterceptUrlEnquiryDao.getUrlByLanuageidAndListingid(
				language, listingid);
	};

	@Override
	public ProductInterceptUrl getUrlByLanuageidAndListingid(
			WebContext context, String listingid) {
		int language = foundationService.getLanguage(context);
		return this.getUrlByLanuageidAndListingid(language, listingid);
	};

	public ProductInterceptUrl getProductBySkuAndLanguage(String sku,
			int languageid) {
		return productInterceptUrlEnquiryDao.getProductBySkuAndLanguage(sku,
				languageid);
	};

	@Override
	public ProductInterceptUrl getProductBySkuAndLanguage(String sku,
			WebContext context) {
		int languageid = foundationService.getLanguage(context);
		return this.getProductBySkuAndLanguage(sku, languageid);
	};

}
