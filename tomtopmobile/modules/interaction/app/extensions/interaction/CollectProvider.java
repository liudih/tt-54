package extensions.interaction;

import java.util.List;
import java.util.Map;

import play.Logger;
import play.libs.Json;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.interaction.ICollectService;
import services.member.login.ILoginService;
import services.product.IProductBadgePartProvider;
import valueobjects.interaction.CollectCount;
import valueobjects.product.ProductBadgePartContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;

import dto.interaction.ProductCollect;

public class CollectProvider implements IProductBadgePartProvider {

	@Inject
	ICollectService collectService;
	@Inject
	ILoginService loginService;
	@Inject
	FoundationService foundationService;

	@Override
	public String getName() {
		return "badge-collect";
	}

	@Override
	public Map<String, Html> getFragment(ProductBadgePartContext partContext) {
		List<String> listingIds = partContext.getListingIds();
		if (listingIds.size() == 0) {
			return Maps.newHashMap();
		}

		// 收藏
		List<CollectCount> collectCount = collectService
				.getCollectCountByListingIds(listingIds);
		List<ProductCollect> collects = Lists.newArrayList();
		try {
			collects = collectService.getCollectByListingIds(listingIds);
		} catch (Exception ex) {
			Logger.error("CollectProvider error---", ex);
		}
		Map<String, CollectCount> countmap = Maps.uniqueIndex(collectCount,
				list -> list.getListingId());
		Multimap<String, ProductCollect> collectmap = Multimaps.index(collects,
				clist -> clist.getClistingid());
		Multimap<String, String> collectmap2 = Multimaps.transformValues(
				collectmap, m -> m.getCemail());

		Logger.debug("++++" + foundationService.getLoginContext().getMemberID());
		final String email = foundationService.getLoginContext().isLogin() ? foundationService
				.getLoginContext().getMemberID() : "";

		Map<String, Html> htmlmap = Maps.toMap(listingIds, id -> {
			Integer count = countmap.get(id) != null ? countmap.get(id)
					.getCollectCount() : 0;
			boolean isCollect = false;
			if (!"".equals(email) && collectmap2.get(id) != null
					&& collectmap2.get(id).contains(email)) {
				isCollect = true;
			}
			return views.html.interaction.collect.badge_collect.render(id,
					count, isCollect);
		});
		return htmlmap;
	}

	@Override
	public int getDisplayOrder() {
		return 20;
	}
}
