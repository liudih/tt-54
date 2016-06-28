package services.cart.member.fragment;


import java.util.List;

import javax.inject.Inject;

import play.Logger;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.cart.ICartBuilderService;
import services.cart.base.template.ITemplateFragmentProvider;
import services.interaction.IMemberBrowseHistoryService;
import services.product.IProductBadgeService;
import valueobjects.base.LoginContext;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartGetRequest;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;
import valueobjects.interaction.MemberBrowseHistory;
import valueobjects.product.ProductBadge;

import com.google.common.collect.Lists;
import com.google.common.collect.Collections2;

import dto.interaction.ProductBrowse;
import facade.cart.Cart;

public class MemberBrowseHistoryTemplateProvider implements
		ITemplateFragmentProvider {

	@Inject
	FoundationService foundation;

	@Inject
	IMemberBrowseHistoryService historyService;

	@Inject
	IProductBadgeService badgeService;
	
	@Inject
	ICartBuilderService cartLifecycle;
	

	@Override
	public String getName() {
		return "member-browse-history-in-member-center";
	}

	@Override
	public Html getFragment(Context context) {
		
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();
		List<ProductBrowse> blist = historyService.getMemberBrowseHistoryByContext(foundation.getWebContext(),
				20,foundation.getLoginContext().isLogin());
		//获得购物车产品listingids
		Cart cart = getCurrentCart();
		List<CartItem> cartItems = cart.getAllItems();
		List<String> cartListingids = Lists.newArrayList();
		for(CartItem ci : cartItems){
			if (ci instanceof SingleCartItem) {
				cartListingids.add(ci.getClistingid());
			} else if (ci instanceof BundleCartItem) {
				for(SingleCartItem si : ((BundleCartItem) ci).getChildList()){
					cartListingids.add(si.getClistingid());
				}
			}
		}
		List<String> listings = Lists.transform(blist, b -> b.getClistingid());
		
		//过滤cart的产品
		listings = Lists.newArrayList(Collections2.filter(listings, l -> !cartListingids.contains(l)));
				
		List<ProductBadge> products = badgeService.getProductBadgesByListingIDs(
				listings, site, lang, currency, null);

		Integer perPage = 5;
		Integer maxPage = 4;
		String divClass = "xxkBOX accH_box history";
		String divId = "accH_box1";
		String ulClass = "accMovebox";
		String liClass = "accMovePic";
		String nextPageAjaxUrl = null;

		return views.html.cart.home.turn_next_page_model.render(context, products,
				maxPage, perPage, nextPageAjaxUrl, null, divClass, divId,
				ulClass, liClass, false);
	}
	
	/**
	 * 获取Cart对象
	 */
	protected Cart getCurrentCart() {
		String email = null;
		if(foundation.getLoginContext()!=null && foundation.getLoginContext().isLogin()){
			email = foundation.getLoginContext().getMemberID();
		}
		String ltc = foundation.getLoginContext().getLTC();
		int site = foundation.getSiteID();
		int lang = foundation.getLanguage();
		String cur = foundation.getCurrency();
		
		CartGetRequest req = new CartGetRequest(email, ltc, site, lang, cur, "cookie");
		return cartLifecycle.createCart(req);
	}

}
