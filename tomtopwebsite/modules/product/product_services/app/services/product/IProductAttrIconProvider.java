package services.product;

import java.util.List;
import java.util.Map;

import play.twirl.api.Html;

public interface IProductAttrIconProvider {

	String getName();

	int getDisplayOrder();

	Map<String, Html> getHtml(List<String> listingIds);

}
