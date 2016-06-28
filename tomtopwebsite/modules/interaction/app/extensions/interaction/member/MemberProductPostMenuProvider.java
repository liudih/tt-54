package extensions.interaction.member;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.interaction.product.post.ProductPostService;
import services.member.login.LoginService;
import extensions.member.account.IMemberAccountMenuProvider;

public class MemberProductPostMenuProvider implements
		IMemberAccountMenuProvider {

	public static final String ID = "product-post";

	@Override
	public String getCategory() {
		// 暂时关闭FAQ
		// return "community";
		return "";
	}

	@Override
	public int getDisplayOrder() {

		return 1;
	}

	@Inject
	ProductPostService postService;

	@Inject
	LoginService loginService;

	@Override
	public Html getMenuItem(String currentMenuID) {

		final boolean highliht = ID.equals(currentMenuID);
		String email = loginService.getLoginData().getEmail();
		int totail = postService.getTotalRecordByEamil(email);
		return views.html.interaction.member.menu_faq.render(highliht, totail);
	}
}
