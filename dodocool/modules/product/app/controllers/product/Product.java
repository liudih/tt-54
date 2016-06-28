package controllers.product;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import base.util.httpapi.ApiUtil;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.dodocool.base.FoundationService;
import services.dodocool.product.ProductCompositeEnquiry;
import services.dodocool.product.ProductCompositeRenderer;
import services.dodocool.product.ProductMessageService;
import services.interaction.ISuperDealService;
import services.product.IProductEnquiryService;
import services.product.IProductUrlService;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import services.search.criteria.ProductLabelType;
import services.search.criteria.ProductTagsCriteria;
import valueobjects.base.Page;
import valueobjects.product.ProductComposite;
import valueobjects.product.ProductContext;
import valueobjects.search.SearchContext;
import context.ContextUtils;
import context.WebContext;
import controllers.base.Home;
import dto.product.ProductMessage;
import dto.product.ProductUrl;

public class Product extends Controller {

	@Inject
	ProductCompositeRenderer productCompositeRenderer;

	@Inject
	ProductCompositeEnquiry compositeEnquiry;

	@Inject
	IProductEnquiryService productEnquiryService;

	@Inject
	IProductUrlService productUrlService;

	@Inject
	FoundationService foundationService;

	@Inject
	ISearchContextFactory factory;

	@Inject
	ISearchService searchService;

	@Inject
	ProductMessageService productMessageService;

	@Inject
	ISuperDealService superDeals;
	
	@Inject
	FoundationService foundation;
	
	
	private final String topSellUrl="http://product.api.tomtop.com/ic/v2/layout/module/contents";

	public Result view(String title) throws MalformedURLException,
			ClassNotFoundException {

		WebContext webContext = ContextUtils.getWebContext(Context.current());
		String listingID = null;
		String sku = null;
		int siteID = foundationService.getSiteID();
		int lang = foundationService.getLanguageId();
		String currency = foundationService.getCurrency();
		ProductUrl productUrl = productUrlService.getProductUrlByUrl(title,
				webContext);
		if (null != productUrl) {
			listingID = productUrl.getClistingid();
			sku = productUrl.getCsku();
		} else {
			return Home.notFoundResult();
		}
		
		Logger.debug("====Product detail listing:   " + listingID
				+ "====sku:  " + sku + "===== " + siteID + "==lang==" + lang);

		ProductContext context = new ProductContext(listingID, sku, siteID,
				lang, currency);
		ProductComposite vo = compositeEnquiry.getProductComposite(context);
		return ok(views.html.product.fragment.detail.render(vo,
				productCompositeRenderer));
	}

/*	public Result newArrial() {
		SearchContext searchContext = factory
				.pureSearch(new ProductTagsCriteria(ProductLabelType.Hot
						.toString()));
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		searchContext.setPageSize(10);
		Page<String> listingsProm = searchService.searchByContext(
				searchContext, webContext);
		List<String> listingids = listingsProm.getList();
		Logger.debug("listingids:{}", listingids);
		List<ProductMessage> productMessages = productMessageService
				.getProductMessages(listingids, webContext);
		Logger.info("---productMessages="+JSONObject.toJSONString(productMessages));
		return ok(views.html.product.newarrivals.render(productMessages));
	}*/
	public Result newArrial() {
			String ca = "NEW-ARRIVALS";
			List<ProductMessage> pinfoList = doUrl(ca);
			Logger.info("---1pinfoList="+JSONObject.toJSONString(pinfoList));
			return ok(views.html.product.newarrivals.render(pinfoList));
	}
		
	private List<ProductMessage> doUrl(String ca){
		int siteID = foundation.getSiteID();
		int languageID = foundation.getLanguageId();
		String ccy = foundation.getCurrency();
		List<ProductMessage> pinfoList = new ArrayList<ProductMessage>();
		int client = 9; // dodocool 
		if(siteID==11){
			client = 14; //ammmoon
		}
		String url = topSellUrl+"?website="+siteID+"&lang="+languageID+"&currency="+ccy+"&client="+client;
		Logger.debug("before send get date- url:"+url);
		String resultBody = new ApiUtil().get(url);
		Logger.debug("end send get date- resultBody:"+resultBody);
		if(StringUtils.isEmpty(resultBody)) return pinfoList;
		String ret =JSON.parseObject(resultBody).getString("ret");
		if("0".equals(ret))return pinfoList;
		JSONObject jsonobject =JSON.parseObject(resultBody).getJSONObject("data");
		if(jsonobject==null) return pinfoList;
		JSONArray jsonArr = jsonobject.getJSONArray(ca);
		if(jsonArr==null ||jsonArr.size()<1)return pinfoList;
		for(int i=0;i<=jsonArr.size()-1;i++){
//			if (jsonArr.size()%2 != 0 && i == jsonArr.size()-1) continue;
			JSONObject jsonO = (JSONObject) jsonArr.get(i);
			ProductMessage pinfo = new ProductMessage();
			String title = jsonO.getString("title");//
			if("TOP-SELLERS".equals(ca)){
				title = title.length()>100?title.substring(0, 100)+"...":title;
			}
			pinfo.setTitle(title);//
			pinfo.setUrl(jsonO.getString("url"));
			pinfo.setImageurl("http://img.tomtop-cdn.com/product/xy/265/265/"+jsonO.getString("imageUrl"));//
			pinfo.setListingid(jsonO.getString("listingId"));//
			pinfo.setSku(jsonO.getString("sku"));//sku
			Map<String, ProductMessage> attribute = Maps.newHashMap();
			attribute.put("test", pinfo);
			pinfo.setAttribute(attribute);
			pinfoList.add(pinfo);
		}
		
		return pinfoList;
	}
		
		
	public Result deals() {
//		WebContext webContext = ContextUtils.getWebContext(Context.current());
//		List<String> listingids = superDeals.getListingIdsByPage(1, 5,
//				webContext);
//		List<ProductMessage> productMessages = productMessageService
//				.getProductMessages(listingids, webContext);
		String ca = "TOP-SELLERS";
		List<ProductMessage> pinfoList = doUrl(ca);
		Logger.info("---2pinfoList="+JSONObject.toJSONString(pinfoList));
		return ok(views.html.product.productdeals.render(pinfoList));
	}
}
