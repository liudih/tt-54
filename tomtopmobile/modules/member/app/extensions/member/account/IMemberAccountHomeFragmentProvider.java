package extensions.member.account;

import java.util.List;

import play.twirl.api.Html;

public interface IMemberAccountHomeFragmentProvider {

	List<Position> getPosition();

	int getDisplayOrder();

	Html getFragment(String email, Position position);

}
