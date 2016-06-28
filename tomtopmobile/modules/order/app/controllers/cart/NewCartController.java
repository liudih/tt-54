package controllers.cart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import play.Logger;
import play.Play;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import plugins.mobile.order.OrderFragmentPluginHelper;
import services.ICurrencyService;
import services.IStorageParentService;
import services.base.FoundationService;
import services.base.utils.CookieUtils;
import services.base.utils.MetaUtils;
import services.cart.ICartBuilderService;
import services.cart.ICartEnquiryService;
import services.cart.ICartLifecycleService;
import services.cart.ICartServices;
import services.loyalty.coupon.ICouponService;
import services.product.inventory.IInventoryEnquiryService;
import valueobjects.base.LoginContext;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartGetRequest;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;

import dto.Currency;
import dto.StorageParent;
import extensions.InjectorInstance;
import extensions.order.collect.ICollectProvider;
import facade.cart.Cart;

public class NewCartController extends Controller{

	@Inject
	ICartBuilderService cartBuilderService;
	@Inject
	FoundationService foundationService;
	@Inject
	ICartLifecycleService cartLifecycle;
	@Inject
	IInventoryEnquiryService inventoryEnquiryService;
	@Inject
	ICartEnquiryService cartEnquiryService;
	@Inject
	ICurrencyService currencyService;
	@Inject
	IStorageParentService storageParentService;
	@Inject
	Set<ICollectProvider> ICollectProviders;
	@Inject
	OrderFragmentPluginHelper ofpHelper;
	
	@Inject
	ICartServices icartServices;
	
	/**
	 * 购物车页面
	 */
	public Result cartview(String storageid) {

		MetaUtils
				.currentMetaBuilder()
				.setTitle("Shopping Cart")
				.setDescription(
						"No Regular No Business. TOMTOP offers hot gadgets at the best price online. Enjoy fast shipping & excellent service.")
				.addKeyword("TOMTOP, hot gadgets, cheap rc models");
		Cart cart = getCart();
		List<CartItem> cartItemlist = cart.getAllItems();
		// 删除状态不为1和库存不足的商品
		for (CartItem cit : cartItemlist) {
			// 判断库存
			boolean isEnough = cartEnquiryService.isEnoughQty(cit);
			if (!isEnough) {
				cit.setIstatus(0);
				cart.deleteItem(Lists.newArrayList(cit));
				continue;
			}
			if (cit instanceof SingleCartItem) {
				if (cit.getIstatus() != null && cit.getIstatus() != 1) {
					cart.deleteItem(Lists.newArrayList(cit));
				}
			} else if (cit instanceof BundleCartItem) {
				boolean isnotStatus = false;
				List<SingleCartItem> blist = ((BundleCartItem) cit)
						.getChildList();
				for (int i = 0; i < blist.size(); i++) {
					if (blist.get(i).getIstatus() != null
							&& blist.get(i).getIstatus() != 1) {
						isnotStatus = true;
						break;
					}
				}
				if (isnotStatus) {
					cit.setIstatus(0);
					cart.deleteItem(Lists.newArrayList(cit));
				}
			}
		}
		cartItemlist = Lists.newArrayList(Collections2.filter(
				cartItemlist,
				c -> c.getStorageID() != null
						&& (c.getIstatus() == null || c.getIstatus() == 1)));

		Multimap<Integer, CartItem> cartItemListIndex = Multimaps.index(
				cartItemlist, c -> c.getStorageID());
		TreeMap<Integer, Collection<CartItem>> cl = new TreeMap<Integer, Collection<CartItem>>();
		cl.putAll(cartItemListIndex.asMap());
		//为了按key顺序排序
		List<Integer> cartStorageids = Lists.newArrayList(cl.keySet());

		String c = foundationService.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(c);
		List<StorageParent> storageList = storageParentService
				.getAllStorageParentList();
		Map<Integer, StorageParent> storageMap = Maps.uniqueIndex(storageList,
				s -> s.getIid());
		// 获取临时收藏
		List<CartItem> laterItemlist = cart.getAllLaterItems();
		// add by lijun
		LoginContext loginCtx = foundationService.getLoginContext();
		Boolean isLogin = loginCtx.isLogin();
		
		List<ICollectProvider> collectList = Lists
				.newArrayList(ICollectProviders.iterator());
		
		return ok(views.html.mobile.cart.cart_view_2.render(cl, currency,
				storageMap, laterItemlist, 
				isLogin, storageid, cartStorageids, collectList));
	}
	
	/**
	 * 生成订单前,用户填写地址信息
	 * 
	 * @author lijun
	 * @return
	 */
	public Result fillAddressBeforeOrder() {
		int site = foundationService.getSiteID();
		int lang = foundationService.getLanguage();
		String currency = foundationService.getCurrency();
		LoginContext loginCtx = foundationService.getLoginContext();
		// 没登录跳转到登录
		if (!loginCtx.isLogin()) {
			return redirect(controllers.member.routes.Login.login(0));
		}
		String email = loginCtx.getMemberID();
		List<CartItem> items = icartServices.getAllItemsCurrentStorageid(site, lang, currency);
		String cartUrl = Play.application().configuration().getString("cart.url");
		if (items == null || items.size() == 0) {
			return redirect(cartUrl);
		}
		// 判断库存
		for (CartItem cit : items) {
			// 判断库存
			boolean isEnough = cartEnquiryService.isEnoughQty(cit);
			if (!isEnough) {
				return badRequest("No inventory");
			}
		}
		
		// 将优惠信息写入老cart对象
//		Double allPrefer = this.getAllPrefer(cart.getId());
//		cart.setAllPrefer(allPrefer);
		Currency curr = foundationService.getCurrencyObj();
		return ok(views.html.mobile.cart.address.render(ofpHelper,
				curr,items));
	}
	
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
	
	@BodyParser.Of(BodyParser.Json.class)
	public Result updateCartItem() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();
		CartItem cartItem = packCartItem(jnode);
		// 判断库存
		boolean isEnough = cartEnquiryService.isEnoughQty(cartItem);
		if (!isEnough) {
			mjson.put("result", "no-enough");
			return ok(Json.toJson(mjson));
		}
		Cart cart = getCart();
		cart.updateItemQty(cartItem);
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
	
	/**
	 * 购物车页面
	 */
	public Result setStorageid(String storageid) {
		Context ctx = Context.current();
		Map<String, Object> mjson = new HashMap<String, Object>();
		String exstorageid = CookieUtils.getCookie("storageid", ctx);
		if(exstorageid!=null && storageid!=null && !exstorageid.equals(storageid)){
			if (ctx.request().cookie(ICouponService.LOYALTY_PREFER) != null) {
				CookieUtils.removeCookie(ICouponService.LOYALTY_PREFER, ctx);
			}
			if (ctx.request().cookie(ICouponService.LOYALTY_TYPE_POINT) != null) {
				CookieUtils.removeCookie(ICouponService.LOYALTY_TYPE_POINT, ctx);
			}
		}
		CookieUtils.setCookie("storageid", storageid, ctx);
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}
	
	public Result showCartSize() {
		Cart cart = getCart();
		List<CartItem> cartItemlist = cart.getAllItems();
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("size", cartItemlist.size());
		return ok(Json.toJson(mjson));
	}
	
}
