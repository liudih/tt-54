package services.interaction.fragment;

import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;

import dto.interaction.ProductBrowse;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.interaction.IMemberBrowseHistoryService;
import services.product.IProductBadgeService;
import services.product.ProductUtilService;
import valueobjects.product.ProductBadge;

public class RecentHistoryFragmentProvider implements ITemplateFragmentProvider {

	@Inject
	FoundationService foundation;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	IMemberBrowseHistoryService historyService;
	
	@Inject
	ProductUtilService productUtilService;
	
	@Override
	public String getName() {
		return "recent-history";
	}

	@Override
	public Html getFragment(Context context) {
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();
		List<ProductBrowse> blist = historyService.getMemberBrowseHistoryByContext(foundation.getWebContext(),
				12,foundation.getLoginContext().isLogin());
		List<String> listings = Lists.transform(blist, b -> b.getClistingid());
		List<ProductBadge> products = badgeService.getProductBadgesByListingIDs(
				listings, site, lang, currency, null);
		productUtilService.addProductBadgeCollect(products,listings, lang, site, currency);
		return views.html.interaction.fragment.recent_history.render(products);
	}

}
