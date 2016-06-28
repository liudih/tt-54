package extensions.interaction;

import java.util.List;
import java.util.Map;

import play.Logger;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.interaction.ICollectService;
import services.member.login.ILoginService;
import services.product.IProductBadgePartProvider;
import services.product.IProductDetailPartProvider;
import valueobjects.interaction.CollectCount;
import valueobjects.product.ProductBadgePartContext;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;

import dto.interaction.ProductCollect;

public class ProductDetailCollectProvider implements IProductDetailPartProvider {

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
	public Html getFragment(ProductBadgePartContext partContext) {
		List<String> listingIds = partContext.getListingIds();

		// 收藏
		List<CollectCount> collectCount = collectService
				.getCollectCountByListingIds(listingIds);
		List<ProductCollect> collects = collectService
				.getCollectByListingIds(listingIds);
		Map<String, CollectCount> countmap = Maps.uniqueIndex(collectCount,
				list -> list.getListingId());
		Multimap<String, ProductCollect> collectmap = Multimaps.index(collects,
				clist -> clist.getClistingid());
		Multimap<String, String> collectmap2 = Multimaps.transformValues(
				collectmap, m -> m.getCemail());

		final String email = foundationService.getLoginContext().isLogin() ? foundationService
				.getLoginContext().getMemberID() : "";

		String id = listingIds.get(0);
		Integer count = countmap.get(id) != null ? countmap.get(id)
				.getCollectCount() : 0;
		boolean isCollect = false;
		if (!"".equals(email) && collectmap2.get(id) != null
				&& collectmap2.get(id).contains(email)) {
			isCollect = true;
		}
		return views.html.interaction.collect.product_detail_collect.render(id, count,
				isCollect);

	}

	@Override
	public int getDisplayOrder() {
		return 20;
	}
}
