package extensions.product.template;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.product.IProductBadgeService;
import valueobjects.product.ProductBadge;

public abstract class AbstractListingTemplateProvider implements
		ITemplateFragmentProvider {

	@Inject
	IProductBadgeService badge;

	@Inject
	FoundationService foundation;

	public abstract List<String> getListingIds(Context context);

	public abstract Html getHeader();

	public abstract Html getFooter();

	/**
	 * 插入一些特定的标签
	 */
	public abstract Html getStartlabel();

	/**
	 * 便签结束
	 */
	public abstract Html getEndlabel();

	@Override
	public Html getFragment(Context context) {
		List<ProductBadge> badges = badge.getProductBadgesByListingIDs(
				getListingIds(context), foundation.getLanguage(context),
				foundation.getSiteID(context), foundation.getCurrency(context),
				null);

		return views.html.home.product_box.render(badges, getHeader(),
				getFooter(), getStartlabel(), getEndlabel());
	}

}
