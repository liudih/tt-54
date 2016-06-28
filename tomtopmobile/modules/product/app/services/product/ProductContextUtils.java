package services.product;

import javax.inject.Inject;

import ch.qos.logback.core.Context;
import context.ContextUtils;
import context.WebContext;
import play.Logger;
import services.base.FoundationService;
import valueobjects.product.ProductContext;
import dto.product.ProductBase;
import dto.product.ProductUrl;

public class ProductContextUtils {


	@Inject
	IProductEnquiryService productEnquiryService;
	
	@Inject
	IProductUrlService urlService;
	
	@Inject
	FoundationService foundation;

	public ProductContext createProductContext(String title, Integer siteId,
			Integer language, String currency, boolean iscampain) {

		String sku = "";
		String listingId = "";
         
		ProductUrl productUrl = urlService.getProductUrlByUrl(title, foundation.getWebContext());
		if (null == productUrl) {
			return null;
		}
		sku = productUrl.getCsku();
		listingId = productUrl.getClistingid();

		ProductBase productbase = productEnquiryService
				.getBaseByListingId(listingId);
		if (null == productbase) {
			return null;
		} else {
			if (!productbase.getIstatus().equals(1)) {
				return null;
			}
		}

		Logger.debug("sku: " + sku);
		Logger.debug("listingId: " + listingId);

		int dftLang = 1;
		return new ProductContext(listingId, sku, siteId,
				language != null ? language : dftLang, currency);
	}
}
