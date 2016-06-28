package extensions.member.account;

import play.twirl.api.Html;

public interface IMemberAccountMenuProvider {

	String getCategory();

	int getDisplayOrder();

	Html getMenuItem(String currentMenuID);

}
