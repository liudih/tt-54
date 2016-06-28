package services.home.fragment;

import java.util.List;

import mapper.product.ProductBaseMapper;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.product.IHomePageDataEnquiry;
import services.product.IHomePageProductShowHistoryService;
import valueobjects.base.Page;

import com.google.inject.Inject;

import extensions.product.template.AbstractListingTemplateProvider;

public class HotSalesProvider extends AbstractListingTemplateProvider {

	@Inject
	IHomePageDataEnquiry homePageDataEnquiry;

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	FoundationService foundation;

	@Inject
	IHomePageProductShowHistoryService homePageShowHistoryService;

	@Override
	public String getName() {
		return "hot-sales";
	}

	@Override
	public List<String> getListingIds(Context context) {
		int page = 0; // 取第一页
		return homePageDataEnquiry.getHotSalesListingIds(
				foundation.getSiteID(context), foundation.getLanguage(context),
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
