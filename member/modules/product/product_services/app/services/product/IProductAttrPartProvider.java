package services.product;

import java.util.List;
import java.util.Map;

import play.twirl.api.Html;

public interface IProductAttrPartProvider {

	String getName();

	int getDisplayOrder();

	Map<String, Map<String,Html>> getHtml(List<String> listingIds);

}
