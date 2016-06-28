package extensions.product.subscribe;

import play.twirl.api.Html;

public interface ISubscribeProvider {

	int getDisplayOrder();
	
	Html getHtml();

}
