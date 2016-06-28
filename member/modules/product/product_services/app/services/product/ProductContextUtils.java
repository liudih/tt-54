package services.product;

import javax.inject.Inject;

import context.WebContext;
import play.Logger;
import services.base.FoundationService;
import valueobjects.product.ProductContext;
import dao.product.IProductBaseEnquiryDao;
import dto.product.ProductBase;
import dto.product.ProductUrl;

public class ProductContextUtils {

	@Inject
	ProductUrlService enquiry;

	@Inject
	IProductBaseEnquiryDao productBaseEnquityDao;

	@Inject
	FoundationService foundationService;

	public ProductContext createProductContext(String title,
			WebContext wcontext, boolean iscampain) {

		String sku = "";
		String listingId = "";
		Integer siteId = foundationService.getSiteID(wcontext);
		String currency = foundationService.getCurrency(wcontext);
		Integer language = foundationService.getLanguage(wcontext);
		ProductUrl productUrl = enquiry.getProductUrlByUrl(title, wcontext);
		if (null == productUrl) {
			return null;
		}
		sku = productUrl.getCsku();
		listingId = productUrl.getClistingid();

		ProductBase productbase = productBaseEnquityDao
				.getProductBaseByListingId(listingId);
		if (null == productbase) {
			return null;
		}

		Logger.debug("sku: " + sku);
		Logger.debug("listingId: " + listingId);

		int dftLang = 1;
		return new ProductContext(listingId, sku, siteId,
				language != null ? language : dftLang, currency);
	}
}
