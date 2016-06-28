package services.home.fragment;

import java.util.List;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.product.IHomePageDataEnquiry;

import com.google.inject.Inject;

import extensions.product.template.AbstractListingTemplateProvider;

public class FeaturedItemsProvider extends AbstractListingTemplateProvider {

	@Inject
	IHomePageDataEnquiry homePageDataEnquiry;

	@Inject
	FoundationService foundation;

	@Override
	public String getName() {
		return "featured-items";
	}

	@Override
	public List<String> getListingIds(Context context) {
		int siteid = 1;
		int lang = 1;
		if (context != null) {
			siteid = foundation.getSiteID(context);
			lang = foundation.getLanguage(context);
		}
		int page = 0;// 表示第一页
		return homePageDataEnquiry.getFeaturedItemsListingIds(siteid, lang,
				page);
	}

	@Override
	public Html getHeader() {
		return null;
	}

	@Override
	public Html getFooter() {
		return null;
	}

	@Override
	public Html getStartlabel() {
		return Html.apply("<li>");
	}

	@Override
	public Html getEndlabel() {
		return Html.apply("</li>");
	}
}
