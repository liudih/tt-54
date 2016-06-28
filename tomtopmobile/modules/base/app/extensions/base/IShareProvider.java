package extensions.base;

import play.twirl.api.Html;

public interface IShareProvider {

	int getDisplayOrder();

	boolean isPureJS();

	Html getShareButton(String url,String image,String title);

}
