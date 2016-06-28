package services.member;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.twirl.api.Html;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import extensions.member.account.IMemberQuickMenuProvider;

@Singleton
public class MemberQuickMenuRenderer {

	final List<IMemberQuickMenuProvider> menuProviders;

	@Inject
	public MemberQuickMenuRenderer(Set<IMemberQuickMenuProvider> menuProviders) {
		this.menuProviders = Ordering
				.natural()
				.onResultOf((IMemberQuickMenuProvider p) -> p.getDisplayOrder())
				.immutableSortedCopy(menuProviders);
	}

	public List<Html> getMenuItems() {
		return Lists.transform(menuProviders, mp -> mp.getQuickMenuItem());
	}
}
