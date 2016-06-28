package extensions.interaction.share;

import javax.inject.Singleton;

import play.twirl.api.Html;
import extensions.interaction.share.IShareProvider;

@Singleton
public class TwitterShareProvider implements IShareProvider {


	@Override
	public boolean isPureJS() {
		return true;
	}

	@Override
	public Html getShareButton(String url,String image,String title) {
		return views.html.interaction.share.twitter.render(url);
	}

	@Override
	public int getDisplayOrder() {
		return 20;
	}

}
