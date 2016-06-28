package services.dodocool.banner;

import java.util.ArrayList;
import java.util.List;

import play.libs.F.Promise;
import play.Logger;
import play.Play;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import scala.Array;
import services.IAdvertisingService;
import services.dodocool.base.FoundationService;
import services.dodocool.base.template.ITemplateFragmentProvider;
import valueobjects.product.AdItem;












import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.twelvemonkeys.lang.StringUtil;

import context.ContextUtils;
import context.WebContext;
import dto.advertising.ProductAdertisingContextExtended;

public class HomeBannerServiceFragmentProvider implements ITemplateFragmentProvider {

	@Inject
	IAdvertisingService advertisingService;
	
	@Inject
	FoundationService foundation;
	
	@Override
	public String getName() {
		return "home_banner";
	}

	@Override
	public Html getFragment(Context context) {
		int siteID = foundation.getSiteID();
		int client = 9; // dodocool 
		if(siteID==11){
			client = 14; //ammmoon
		}
		int lang = foundation.getLanguageId();
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		ProductAdertisingContextExtended ctx = new ProductAdertisingContextExtended(
				null, 5, 3, webContext);
//		List<AdItem> banners = advertisingService.getAdvertisingsExtended(ctx);
		List<AdItem> banners = new ArrayList<AdItem>();
		String url = "http://advert.api.tomtop.com/ic/v2/base/banners_content?layoutCode=HOME&bannerCode=BANNER-SLIDER&client="+client+"&lang="+lang;
		Logger.debug("before send get date- url="+url);
		String result = get(url);
		Logger.debug("result:"+result); 
		if(StringUtil.isEmpty(result)) return null;
		String ret =JSON.parseObject(result).getString("ret");
		if("0".equals(ret))return null;
		JSONObject jsonobject =JSON.parseObject(result).getJSONObject("data");
		if(jsonobject==null) return null;
		JSONArray jsonArr = jsonobject.getJSONArray("BANNER-SLIDER");
		if(jsonArr==null || jsonArr.size()<1) return null;
		for(int i=0;i<=jsonArr.size()-1;i++){
			JSONObject jsonO = (JSONObject) jsonArr.get(i);
			AdItem asItem = new AdItem(jsonO.getString("title"), jsonO.getString("imgUrl"), jsonO.getString("url"),"");
			banners.add(asItem);
		}
		Logger.debug("banners:"+banners); 
		return views.html.home.banner.render(banners);
	}
	
	
	public static String get(String url) {

		WSRequestHolder wsRequest = WS.url(url).setHeader("Content-Type",
				"application/json");
		Promise<String> resultStr = wsRequest.get().map(response -> {
			return response.getBody();
		});
		return resultStr.get(100000);
	}


}
