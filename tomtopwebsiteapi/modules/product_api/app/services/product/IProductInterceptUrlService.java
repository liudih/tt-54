package services.product;

import context.WebContext;
import dto.product.ProductInterceptUrl;

public interface IProductInterceptUrlService {

	ProductInterceptUrl getProductInterceptUrlByUrl(String url,
			WebContext context);

	ProductInterceptUrl getUrlByLanuageidAndListingid(WebContext context,
			String listingid);

	ProductInterceptUrl getProductBySkuAndLanguage(String sku,
			WebContext context);

}
