package services.product.homepage;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.base.FoundationService;
import services.product.IProductBadgeService;
import services.product.ProductUtilService;
import valueobjects.product.ProductBadge;

public abstract class AbstractHomepage {

	@Inject
	IProductBadgeService badge;

	@Inject
	FoundationService foundation;

	@Inject
	ProductUtilService productUtilService;

	public static final int INTEX_START_PAGE = 0;

	public abstract List<String> getListIds(Context context);

	public List<ProductBadge> getProductBadges(Context context) {

		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		String currency = foundation.getCurrency();
		List<ProductBadge> badges = badge.getProductBadgesByListingIDs(
				getListIds(context), language, website, currency, null);
		// 将产品加入收藏
		productUtilService.addProductBadgeCollect(badges, getListIds(context),
				language, website, currency);
		// 产品加入评论
		productUtilService.addReview(badges, getListIds(context), language,
				website, currency);
		return badges;
	}
}
