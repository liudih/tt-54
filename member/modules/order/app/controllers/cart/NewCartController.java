package controllers.cart;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.order.MemberCartService;
import utils.order.HttpClientUtil;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class NewCartController extends Controller{

	@Inject
	FoundationService foundationService;
	
	@Inject
	MemberCartService memberCartService;
	
	
	/**
	 * 购物车下拉列表数据
	 */
	public Result getCartsJson() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		
		int lang = foundationService.getLanguage();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		List<CartItem> clist = memberCartService.getAllItems(siteid, lang, currency);
		
		StringBuilder url = new StringBuilder("http://product.api.tomtop.com/ic/v2/product/shoppingCart");
		url.append("?client=").append(foundationService.getSiteID());
		url.append("&website=").append(foundationService.getSiteID());
		url.append("&lang=").append(foundationService.getLanguage());
		url.append("&currency=").append(foundationService.getCurrency());
		List<Map<String,Object>> mlist = Lists.newArrayList();
		for(CartItem ci : clist){
			Map<String,Object> mm = new HashMap<String, Object>();
			mm.put("listingId", ci.getClistingid());
			mm.put("num", ci.getIqty());
			mm.put("depotId", ci.getStorageID());
			mlist.add(mm);
		}
		String ddata = "";
		try {
			ddata = URLEncoder.encode(JSON.toJSONString(mlist),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		url.append("&listings=").append(ddata);
		String result = HttpClientUtil.doGet(url.toString());
		if(result != null ){
			JSONObject jb = JSON.parseObject(result);
			Integer ret = jb.getInteger("ret");
			if(ret!=null && ret==1){
				mjson.put("result", "success");
				mjson.put("data", jb.get("data"));
			}else{
				mjson.put("result", jb.getString("errMsg"));
				return ok(Json.toJson(mjson));
			}
		}
		Logger.debug("jjjj={}",JSON.toJSONString(clist));
		
		mjson.put("size", clist.size());
//			mjson.put("html", views.html.cart.cartdrop_list_new_2.render(list)
//					.toString());
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}
	
	
	/**
	 * 删除购物车商品
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result delCartItem(){
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();
		if(jnode!=null){
			CartItem ci = packCartItem(jnode);
			List<CartItem> clist = Lists.newArrayList();
			clist.add(ci);
			memberCartService.deleteItem(clist);
			mjson.put("result", "success");
		}else{
			mjson.put("result", "data is null");
		}
		return ok(Json.toJson(mjson));
	}
	
	
	/**
	 * json组装CartItem对象
	 */
	protected CartItem packCartItem(JsonNode jnode){
		CartItem cartItem = new CartItem();
		if (jnode.size()==1) {
			cartItem = new SingleCartItem();
			JsonNode jsonNode = jnode.get(0);
			Integer storageID = null;
			if (jsonNode.get("storageid") != null) {
				storageID = jsonNode.get("storageid").asInt();
				cartItem.setStorageID(storageID);
			}
			if(jsonNode.get("qty")!=null){
				Integer qty = jsonNode.get("qty").asInt();
				cartItem.setIqty(qty);
			}
			String clistingid = jsonNode.get("clistingid").asText();
			cartItem.setClistingid(clistingid);
		}else if(jnode.size()>1){
			cartItem = new BundleCartItem();
			List<SingleCartItem> childList = new ArrayList<SingleCartItem>();
			Iterator<JsonNode> iterator = jnode.iterator();
			while (iterator.hasNext()) {
				JsonNode next = iterator.next();
				SingleCartItem singleCartItem = new SingleCartItem();
				if (next.get("storageid") != null) {
					Integer storageID = next.get("storageid").asInt();
					singleCartItem.setStorageID(storageID);
				}
				if(next.get("qty")!=null){
					Integer qty = next.get("qty").asInt();
					singleCartItem.setIqty(qty);
				}
				String clistingid = next.get("clistingid").asText();
				singleCartItem.setClistingid(clistingid);
				childList.add(singleCartItem);
			}
			((BundleCartItem) cartItem).setChildList(childList);
			if(childList.size()>0){
				cartItem.setStorageID(childList.get(0).getStorageID());
			}
		}
		return cartItem;
	}
	
	
}
