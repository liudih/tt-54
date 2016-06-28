package controllers.affiliate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Hex;

import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import service.tracking.IAffiliateService;
import services.base.FoundationService;
import services.base.captcha.CaptchaService;
import services.member.login.LoginService;
import valueobjects.base.Page;
import authenticators.member.MemberLoginAuthenticator;
import entity.tracking.AffiliateBanner;
import entity.tracking.AffiliateInfo;
import forms.tracking.AffiliateForm;

public class AffiliateHome extends Controller {
	@Inject
	FoundationService foundation;
	@Inject
	CaptchaService captchaService;
	@Inject
	IAffiliateService affiliateService;
	@Inject
	LoginService loginService;
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result home(){
		String aid = getAid();
		boolean isHaveAid = aid!=null;
		return ok(views.html.affiliate.affiliate_home.render(isHaveAid));
	}
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result affiliateAd(int page,int limit){
		String aid = getAid();
		if(aid==null){
			return redirect(controllers.affiliate.routes.AffiliateHome.Activate(
					controllers.affiliate.routes.AffiliateHome.affiliateAd(1,10).toString()));
		}
		int siteid = foundation.getSiteID();
		int lang = foundation.getLanguage();
		Page<AffiliateBanner> list = affiliateService.getAffiliateBannerPage(lang,siteid,page,limit,1);
		Page<AffiliateBanner> list2 = affiliateService.getAffiliateBannerPage(lang,siteid,page,limit,2);
		
		return ok(views.html.affiliate.affiliate_ad.render(list,list2,aid));
	}
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result affiliateAdAjax(int page,int limit,int type){
		String aid = getAid();
		Map<String,Object> mjson = new HashMap<String,Object>();
		int siteid = foundation.getSiteID();
		int lang = foundation.getLanguage();
		Page<AffiliateBanner> list = affiliateService.getAffiliateBannerPage(lang,siteid,page,limit,type);
		String html = "";
		if(type==1){
			html = views.html.affiliate.affiliate_ad_list.render(list.getList(),aid).toString();
		}else if(type==2){
			html = views.html.affiliate.affiliate_ad_list_text.render(list.getList(),aid).toString();
		}
		html = html.replaceAll("[[\n][\r]]", "");
		mjson.put("page", page);
		mjson.put("totalpage", list.totalPages());
		mjson.put("html", html);
		
		return ok(Json.toJson(mjson));
	}
	
	public Result getHtml(Integer bid,String aid){
		AffiliateBanner b = affiliateService.getAffiliateBannerById(bid);
		return ok(views.html.affiliate.ad_html.render(b,aid));
	}
	
	public Result homeNotLogin(){
		if (foundation.getLoginContext().isLogin()) {
			return redirect(controllers.affiliate.routes.AffiliateHome.affiliateAd(1,10));
		}
		return ok(views.html.affiliate.affiliate_home_nologin.render());
	}
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result Activate(String backurl){
		return ok(views.html.affiliate.affiliate_activate.render(backurl));
	}
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result addData(){
		int siteid = foundation.getSiteID();
		Map<String,Object> mjson = new HashMap<String,Object>();
		mjson.put("result", "error");
		String captcha = request().body().asFormUrlEncoded().get("captcha")[0];
		if(!captchaService.verify(captcha)){
			mjson.put("result", "wrongcaptcha");
			return ok(Json.toJson(mjson)); 
		}
		
		play.data.Form<AffiliateForm> userForm = Form.form(AffiliateForm.class).bindFromRequest();
		if(userForm.hasErrors()){
			Iterator err = userForm.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry= (Entry<String, List<ValidationError>>) err.next();  
			mjson.put("result", entry.getKey()+":  "+entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}
		AffiliateForm form = userForm.get();
		form.setIwebsiteid(siteid);
		//判断Aid的有效性
		String res = affiliateService.checkValidAId(form);
		if(res!=null && !"success".equals(res)){
			mjson.put("result", res);
			return ok(Json.toJson(mjson));
		}
		boolean flag = affiliateService.addAffilateInfo(form);
		if(flag){
			mjson.put("result", "success");
		}
		//判断是否有返回url
		String backurl = request().body().asFormUrlEncoded().get("backurl")[0];
		if(backurl!=null && !"".equals(backurl)){
			mjson.put("backurl", backurl);
		}
		return ok(Json.toJson(mjson));
	}
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result Setting(){
		String aid = getAid();
		if(aid==null){
			return redirect(controllers.affiliate.routes.AffiliateHome.Activate(
					controllers.affiliate.routes.AffiliateHome.Setting().toString()));
		}
		String email = "";
		if (loginService.getLoginData()!=null) {
			email = loginService.getLoginData().getEmail();
		}
		AffiliateInfo a = affiliateService.getAffiliateInfoByEmail(email);
		return ok(views.html.affiliate.affiliate_setting.render(email,a));
	}
	
	public Result updateInfo(){
		Map<String,Object> mjson = new HashMap<String,Object>();
		mjson.put("result", "error");
		String captcha = request().body().asFormUrlEncoded().get("captcha")[0];
		if(!captchaService.verify(captcha)){
			mjson.put("result", "wrongcaptcha");
			return ok(Json.toJson(mjson)); 
		}
		play.data.Form<AffiliateForm> userForm = Form.form(AffiliateForm.class).bindFromRequest();
		if(userForm.hasErrors()){
			Iterator err = userForm.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry= (Entry<String, List<ValidationError>>) err.next();  
			mjson.put("result", entry.getKey()+":  "+entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}
		AffiliateForm form = userForm.get();
		
		boolean flag = affiliateService.updateAffiliateInfo(form);
		if(flag){
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}
	public Result getBannerImage(Integer iid) {
		AffiliateBanner b = affiliateService.getBannerById(iid);

		if (null != b && null != b.getBbannerfile()) {
			String etag = generateETag(b.getBbannerfile());
			String previous = request().getHeader(IF_NONE_MATCH);
			if (etag != null && etag.equals(previous)) {
				return status(NOT_MODIFIED);
			}
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, etag);
			return ok(b.getBbannerfile()).as("image/png");
		}
		return badRequest();
	}
	
	private String generateETag(byte[] values) {
		if (values != null) {
			return md5(values);
		}
		return null;
	}

	private String md5(byte[] value) {
		try {
			String md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5")
					.digest(value));
			return md5;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Should not happen", e);
		}
	}
	private String getAid(){
		String email = "";
		if (loginService.getLoginData()!=null) {
			email = loginService.getLoginData().getEmail();
		}
		AffiliateInfo a = affiliateService.getAffiliateInfoByEmail(email);
		return a!=null ? a.getCaid() : null;
	}
}
