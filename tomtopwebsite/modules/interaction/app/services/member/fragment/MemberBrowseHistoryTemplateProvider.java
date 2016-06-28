package services.member.fragment;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.interaction.MemberBrowseHistoryService;
import services.product.IProductBadgeService;
import valueobjects.interaction.MemberBrowseHistory;
import valueobjects.product.ProductBadge;

import com.google.common.collect.Lists;

public class MemberBrowseHistoryTemplateProvider implements
		ITemplateFragmentProvider {

	@Inject
	FoundationService foundation;

	@Inject
	MemberBrowseHistoryService service;

	@Inject
	IProductBadgeService badgeService;

	@Override
	public String getName() {
		return "member-browse-history-in-member-center";
	}

	@Override
	public Html getFragment(Context context) {
		int site = 1;
		int language = 1;
		String currency = "USD";
		if (context != null) {
			site = foundation.getSiteID(context);
			language = foundation.getLanguage(context);
			currency = foundation.getCurrency();
		}
		MemberBrowseHistory b = service.getMemberBrowseHistory(20);
		List<ProductBadge> product = badgeService.getProductBadgesByListingIDs(
				Lists.transform(b.getHistory(), h -> h.getClistingid()),
				language, site, currency, null);

		Integer perPage = 5;
		Integer maxPage = 4;
		String divClass = "xxkBOX accH_box history";
		String divId = "accH_box1";
		String ulClass = "accMovebox";
		String liClass = "accMovePic";
		String nextPageAjaxUrl = null;

		return views.html.home.turn_next_page_model.render(context, product,
				maxPage, perPage, nextPageAjaxUrl, null, divClass, divId,
				ulClass, liClass, false);
	}

}
