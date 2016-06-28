package services.mobile.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;

import services.ICountryService;
import services.IStorageService;
import services.cart.ICartEnquiryService;
import services.interaction.ICollectService;
import services.loyalty.IPointsService;
import services.loyalty.IPreferService;
import services.loyalty.coupon.ICouponService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.personal.AddressService;
import services.order.ICheckoutService;
import services.order.IFreightService;
import services.product.IProductEnquiryService;
import services.shipping.IShippingMethodService;
import services.shipping.IShippingServices;
import utils.ImageUtils;
import utils.MsgUtils;
import utils.ValidataUtils;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;
import valueobjects.loyalty.LoyaltyCoupon;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import context.WebContext;
import dto.Country;
import dto.Storage;
import dto.interaction.ProductCollect;
import dto.mobile.AddressInfo;
import dto.mobile.CartItemInfo;
import dto.mobile.ShippingMethodInfo;
import dto.product.ProductBase;
import forms.mobile.CartForm;
import forms.mobile.CartItemForm;

public class CartInfoService2 {

	@Inject
	ICountryService countryService;
	@Inject
	ICouponService couponService;
	@Inject
	IPointsService pointsService;
	@Inject
	IPreferService preferService;
	@Inject
	ICheckoutService checkoutService;
	@Inject
	IStorageService storageService;
	@Inject
	IProductEnquiryService productEnquiryService;
	@Inject
	ICartEnquiryService cartEnquiryService;
	@Inject
	ICollectService collectService;
	@Inject
	IShippingServices shippingService;
	@Inject
	IShippingMethodService shippingMethodService;
	@Inject
	IFreightService freightService;
	@Inject
	LoginService loginService;
	@Inject
	AddressService addressService;
	@Inject
	MobileService mobileService;

	/**
	 * 購物車條目信息
	 * 
	 * @param storage
	 * @param cifs
	 * @return
	 */
	public Map<String, Object> viewCart(CartForm cf, Integer address) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<CartItem> cartItems = getCartItem(cf.getItems());
		Logger.error("========cartItems=======" + cartItems);
		if (cartItems != null && cartItems.size() > 0) {
			List<CartItemInfo> cartItemInfos = getCartItemInfos(cartItems);
			result.put("items", cartItemInfos);
			// 用户默认地址
			String email = loginService.getLoginMemberEmail();
			Map<String, Object> ships = getShipMethods(cf.getStorageid(),
					cartItems, address);
			if (ships != null) {
				result.putAll(ships);
			}
			// 可用积分数量
			int points = pointsService.getUsefulPoints(email,
					mobileService.getWebSiteID());
			result.put("point", points);
			List<LoyaltyCoupon> coupons = getMyUsableCoupon(email, cartItems,
					mobileService.getWebContext());
			if (coupons != null) {
				result.put("coupons", coupons);
			}
			return result;
		}
		return null;
	}

	public Map<String, Object> getShipMethods(CartForm cf, Integer address) {
		List<CartItem> cartItems = getCartItem(cf.getItems());
		if (cartItems != null && cartItems.size() > 0) {
			return getShipMethods(cf.getStorageid(), cartItems, address);
		}
		return null;
	}

	private Map<String, Object> getShipMethods(Integer storageid,
			List<CartItem> cartItems, Integer address) {
		// 用户默认地址
		Map<String, Object> info = Maps.newHashMap();
		String email = loginService.getLoginMemberEmail();
		AddressInfo mdinfo = null;
		if (address != null && address > 0) {
			mdinfo = addressService.getAddressInfoById(address);
		}
		if (mdinfo == null) {
			mdinfo = addressService.getDefaultShippingAddress(email);
		}
		if (mdinfo != null) {
			Country country = countryService.getCountryByCountryId(mdinfo
					.getCountry());
			if (country != null) {
				info.put("address", getAdressInfo(mdinfo, country));
				// 托运方式
				List<ShippingMethodInfo> methods = getShippingMethods(
						storageid, cartItems, country);
				if (methods != null && methods.size() > 0) {
					info.put("shiplist", methods);
				} else {
					info.put("shiplist", new ArrayList<>());
				}
				return info;
			}
		}
		return null;

	}

	private Map<String, Object> getAdressInfo(AddressInfo adinfo,
			Country country) {
		Map<String, Object> add = Maps.newHashMap();
		String addressStr = adinfo.getStreet() + " " + adinfo.getCity() + " "
				+ adinfo.getProvince() + " " + country.getCname();
		add.put("aid", ValidataUtils.validataInt(adinfo.getIid()));
		add.put("addr", addressStr);
		add.put("name", adinfo.getFirstname() + " " + adinfo.getLastname());
		add.put("tel", adinfo.getTelephone());
		return add;
	}

	/**
	 * 獲取購物條目詳情
	 * 
	 * @param cifs
	 * @return
	 */
	private List<CartItem> getCartItem(List<CartItemForm> cifs) {
		if (cifs != null && cifs.size() > 0) {
			List<CartItem> cis = Lists.transform(cifs,
					new Function<CartItemForm, CartItem>() {
						@Override
						public CartItem apply(CartItemForm cif) {
							CartItem item = new SingleCartItem();
							item.setClistingid(cif.getGid());
							item.setIqty(cif.getQty());
							return item;
						}
					});
			return cartEnquiryService.getCartItems(cis,
					mobileService.getWebSiteID(),
					mobileService.getLanguageID(), mobileService.getCurrency());
		}
		return null;
	}

	/**
	 * 轉換購物條目信息
	 * 
	 * @param cartItems
	 * @return
	 */
	public List<CartItemInfo> getCartItemInfos(List<CartItem> cartItems) {
		if (cartItems != null && cartItems.size() > 0) {
			List<CartItemInfo> resultList = Lists.transform(cartItems,
					new Function<CartItem, CartItemInfo>() {
						@Override
						public CartItemInfo apply(CartItem cartItem) {
							CartItemInfo itemInfo = new CartItemInfo();
							itemInfo.setExtra(ValidataUtils
									.validataStr(cartItem.getAddition()));
							itemInfo.setIid("");
							itemInfo.setImgurl(ValidataUtils
									.validataStr(ImageUtils.getWebPath(
											cartItem.getCimageurl(), 265, 265,
											mobileService.getMobileContext())));
							itemInfo.setGid(ValidataUtils.validataStr(cartItem
									.getClistingid()));
							itemInfo.setTitle(ValidataUtils
									.validataStr(cartItem.getCtitle()));
							itemInfo.setQty(cartItem.getIqty());
							itemInfo.setSale(cartItem.getPrice() == null ? 0
									: cartItem.getPrice().getUnitPrice());
							itemInfo.setPcs(cartItem.getPrice() == null ? 0
									: cartItem.getPrice().getUnitBasePrice());
							itemInfo.setSku(cartItem.getSku());
							ProductBase product = productEnquiryService
									.getProductByListingIdAndLanguageWithdoutDesc(
											cartItem.getClistingid(),
											mobileService.getLanguageID());
							itemInfo.setStock(product.getIqty());
							// 判断该商品是否收藏
							if (loginService.isLogin()) {
								List<ProductCollect> collects = collectService
										.getCollectByMember(cartItem
												.getClistingid(), loginService
												.getLoginMemberEmail());
								if (collects != null && collects.size() > 0) {
									itemInfo.setWish(1);
								}
							}
							Map<String, String> attr = cartItem
									.getAttributeMap();
							String attrs = "";
							if (attr != null && !attr.isEmpty()) {
								Iterator<Entry<String, String>> iterator = attr
										.entrySet().iterator();
								while (iterator.hasNext()) {
									Entry<String, String> next = iterator
											.next();
									attrs = attrs + next.getKey() + ":"
											+ next.getValue() + " ";
								}
							}
							itemInfo.setAttrs(attrs);
							return itemInfo;
						}
					});
			return resultList;
		}
		return null;
	}

	/**
	 * 应用优惠券
	 * 
	 * @param cartItems
	 * @param email
	 * @param code
	 * @return
	 */
	public LoyaltyPrefer applyCoupon(List<CartItem> cartItems, String email,
			String code) {
		// 远程调用应用优惠券
		WebContext webContext = mobileService.getWebContext();
		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = preferService.getAllPreferByEmail(
				email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}
		LoyaltyPrefer loyaltyPrefer = couponService.applyCoupon(email,
				cartItems, code, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				return new LoyaltyPrefer();
			}
		}
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) > totalPrice) {
				return new LoyaltyPrefer();
			}
		}
		return loyaltyPrefer;
	}

	public boolean isSameStorage(List<String> gids, int storage) {
		return shippingService.isSameStorage(gids, "" + storage);
	}

	/**
	 * 应用推广码
	 * 
	 * @param cartItems
	 * @param email
	 * @param code
	 * @return
	 */
	public LoyaltyPrefer applyPromo(List<CartItem> cartItems, String email,
			String code) {
		WebContext webContext = mobileService.getWebContext();
		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = preferService.getAllPreferByEmail(
				email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}
		LoyaltyPrefer loyaltyPrefer = couponService.applyPromo(email,
				cartItems, code, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				return new LoyaltyPrefer();
			}
		}
		return loyaltyPrefer;
	}

	/**
	 * 获取当前的 抵扣
	 * 
	 * @param cartItems
	 * @param email
	 * @return
	 */
	public LoyaltyPrefer getCurrentPrefer(List<CartItem> cartItems, String email) {
		WebContext webContext = mobileService.getWebContext();
		LoyaltyPrefer loyaltyPrefer = couponService.getCurrentPrefer(email,
				cartItems, webContext);
		return loyaltyPrefer;
	}

	/**
	 * 使用积分
	 * 
	 * @param email
	 * @param costpoints
	 * @return
	 */
	public LoyaltyPrefer applyPoints(List<CartItem> cartItems, String email,
			Integer costpoints) {
		WebContext webContext = mobileService.getWebContext();
		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = preferService.getAllPreferByEmail(
				email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}
		LoyaltyPrefer loyaltyPrefer = pointsService.applyPoints(email,
				cartItems, costpoints, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				return new LoyaltyPrefer();
			}
		}
		return loyaltyPrefer;
	}

	/**
	 * 获取当前所有的抵扣
	 * 
	 * @return
	 */
	public List<LoyaltyPrefer> getAllCurrentPrefer(List<CartItem> cartItems) {
		String email = loginService.getLoginMemberEmail();
		if (email == null || email.length() == 0) {
			return null;
		}
		WebContext webContext = mobileService.getWebContext();
		List<LoyaltyPrefer> loyaltyPrefers = preferService.getAllPreferByEmail(
				email, cartItems, webContext);
		if (null != loyaltyPrefers && loyaltyPrefers.size() > 0) {
			return loyaltyPrefers;
		}
		return null;
	}

	/**
	 * 获取未使用的优惠券
	 * 
	 * @param email
	 * @param cartItems
	 * @param webCtx
	 * @return
	 */
	public List<LoyaltyCoupon> getMyUsableCoupon(String email,
			List<CartItem> cartItems, WebContext webCtx) {
		return couponService.getMyUsableCoupon(email, cartItems, webCtx);
	}

	/**
	 * 生产订单保存 抵扣使用
	 * 
	 * @param lps
	 * @param email
	 * @return
	 */
	public boolean saveAllLoyaltyPrefer(List<LoyaltyPrefer> lps, String email) {
		return preferService.saveAllPrefer(email, lps,
				mobileService.getWebContext());
	}

	/**
	 * 获取邮寄方式信息
	 * 
	 * @param storage
	 * @param cartItems
	 * @param country
	 * @return
	 */
/*	private List<ShippingMethodInfo> getShippingMethods(Integer storageId,
			List<CartItem> cartItems, Country country) {
		if (cartItems == null || cartItems.size() == 0) {
			return null;
		}
		if (country == null) {
			return null;
		}
		List<String> listingId = Lists.newLinkedList();
		FluentIterable
				.from(cartItems)
				.forEach(
						item -> {
							if (item instanceof valueobjects.cart.SingleCartItem) {
								listingId.add(item.getClistingid());
							} else if (item instanceof valueobjects.cart.BundleCartItem) {
								List<valueobjects.cart.SingleCartItem> childs = ((valueobjects.cart.BundleCartItem) item)
										.getChildList();
								List<String> clisting = Lists.transform(childs,
										c -> c.getClistingid());
								listingId.addAll(clisting);
							}
						});
		Storage storage;
		if (storageId == null) {
			storage = shippingService.getShippingStorage(
					mobileService.getWebSiteID(), country, listingId);
		} else {
			storage = storageService.getStorageForStorageId(storageId);
		}
		Integer storageid = storage.getIid();
		Integer lang = mobileService.getLanguageID();
		String countryName = country.getCshortname();
		Double subtotal = checkoutService.subToatl(cartItems);
		Double weight = freightService.getTotalWeightV2(cartItems);
		Double shippingWeight = freightService.getTotalShipWeightV2(cartItems);
		Boolean isSpecial = shippingMethodService.isSpecial(listingId);
		String currency = mobileService.getCurrency();
		Integer site = mobileService.getWebSiteID();
		ShippingMethodRequst requst = new ShippingMethodRequst(storageid,
				countryName, weight, shippingWeight, lang, subtotal, listingId,
				isSpecial, currency, site, false);
		ShippingMethodInformations infos = shippingMethodService
				.getShippingMethodInformations(requst);
		List<ShippingMethodInfo> methodinfos = Lists.transform(infos.getList(),
				new Function<ShippingMethodInformation, ShippingMethodInfo>() {
					@Override
					public ShippingMethodInfo apply(
							ShippingMethodInformation method) {
						ShippingMethodInfo methodInfo = new ShippingMethodInfo();
						methodInfo.setSid(method.getId());
						methodInfo.setPcs(ValidataUtils.validataDouble(method
								.getFreight()));
						methodInfo.setTitle(ValidataUtils.validataStr(method
								.getContext()));
						return methodInfo;
					}
				});
		return methodinfos;
	}*/
	
	/**
	 * @function 获取有地方式，使用新接口4.16
	 * @param storageId
	 * @param cartItems
	 * @param country
	 * @return
	 * @author lyf
	 */
	private List<ShippingMethodInfo> getShippingMethods(Integer storageId,
			List<CartItem> cartItems, Country country) {
		if (cartItems == null || cartItems.size() == 0) {
			return null;
		}
		if (country == null) {
			return null;
		}
		List<String> listingId = Lists.newLinkedList();
		FluentIterable
		.from(cartItems)
		.forEach(
				item -> {
					if (item instanceof valueobjects.cart.SingleCartItem) {
						listingId.add(item.getClistingid());
					} else if (item instanceof valueobjects.cart.BundleCartItem) {
						List<valueobjects.cart.SingleCartItem> childs = ((valueobjects.cart.BundleCartItem) item)
								.getChildList();
						List<String> clisting = Lists.transform(childs,
								c -> c.getClistingid());
						listingId.addAll(clisting);
					}
				});
		Storage storage;
		if (storageId == null) {
			storage = shippingService.getShippingStorage(
					mobileService.getWebSiteID(), country, listingId);
		} else {
			storage = storageService.getStorageForStorageId(storageId);
		}
		Integer storageid = storage.getIid();
		Integer lang = mobileService.getLanguageID();
		String countryName = country.getCshortname();
		Double subtotal = checkoutService.subToatl(cartItems);//商品总价
		Double weight = freightService.getTotalWeightV2(cartItems);
		Double shippingWeight = freightService.getTotalShipWeightV2(cartItems);
		Boolean isSpecial = shippingMethodService.isSpecial(listingId);
		String currency = mobileService.getCurrency();
		Integer site = mobileService.getWebSiteID();
		
		String url = "http://logistics.api.tomtop.com/shipping";
		//封装data数据
		String data = getPostData(country,currency,lang,storage,subtotal,cartItems);
		//post请求
		Logger.info("before send post data="+data);
		String resultBody = post(url, data);
		Logger.info("end send post resultBody="+resultBody);
		if(StringUtils.isBlank(resultBody))return null;
		JSONObject jsonobject = JSONObject.parseObject(resultBody);
		if(jsonobject==null ||jsonobject.getJSONArray("data")==null)return null;
		JSONArray  dataArr= jsonobject.getJSONArray("data");
		if(dataArr==null ||dataArr.size()<1) return null;
		List<ShippingMethodInfo> methodinfos = new ArrayList<ShippingMethodInfo>();
		for(int i=0;i<=dataArr.size()-1;i++){
			JSONObject dataj= (JSONObject)dataArr.get(i);
			if(dataj == null) continue;
			
			if("false".equals(dataj.getString("isShow")))continue;//将不可用的物流提出
			ShippingMethodInfo methodInfo = new ShippingMethodInfo();
			methodInfo.setPcs(dataj.getDouble("price")==null?0:dataj.getDouble("price"));
			String shipid = dataj.getString("id");
			methodInfo.setSid(dataj.getInteger("id")==null?0:dataj.getInteger("id"));
			String title = StringUtils.isBlank(dataj.getString("title"))?"":dataj.getString("title");
			String code = StringUtils.isBlank(dataj.getString("code"))?"":dataj.getString("code");
			String description = StringUtils.isBlank(dataj.getString("description"))?"":dataj.getString("description");
			Logger.info("-----键值对：shipid="+shipid+",code="+code);
			MsgUtils.put(shipid, code);//shipid 和 code 键值对存入
			methodInfo.setTitle(title+"("+description+")");
			methodinfos.add(methodInfo);
		}
		return methodinfos;
	}
	
	/**
	 * post请求数据封装
	 * @param country
	 * @param currency
	 * @param lang
	 * @param storage
	 * @param subtotal
	 * @param cartItems
	 */
	private String getPostData(Country country, String currency, Integer lang,
			Storage storage, Double subtotal, List<CartItem> cartItems) {
		JSONObject json = new JSONObject();
		json.put("country", country.getCshortname());
		json.put("currency", currency);
		json.put("language", lang);
		json.put("storageId", storage.getIid());
		json.put("totalPrice", subtotal);
		JSONArray ayy = new JSONArray();
		for(CartItem item : cartItems){
			JSONObject jsonch = new JSONObject();
			jsonch.put("listingId", item.getClistingid());
			jsonch.put("qty", item.getIqty());
			String [] c =null;
			if (item instanceof valueobjects.cart.BundleCartItem) {
				List<SingleCartItem> items= ((valueobjects.cart.BundleCartItem) item).getChildList();
				c = new String[items.size()];
				for(int i=0;i<=items.size()-1;i++){
					c[i] = items.get(i).getClistingid();
				}
			}else{
				c = new String[0];
			}
			jsonch.put("chrd", c);
			ayy.add(jsonch);
		}
		json.put("shippingCalculateLessParamBase", ayy);
		return json.toString();
	}

	/**
	 * @function post请求
	 * @param url
	 * @param objjson
	 * @return
	 */
	private String post(String url,String objjson) {
		String newUrlToken = Play.application().configuration().getString("newUrlToken");
		Logger.info("---------newUrlToken="+newUrlToken);
		String token =StringUtils.isBlank(newUrlToken)?"test":newUrlToken;
/*        String json = "{\"username\":\"zhangsan\",\"性别\":\"男\",\"company\":{\"companyName\":\"中华\",\"address\":\"北京\"},\"cars\":[\"奔驰\",\"宝马\"]}";  
        ObjectMapper mapper = new ObjectMapper();  
        //JSON ----> JsonNode  
        JsonNode rootNode = mapper.readTree(json);    */
		WSRequestHolder wsRequest = WS.url(url).setHeader("Content-Type",
				"application/json").setHeader("token", token);
		Promise<String> resultStr = wsRequest.post(objjson).map(response -> {
			return response.getBody();
		});
		return resultStr.get(100000);
	}
	

}
