package extensions.member.login;

import play.twirl.api.Html;

public interface ILoginProvider {

	int getDisplayOrder();

	boolean isPureJS();

	Html getLoginButton();

}
