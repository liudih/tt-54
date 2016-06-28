package services.member.account;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.twirl.api.Html;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;

import extensions.member.account.IMemberAccountMenuProvider;

@Singleton
public class MemberAccountMenuRenderer {

	final ListMultimap<String, IMemberAccountMenuProvider> menuProvidersMap;

	@Inject
	public MemberAccountMenuRenderer(
			Set<IMemberAccountMenuProvider> menuProviders) {
		this.menuProvidersMap = Multimaps.index(menuProviders,
				m -> m.getCategory());
	}

	public List<Html> getMenuItems(String categoryName, String currentMenuID) {
		List<IMemberAccountMenuProvider> m = menuProvidersMap.get(categoryName);
		if (m != null) {
			return Lists.transform(
					Ordering.natural()
							.onResultOf(
									(IMemberAccountMenuProvider p) -> p
											.getDisplayOrder()).sortedCopy(m),
					mp -> mp.getMenuItem(currentMenuID));
		}
		return null;
	}
}
