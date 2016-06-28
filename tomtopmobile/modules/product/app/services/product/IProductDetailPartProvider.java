package services.product;

import java.util.Map;

import play.twirl.api.Html;
import valueobjects.product.ProductBadgePartContext;

/**
 * Provide extra information for product badge (as shown for each product in
 * product listing/search)
 * 
 * @author kmtong
 *
 */
public interface IProductDetailPartProvider {

	String getName();

	int getDisplayOrder();

	Html getFragment(ProductBadgePartContext partContext);
}
