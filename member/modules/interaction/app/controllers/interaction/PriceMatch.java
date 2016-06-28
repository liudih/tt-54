package controllers.interaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.FoundationService;
import services.base.captcha.CaptchaService;
import services.interaciton.review.ProductReviewsService;
import services.interaction.PriceMatchService;
import services.member.login.LoginService;
import services.product.IProductBadgeService;
import services.product.ProductBaseTranslateService;
import valueobjects.base.Page;
import valueobjects.interaction.PriceBadge;
import authenticators.member.MemberLoginAuthenticator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.product.ProductImage;
import dto.product.ProductTranslate;
import dto.product.ProductUrl;
import forms.interaction.PriceMatchForm;

public class PriceMatch extends Controller {

	@Inject
	PriceMatchService priceMatchService;
	
	@Inject
	FoundationService foundation;
	
	@Inject
	LoginService loginService;
	
	@Inject
	IProductBadgeService badgeService;
	
	@Inject
	ProductReviewsService productService;
	
	@Inject
	ProductBaseTranslateService updateService;
	@Inject
	CaptchaService captchaService;
	
	public Result addData() {
		Map<String,Object> mjson = new HashMap<String,Object>();
		mjson.put("result", "error");
		String captcha = request().body().asFormUrlEncoded().get("captcha")[0];
		if(!captchaService.verify(captcha)){
			mjson.put("result", "wrongcaptcha");
			return ok(Json.toJson(mjson)); 
		}
		
		play.data.Form<PriceMatchForm> userForm = Form.form(PriceMatchForm.class).bindFromRequest();
		if(userForm.hasErrors()){
			mjson.put("result", "Data cannot be empty!");
			return ok(Json.toJson(mjson));
		}
		boolean flag = priceMatchService.addPriceMatch(userForm.get());
		if(flag){
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result priceMatchResult(int page,int limit,String filter,int da){
		String email = "";
		int iwebsiteid=foundation.getSiteID();
		if (foundation.getLoginContext().isLogin()) {
			email = loginService.getLoginData().getEmail();
		}else{
			return TODO;
		}
		Page<dto.interaction.PriceMatch> collects=priceMatchService.getPriceMatchByEmail(email, iwebsiteid,limit,page,filter,da);
		List<String> listingList = Lists.transform(collects.getList(), obj -> {
			return obj.getClistingid();
		});
		if(listingList.size()!=0){
			List<ProductImage> imgs=productService.getImageUrlByClistingid(listingList);
			Map<String, String> mimgurl = Maps.newHashMap();
			for (ProductImage b : imgs) {
				mimgurl.put(b.getClistingid(), b.getCimageurl());
			}
			List<ProductTranslate> translates=updateService.getTitleByClistingids(listingList);
			Map<String,String> mtitle=Maps.newHashMap();
			for(ProductTranslate t : translates){
				mtitle.put(t.getClistingid(),t.getCtitle());
			}
			List<ProductUrl> productUrls=updateService.getUrlByClistingIds(listingList);
			Map<String,String> murls=Maps.newHashMap();
			for(ProductUrl u: productUrls){
				murls.put(u.getClistingid(),u.getCurl());
			}
			PriceBadge pBadge=new PriceBadge(collects, mimgurl,mtitle,murls);
			return ok(views.html.interaction.collect.priceMatch.render(pBadge,filter,da));
		}
		else{
			PriceBadge pBadge=new PriceBadge(collects,null,null,null);
			return ok(views.html.interaction.collect.priceMatch.render(pBadge,filter,da));
		}
	}
}
