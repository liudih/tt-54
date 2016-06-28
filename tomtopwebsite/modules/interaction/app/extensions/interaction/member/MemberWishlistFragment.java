package extensions.interaction.member;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.utils.DateFormatUtils;
import services.base.FoundationService;
import services.interaction.collect.CollectService;
import services.member.login.LoginService;
import services.product.IProductBadgeService;
import valueobjects.base.Page;
import valueobjects.member.MemberInSession;
import valueobjects.product.ProductBadge;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import dto.interaction.ProductCollect;
import extensions.interaction.share.IShareProvider;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class MemberWishlistFragment implements
		IMemberAccountHomeFragmentProvider {
	@Inject
	CollectService collectService;
	@Inject
	FoundationService foundation;
	@Inject
	LoginService loginService;
	@Inject
	IProductBadgeService badgeService;
	@Inject
	Set<IShareProvider> shareProviders;
	
	@Override
	public Position getPosition() {
		return Position.BODY;
	}

	@Override
	public int getDisplayOrder() {
		return 20;
	}

	@Override
	public Html getFragment(MemberInSession member) {
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();
		String email = "";
		if (foundation.getLoginContext().isLogin()) {
			email = loginService.getLoginData().getEmail();
		}else{
			return Html.apply("");
		}
		Page<ProductCollect> collects = collectService.getCollectsPageByEmail(email,1,3,lang,null,null,null);
		List<String> listingid = Lists.transform(collects.getList(),l->l.getClistingid());
		HashMap<String,String> datemap = new HashMap<String,String>();
		for(ProductCollect c : collects.getList()){
			datemap.put(c.getClistingid(),DateFormatUtils.getDateMDY(c.getDcreatedate()));
		}
		Page<String> listingids = new Page<String>(listingid, collects.totalCount(), collects.pageNo(), collects.pageSize());
		Page<ProductBadge> badgePage = listingids.batchMap(list -> badgeService
				.getProductBadgesByListingIDs(list, lang, site, currency,null,false,true));
		for(ProductBadge b : badgePage.getList()){
			if(b!=null){
				b.setCollectDate(datemap.get(b.getListingId()));
			}
		}
		List<IShareProvider> shareProviderList =  FluentIterable
				.from(Ordering
						.natural()
						.onResultOf((IShareProvider lp) -> lp.getDisplayOrder())
						.sortedCopy(shareProviders)).toList();
		return views.html.interaction.collect.wishlist_home.render(badgePage,shareProviderList);
	}

}
