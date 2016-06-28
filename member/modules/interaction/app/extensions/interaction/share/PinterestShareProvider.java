package extensions.interaction.share;

import javax.inject.Singleton;

import play.twirl.api.Html;
import extensions.interaction.share.IShareProvider;

@Singleton
public class PinterestShareProvider implements IShareProvider {


	@Override
	public boolean isPureJS() {
		return true;
	}

	@Override
	public Html getShareButton(String url,String image,String title) {
		return views.html.interaction.share.pinterest.render(url,image,title);
	}

	@Override
	public int getDisplayOrder() {
		return 40;
	}

}
