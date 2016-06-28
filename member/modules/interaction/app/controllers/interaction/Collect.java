package controllers.interaction;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.elasticsearch.common.collect.Collections2;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.utils.DateFormatUtils;
import services.base.FoundationService;
import services.cart.ICartLifecycleService;
import services.interaction.collect.CollectService;
import services.member.login.LoginService;
import services.product.CategoryEnquiryService;
import services.product.IProductBadgeService;
import valueobjects.base.Page;
import valueobjects.product.ProductBadge;
import valueobjects.product.category.CategoryComposite;
import authenticators.member.MemberLoginAuthenticator;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.inject.Inject;

import dto.interaction.ProductCollect;
import extensions.interaction.share.IShareProvider;

public class Collect extends Controller {

	@Inject
	CollectService collectService;
	
	@Inject
	FoundationService foundation;
	
	@Inject
	LoginService loginService;
	@Inject
	ICartLifecycleService cartLifecycle;
	@Inject
	IProductBadgeService badgeService;
	@Inject
	CategoryEnquiryService categoryEnquiryService;
	@Inject
	Set<IShareProvider> shareProviders;

	@BodyParser.Of(BodyParser.Json.class)
	public Result collect(String lid,String action){
		boolean flag = false;
		String email = "";
		if (foundation.getLoginContext().isLogin()) {
			email = loginService.getLoginData().getEmail();
		}else{
			return ok("{\"result\":\"nologin\"}");
		}
		//add all
		if("addall".equals(action) && !"".equals(lid)){
			String[] listingArr = lid.split(",");
			List<String> listingids = Lists.newArrayList(listingArr);
			listingids = ImmutableSet.copyOf(listingids).asList();
			List<String> existlist = collectService.getCollectListingIDByEmail(email);
			listingids = Lists.newArrayList(Collections2.filter(listingids,list-> !existlist.contains(list)));
			for(String ids: listingids){
				collectService.addCollect(ids, email);
			}
			flag = true;
		}
		if("add".equals(action) && lid!=null && !"".equals(lid)){
			List<ProductCollect> clist = collectService.getCollectByMember(lid, email);
			if(clist.size()>0){
				return ok("{\"result\":\"Has the collection\"}");
			}
			flag = collectService.addCollect(lid, email);
		}else if("del".equals(action) && lid!=null && !"".equals(lid)){
			flag = collectService.delCollect(lid, email);
		}
		if(flag){
			return ok("{\"result\":\"success\"}");
		}else{
			return ok("{\"result\":\"error\"}");
		}
	}
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result delCollect(String lids){
		String email = loginService.getLoginData().getEmail();
		boolean flag = false;
		if(lids!=null && !"".equals(lids)){
			flag = collectService.delCollectByListingids(lids, email);
		}
		if(flag){
			return ok("{\"result\":\"success\"}");
		}else{
			return ok("{\"result\":\"error\"}");
		}
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public Result getcollect(String listingid){
		String email = "";
		if (foundation.getLoginContext().isLogin()) {
			email = loginService.getLoginData().getEmail();
		}else{
			Object[] o = new Object[] {"nologin"};
			return ok(Json.toJson(o));
		}
		List<String> list = collectService.getCollectListingIDByEmail(email);
		//计算商品收藏数量
		if(listingid!=null && !"".equals(listingid)){
			int count = collectService.getCountByListingID(listingid);
			Object[] o = new Object[] { list, count };
			return ok(Json.toJson(o));
		} 
		
		return ok(Json.toJson(list));
	}
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result wishList(int page,int limit,Integer categoryId,String sort,String searchname){
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();
		String email = "";
		if (foundation.getLoginContext().isLogin()) {
			email = loginService.getLoginData().getEmail();
		}else{
			return badRequest("You have not logged on");
		}
		//所有的一级类目
		List<CategoryComposite> categorys = categoryEnquiryService.getRootCategories(lang,site);
				
		Page<ProductCollect> collects = collectService.getCollectsPageByEmail(email,page,limit,lang,sort,searchname,categoryId);
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
			
			
		return ok(views.html.interaction.collect.wishlist.render(badgePage,categorys,
				categoryId,sort,searchname,shareProviderList));
	}

}