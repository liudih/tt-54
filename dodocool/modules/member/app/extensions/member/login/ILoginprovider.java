package extensions.member.login;

import play.twirl.api.Html;

public interface ILoginprovider {

	int getDisplayOrder();

	boolean isPureJS();

	Html getLoginButton();

	String getName();

}
