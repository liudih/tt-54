package extensions.messaging.member;

import javax.inject.Singleton;

import play.twirl.api.Html;
import services.messaging.IMessageService;

import com.google.inject.Inject;

import extensions.member.IMyMessageProvider;
import extensions.member.account.IMemberAccountMenuProvider;

/**
 * 账户中心->Account->MyMessage 菜单提供者
 * 
 * @author lijun
 *
 */
@Singleton
public class MemberMyMessageProvider implements IMemberAccountMenuProvider,IMyMessageProvider {

	private static final String CATEGORY = "account";
	private static final String ID = "message-list";
	@Inject
	private IMessageService service;

	@Override
	public String getCategory() {
		return CATEGORY;
	}

	@Override
	public int getDisplayOrder() {
		return 21;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		int total = service.getMyMessageTotal();
		// 是否高亮显示
		final boolean highliht = ID.equals(currentMenuID);
		return views.html.messaging.member.menu.render(highliht, total);
	}

	@Override
	public int getUnreadTotal() {
		return service.getMyMessageTotal();
	}

}
