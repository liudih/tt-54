package extensions.research;

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
public interface IProductBadgePartProvider {

	String getName();

	int getDisplayOrder();

	Map<String, Html> getFragment(ProductBadgePartContext partContext);

}
