package services.mobile.product;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import services.mobile.MobileService;
import services.product.ICategoryEnquiryService;
import valueobjects.product.category.CategoryComposite;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import dto.mobile.CategoryCompositeInfo;
import dto.mobile.CategoryInfo;
import dto.product.CategoryWebsiteWithName;

public class CategoryService {

	@Inject
	ICategoryEnquiryService categoryEnquiryService;

	@Inject
	MobileService mobileService;

	/**
	 * 查询根节点 分类信息
	 * 
	 * @param max
	 * @param websiteid
	 * @param languageid
	 * @return
	 */
	public List<CategoryInfo> getRootCategorys(int max) {
		List<CategoryWebsiteWithName> categorys = categoryEnquiryService
				.getCategoryItemRootByDisplay(mobileService.getWebContext(),
						true);
		if (categorys != null && categorys.size() > 0) {
			List<CategoryInfo> resultList = new ArrayList<CategoryInfo>();
			for (CategoryWebsiteWithName category : categorys) {
				if (max < category.getIid()) {
					resultList.add(new CategoryInfo(category.getIparentid(),
							category.getCname(), category.getIcategoryid(), 1));
				}
			}
			return resultList;
		}
		return null;
	}

	/**
	 * 查询 二三 级节点 分类信息
	 * 
	 * @param cId
	 * @param max
	 * @param depth
	 * @param websiteid
	 * @param languageid
	 * @return
	 */
	public List<CategoryCompositeInfo> getCategorysByParentId(int cid, int max,
			int depth) {
		List<CategoryComposite> list = categoryEnquiryService
				.getNewChildCategory(cid, max, mobileService.getWebContext(),
						depth);
		if (list != null && list.size() > 0) {
			List<CategoryCompositeInfo> resultList = convertData(max, depth,
					list);
			return resultList;
		}
		return null;
	}

	private List<CategoryCompositeInfo> convertData(int max, int depth,
			List<CategoryComposite> data) {
		List<CategoryCompositeInfo> resultList = Lists.transform(data,
				new Function<CategoryComposite, CategoryCompositeInfo>() {
					@Override
					public CategoryCompositeInfo apply(
							CategoryComposite category) {
						if (depth == 0) {
							return new CategoryCompositeInfo(new CategoryInfo(
									category.getSelf().getIparentid(), category
											.getSelf().getCname(), category
											.getSelf().getIcategoryid(), 3),
									new LinkedList<CategoryCompositeInfo>());
						}
						return new CategoryCompositeInfo(new CategoryInfo(
								category.getSelf().getIparentid(), category
										.getSelf().getCname(), category
										.getSelf().getIcategoryid(), 2),
								convertData(max, depth - 1,
										category.getChildren()));
					}
				});
		return resultList;
	}
}
