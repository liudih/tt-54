package extensions.member.account;

import play.twirl.api.Html;

public interface IMemberQuickMenuProvider {

	int getDisplayOrder();

	Html getQuickMenuItem();

}
