package extensions.loyalty.subscribe;

import javax.inject.Singleton;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import extensions.product.subscribe.ISubscribeProvider;

@Singleton
public class SubscribeBoxProvider implements ISubscribeProvider {

	@Override
	public int getDisplayOrder(){
		return 10;
	}
	
	@Override
	public Html getHtml() {
		return views.html.loyalty.subscribe.subscribe_box.render(Context.current());
	}

}
