package services.product;

import java.util.List;

import context.WebContext;
import dto.product.ProductParentUrl;
import dto.product.ProductUrl;
import dto.product.ProductUrlWithSmallImage;

public interface IProductUrlService {

	ProductUrl getProductUrlsByListingId(String listingId, WebContext context);

	List<ProductUrlWithSmallImage> getProductUrlsByListingIds(
			List<String> listingIds, WebContext context);

	ProductUrl getProductUrlByUrl(String url,WebContext context);

	List<ProductUrl> getProductUrlByClistingids(List<String> clistingids);

	List<ProductUrl> getProductUrlByListingId(String listingid);

	List<ProductUrl> getProductUrlByListingIdsAndLanguageId(
			List<String> listingids, WebContext context);

	ProductUrl getBaseUrlBylanguageAndUrl(String url, WebContext context);

	ProductUrl getProductBySkuAndLanguage(String sku, WebContext context);

	ProductParentUrl getProductParentUrlByUrl(String url, WebContext context);

}
