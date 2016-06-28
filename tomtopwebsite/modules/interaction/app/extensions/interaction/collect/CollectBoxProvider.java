package extensions.interaction.collect;

import javax.inject.Singleton;

import play.twirl.api.Html;
import extensions.order.collect.ICollectProvider;

@Singleton
public class CollectBoxProvider implements ICollectProvider {

	
	@Override
	public Html getHtml() {
		return views.html.interaction.collect.collect_box.render();
	}

}
