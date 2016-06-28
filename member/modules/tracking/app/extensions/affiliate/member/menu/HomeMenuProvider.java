package extensions.affiliate.member.menu;

import play.twirl.api.Html;
import extensions.member.account.IMemberAccountMenuProvider;

public class HomeMenuProvider implements IMemberAccountMenuProvider {

	public static final String ID ="affiliate-home";
	
	@Override
	public String getCategory() {
		
		return "affiliate";
	}

	@Override
	public int getDisplayOrder() {
		
		return 0;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		
		final boolean highliht=ID.equals(currentMenuID);
		return views.html.affiliate.menu.home.render(highliht);
	}

}
