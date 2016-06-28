package services.home.fragment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mapper.product.CategoryNameMapper;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.product.IHomePageDataEnquiry;
import services.product.IProductBadgeService;
import valueobjects.base.Page;
import valueobjects.product.ProductBadge;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dto.product.CategoryName;

public class NewArrivalsProvider implements ITemplateFragmentProvider {

	@Inject
	IHomePageDataEnquiry homePageDataEnquiry;

	@Inject
	FoundationService foundation;

	@Inject
	IProductBadgeService productBadgeService;

	@Inject
	CategoryNameMapper categoryNameMapper;

	@Override
	public String getName() {
		return "new-arrivals";
	}

	@Override
	public Html getFragment(Context context) {
		int site = 1;
		int language = 1;
		if (context != null) {
			site = foundation.getSiteID(context);
			language = foundation.getLanguage(context);
		}
		List<CategoryName> categoryNames = categoryNameMapper
				.getNewArrivalsCategoryNames(language, site);
		Map<Integer, String> newArrialsCategorysMap = new LinkedHashMap<Integer, String>();
		if (null != categoryNames && categoryNames.size() > 0) {
			for (CategoryName categoryName : categoryNames) {
				newArrialsCategorysMap.put(categoryName.getIcategoryid(),
						categoryName.getCname());
			}
		}

		Map<String, List<ProductBadge>> newArrialsCategorysProductMap = new LinkedHashMap<String, List<ProductBadge>>();
		Integer limit = 5; //在首页前台页面每个二级品类只需要显示5个产品
		for (Integer categoryId : newArrialsCategorysMap.keySet()) {
			Page<String> listingIds = homePageDataEnquiry
					.getNewArrialListingIdsByCategoryId(categoryId, site,
							language);
			List<ProductBadge> kkBadges = Lists.newArrayList();
			if (null != listingIds.getList() && listingIds.getList().size() == limit) {
				kkBadges = productBadgeService.getProductBadgesByListingIDs(
						listingIds.getList(), foundation.getLanguage(context),
						foundation.getSiteID(context),
						foundation.getCurrency(context), null);
				newArrialsCategorysProductMap.put(
						newArrialsCategorysMap.get(categoryId), kkBadges);
			}
			if (newArrialsCategorysProductMap.size() >= 8) {
				break;
			}
		}
		return views.html.home.new_arrivals
				.render(newArrialsCategorysProductMap);
	}
}
