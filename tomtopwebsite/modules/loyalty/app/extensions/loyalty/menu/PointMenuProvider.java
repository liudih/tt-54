package extensions.loyalty.menu;

import play.twirl.api.Html;
import extensions.member.account.IMemberAccountMenuProvider;

public class PointMenuProvider implements IMemberAccountMenuProvider {

	public static final String ID ="mypoints";
	
	@Override
	public String getCategory() {
	
		return "account";
	}

	@Override
	public int getDisplayOrder() {
		
		return 30; 
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		final boolean highliht = ID.equals(currentMenuID);
		return views.html.loyalty.points.menu.point.render(highliht);
	}

}
