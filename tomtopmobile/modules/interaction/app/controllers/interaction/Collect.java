package controllers.interaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.common.collect.Collections2;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.utils.DateFormatUtils;
import services.cart.ICartLifecycleService;
import services.interaction.ICollectService;
import services.member.login.ILoginService;
import services.product.ICategoryEnquiryService;
import services.product.IProductBadgeService;
import services.product.ProductUtilService;
import valueobjects.base.Page;
import valueobjects.product.ProductBadge;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dto.interaction.ProductCollect;
import extensions.base.IShareProvider;

public class Collect extends Controller {

	@Inject
	ICollectService collectService;

	@Inject
	FoundationService foundation;

	@Inject
	ILoginService loginService;
	@Inject
	ICartLifecycleService cartLifecycle;
	@Inject
	IProductBadgeService badgeService;
	@Inject
	ICategoryEnquiryService categoryEnquiryService;

	@Inject
	ProductUtilService productUtilService;

	@Inject
	Set<IShareProvider> shareProviders;

	public static final int START_PAGE = 1;

	public static final int PAGE_SIZE = 30;

	@BodyParser.Of(BodyParser.Json.class)
	public Result collect(String lid, String action) {
		boolean flag = false;
		String email = "";
		if (foundation.getLoginContext().isLogin()) {
			email = foundation.getLoginContext().getMemberID();
		} else {
			return ok("{\"result\":\"nologin\"}");
		}
		// add all
		if ("addall".equals(action) && !"".equals(lid)) {
			String[] listingArr = lid.split(",");
			List<String> listingids = Lists.newArrayList(listingArr);
			listingids = ImmutableSet.copyOf(listingids).asList();
			List<String> existlist = collectService
					.getCollectListingIDByEmail(email);
			listingids = Lists.newArrayList(Collections2.filter(listingids,
					list -> !existlist.contains(list)));
			for (String ids : listingids) {
				collectService.addCollect(ids, email);
			}
			flag = true;
		}
		if ("add".equals(action) && lid != null && !"".equals(lid)) {
			List<ProductCollect> clist = collectService.getCollectByMember(lid,
					email);
			if (clist.size() > 0) {
				return ok("{\"result\":\"Has the collection\"}");
			}
			flag = collectService.addCollect(lid, email);
		} else if ("del".equals(action) && lid != null && !"".equals(lid)) {
			flag = collectService.delCollect(lid, email);
		}
		if (flag) {
			int count = 0;
			List<String> list = collectService
					.getCollectListingIDByEmail(email);
			if (null != list) {
				count = list.size();
			}
			return ok("{\"result\":\"success\" ,\"count\": " + count + "}");
		} else {
			return ok("{\"result\":\"error\"}");
		}
	}

	// @Authenticated(MemberLoginAuthenticator.class)
	public Result delCollect(String lids) {
		String email = foundation.getLoginContext().getMemberID();
		boolean flag = false;
		if (lids != null && !"".equals(lids)) {
			flag = collectService.delCollectByListingids(lids, email);
		}
		if (flag) {
			return ok("{\"result\":\"success\"}");
		} else {
			return ok("{\"result\":\"error\"}");
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result getcollect(String listingid) {
		String email = "";
		if (foundation.getLoginContext().isLogin()) {
			email = foundation.getLoginContext().getMemberID();
		} else {
			Object[] o = new Object[] { "nologin" };
			return ok(Json.toJson(o));
		}
		List<String> list = collectService.getCollectListingIDByEmail(email);
		// 计算商品收藏数量
		if (listingid != null && !"".equals(listingid)) {
			int count = collectService.getCountByListingID(listingid);
			Object[] o = new Object[] { list, count };
			return ok(Json.toJson(o));
		}

		return ok(Json.toJson(list));
	}

	/**
	 * 加载收藏信息
	 * 
	 * @return
	 */
	public Result wishList() {

		Page<ProductBadge> badgePage = getWishlist(START_PAGE, PAGE_SIZE);

		return ok(views.html.interaction.wishlist.product_wishlist
				.render(badgePage));

	}

	/**
	 * 加载收藏信息，用于编辑
	 * 
	 * @return
	 */
	public Result editWishlist() {
		Page<ProductBadge> badgePage = getWishlist(START_PAGE, PAGE_SIZE);
		return ok(views.html.interaction.wishlist.wishlist_editor
				.render(badgePage));
	}

	/**
	 * 异步加载收藏信息
	 * 
	 * @return
	 */
	public Result wishAsynlist(Integer page) {
		String email = foundation.getLoginContext().getMemberID();
		List<String> wishList = collectService
				.getCollectListingIDByEmail(email);
		Integer pagenum = page;
		List<ProductBadge> badgePage = getWishlist(pagenum, PAGE_SIZE)
				.getList();

		Map<String, Object> map = new HashMap<String, Object>();
		Integer totlePage = (wishList.size() - 1) / PAGE_SIZE + 1;
		map.put("totlePage", totlePage);
		map.put("html", views.html.product.product_more_badge.render(badgePage)
				.toString());
		return ok(Json.toJson(map));
	}

	/**
	 * 异步加载收藏信息，用于编辑
	 * 
	 * @return
	 */
	public Result wishAsynlistEdit(Integer page) {
		String email = foundation.getLoginContext().getMemberID();
		List<String> wishList = collectService
				.getCollectListingIDByEmail(email);
		Integer pagenum = page;
		List<ProductBadge> badgePage = getWishlist(pagenum, PAGE_SIZE)
				.getList();

		Map<String, Object> map = new HashMap<String, Object>();
		Integer totlePage = (wishList.size() - 1) / PAGE_SIZE + 1;
		map.put("totlePage", totlePage);
		map.put("html", views.html.interaction.wishlist.wishedit_more_badge
				.render(badgePage).toString());
		return ok(Json.toJson(map));
	}

	private Page<ProductBadge> getWishlist(Integer page, Integer limmit) {
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();
		String email = foundation.getLoginContext().getMemberID();

		Page<ProductCollect> collects = collectService.getCollectsPageByEmail(
				email, page, limmit, lang, "date", null, null);
		List<String> listingid = Lists.transform(collects.getList(),
				l -> l.getClistingid());
		HashMap<String, String> datemap = new HashMap<String, String>();
		for (ProductCollect c : collects.getList()) {
			datemap.put(c.getClistingid(),
					DateFormatUtils.getDateMDY(c.getDcreatedate()));
		}
		Page<String> listingids = new Page<String>(listingid,
				collects.totalCount(), collects.pageNo(), collects.pageSize());
		
		Page<ProductBadge> badgePage = listingids.batchMap(list -> badgeService
				.getProductBadgesByListingIDs(list, lang, site, currency, null,
						false, false));
		//产品加入收藏
		productUtilService.addProductBadgeCollect(badgePage.getList(),
				listingids.getList(), lang, site, currency);
		//产品加入评论
		productUtilService.addReview(badgePage.getList(), listingids.getList(),
				lang, site, currency);
		
		return badgePage;
	}

}