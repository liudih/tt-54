package extensions.facebook.share;

import javax.inject.Singleton;

import play.twirl.api.Html;
import extensions.interaction.share.IShareProvider;

@Singleton
public class FacebookShareProvider implements IShareProvider {


	@Override
	public boolean isPureJS() {
		return true;
	}

	@Override
	public Html getShareButton(String url,String image,String title) {
		return views.html.facebook.share.render(url);
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

}
