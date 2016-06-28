package services.home.fragment;

import java.util.List;
import java.util.Map;

import mapper.product.CategoryLabelNameMapper;
import mapper.product.CategoryNameMapper;
import play.Logger;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import valueobjects.product.category.CategoryItem;
import valueobjects.product.category.HotCategory;

import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.product.CategoryLabelName;

public class HotCategoriesProvider implements ITemplateFragmentProvider {

	@Inject
	CategoryNameMapper categoryNameMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	CategoryLabelNameMapper categoryLabelNameMapper;

	@Override
	public String getName() {
		return "hot-categories";
	}

	@Override
	public Html getFragment(Context context) {
		int site = 1;
		int language = 1;
		if (context != null) {
			site = foundationService.getSiteID(context);
			language = foundationService.getLanguage(context);
		}
		List<HotCategory> hotCategories = Lists.newArrayList();
		List<CategoryItem> hotFirstCategoryNames = categoryNameMapper
				.getHotFirstCategoryNames(language, site);

		if (null != hotFirstCategoryNames && hotFirstCategoryNames.size() > 0) {
			for (CategoryItem hotFirstCategory : hotFirstCategoryNames) {
				List<Map<String, String>> secondCategoryNameAndPathMaps = com.google.common.collect.Lists
						.newLinkedList();
				List<CategoryItem> secondCategorys = categoryNameMapper
						.getHotSecondCategoryNames(hotFirstCategory.getId(),
								site, language);
				CategoryLabelName categoryLabelName = categoryLabelNameMapper
						.getByCategoryIdAndSiteIdAndLangId(
								hotFirstCategory.getId(), site, language);
				String imgUrl = "";
				Integer labelNameId = null;
				String prompt = "";
				if (null != categoryLabelName) {
					imgUrl = categoryLabelName.getCurl();
					labelNameId = categoryLabelName.getIid();
					prompt = categoryLabelName.getCprompt();
				}
				Map<String, String> secondCategoryNameAndPathMap = Maps
						.newLinkedHashMap();
				if (null != secondCategorys && secondCategorys.size() > 0) {
					for (CategoryItem categoryItem : secondCategorys) {
						secondCategoryNameAndPathMap.put(
								categoryItem.getName(), categoryItem.getPath());
					}
					secondCategoryNameAndPathMaps
							.add(secondCategoryNameAndPathMap);
				}
				hotCategories.add(new HotCategory(hotFirstCategory.getId(),
						hotFirstCategory.getName(), hotFirstCategory.getPath(),
						imgUrl, labelNameId, prompt,
						secondCategoryNameAndPathMaps));
			}
		}
		return views.html.home.hot_categories.render(hotCategories);
	}
}
