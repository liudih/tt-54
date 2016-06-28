package extensions.member.account;

import play.twirl.api.Html;
import valueobjects.member.MemberInSession;

public interface IMemberAccountHomeFragmentProvider {

	int getDisplayOrder();

	Html getFragment(MemberInSession member);

}
