package controllers.cart;

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
import services.cart.ICartBuilderService;
import services.cart.ICartLifecycleService;
import services.product.inventory.InventoryEnquiryService;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartGetRequest;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import extensions.InjectorInstance;
import facade.cart.Cart;

public class NewCartController extends Controller{

	@Inject
	ICartBuilderService cartBuilderService;
	@Inject
	FoundationService foundationService;
	@Inject
	ICartLifecycleService cartLifecycle;
	@Inject
	InventoryEnquiryService inventoryEnquiryService;
	
	/**	
	 * 添加商品到购物车
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result saveCartItem() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();
		CartItem cartItem = packCartItem(jnode);
		Cart cart = getCart();
		CartItem ocartItem = cart.getCartItem(cartItem);
		if(ocartItem!=null){
			if (ocartItem instanceof SingleCartItem) {
				if(cartItem.getIqty()!=null && ocartItem.getIqty()!=null){
					cartItem.setIqty(cartItem.getIqty()+ocartItem.getIqty());
				}
			} else if (ocartItem instanceof BundleCartItem) {
				List<SingleCartItem> blist = ((BundleCartItem) cartItem).getChildList();
				List<SingleCartItem> oblist = ((BundleCartItem) ocartItem).getChildList();
				for(int i=0;i<blist.size();i++){
					if(blist.size()==oblist.size() && blist.get(i).getIqty()!=null && oblist.get(i).getIqty()!=null){
						blist.get(i).setIqty(blist.get(i).getIqty()+oblist.get(i).getIqty());
					}
				}
			}
		}
		//判断库存
		boolean isEnough = isEnoughQty(cartItem);
		if(!isEnough){
			mjson.put("result", "no-enough");
			return ok(Json.toJson(mjson));
		}
		cart.addCartItem(cartItem);
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}
	
	/**
	 * 购物车下拉列表数据
	 */
	public Result getCartsJson() {
		moveNewCart();
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		Cart cart = getCart();
		List<CartItem> list = cart.getAllItems();
		if (list != null && list.size() > 0) {
			//都转换成单品，捆绑显示主产品信息
			list = Lists.transform(list, l -> {
				if(l instanceof BundleCartItem){
					Logger.debug("BundleCartItem has childList?");
					List<SingleCartItem> slist = ((BundleCartItem) l).getChildList();
					String id = "";
					for(SingleCartItem sc : slist){
						id += sc.getClistingid()+",";
					}
					slist.get(0).setId(id);
					return slist.get(0);
				}else{
					l.setId(l.getClistingid());
					return l;
				}
			});
			mjson.put("size", list.size());
			mjson.put("html", views.html.cart.cartdrop_list_new_2.render(list)
					.toString());
			mjson.put("result", "success");
		}
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
			Cart cart = getCart();
			cart.deleteItem(clist);
			mjson.put("result", "success");
		}else{
			mjson.put("result", "data is null");
		}
		return ok(Json.toJson(mjson));
	}
	
	protected Cart getCart(){
		int lang = foundationService.getLanguage();
		int site = foundationService.getSiteID();
		String ltc = foundationService.getLoginContext().getLTC();
		String cur = foundationService.getCurrency();
		String email = null;
		if(foundationService.getLoginContext().isLogin()){
			email = foundationService.getLoginContext().getMemberID();
		}
		CartGetRequest req = new CartGetRequest(email, ltc, site, lang, cur, "cookie");
		facade.cart.Cart cart = cartBuilderService.createCart(req);
		InjectorInstance.getInjector().injectMembers(cart);
		return cart;
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
	
	
	//旧的cart数据放进新的购物车
	public void moveNewCart(){
		facades.cart.Cart cart0 = getCurrentCart(true);
		List<valueobjects.order_api.cart.CartItem> list0 = cart0.getAllItems();
		if(list0.size()>0){
			Cart cart = getCart();
			for(valueobjects.order_api.cart.CartItem cio : list0){
				CartItem cc =transNewCartItem(cio);
				if(cc!=null){
					cart.addCartItem(cc);
				}
			}
			cart0.clear();
		}
	}
	
	protected facades.cart.Cart getCurrentCart(boolean createIfNotExist) {
		String email = null;
		if (foundationService.getLoginContext().isLogin()) {
			email = foundationService.getLoginContext().getMemberID();
		}
		// Logger.debug("email:::::{}",email);
		valueobjects.order_api.cart.CartGetRequest req = new valueobjects.order_api.cart.CartGetRequest(email, foundationService
				.getLoginContext().getLTC(), foundationService.getSiteID(), 
				foundationService.getLanguage(), foundationService.getCurrency());
		if (createIfNotExist) {
			return cartLifecycle.getOrCreateCart(req);
		} else {
			return cartLifecycle.getCart(req);
		}
	}
	
	protected CartItem transNewCartItem(valueobjects.order_api.cart.CartItem cio){
		if(cio!=null && cio instanceof valueobjects.order_api.cart.SingleCartItem){
			CartItem ci = new CartItem();
			ci.setAttributeMap(cio.getAttributeMap());
			ci.setCimageurl(cio.getCimageurl());
			ci.setClistingid(cio.getClistingid());
			ci.setCtitle(cio.getCtitle());
			ci.setCurl(cio.getCurl());
			ci.setId(cio.getClistingid());
			ci.setIqty(cio.getIqty());
			ci.setSku(cio.getSku());
			ci.setStorageID(cio.getStorageID());
			if(cio.getPrice()!=null){
				valueobjects.cart.Price cp = new valueobjects.cart.Price();
				cp.setCurrency(cio.getPrice().getCurrency());
				cp.setDiscount(cio.getPrice().getDiscount());
				cp.setDiscounted(cio.getPrice().isDiscounted());
				cp.setPrice(cio.getPrice().getPrice());
				cp.setSymbol(cio.getPrice().getSymbol());
				cp.setUnitBasePrice(cio.getPrice().getUnitBasePrice());
				cp.setUnitPrice(cio.getPrice().getUnitPrice());
				cp.setValidFrom(cio.getPrice().getValidFrom());
				cp.setValidTo(cio.getPrice().getValidTo());
				ci.setPrice(cp);
			}
			return ci;
		}else{
			return null;
		}
	}
	
	private boolean isEnoughQty(CartItem cartItem) {
		if (cartItem instanceof SingleCartItem) {
			if (inventoryEnquiryService.checkInventory(cartItem.getClistingid(), cartItem.getIqty()) && cartItem.getIqty()<=999) {
				return true;
			}
		} else if (cartItem instanceof BundleCartItem) {
			List<SingleCartItem> blist = ((BundleCartItem) cartItem).getChildList();
			for(int i=0;i<blist.size();i++){
				if (!inventoryEnquiryService.checkInventory(blist.get(i).getClistingid(), blist.get(i).getIqty()) && blist.get(i).getIqty()>999) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
