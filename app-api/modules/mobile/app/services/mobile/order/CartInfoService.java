package services.mobile.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import services.ICountryService;
import services.ICurrencyService;
import services.cart.ICartLifecycleService;
import services.cart.ICartService;
import services.interaction.ICollectService;
import services.loyalty.IPointsService;
import services.loyalty.coupon.ICouponService;
import services.member.login.ILoginService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.personal.AddressService;
import services.order.IFreightService;
import services.product.IProductEnquiryService;
import services.shipping.IShippingMethodService;
import services.shipping.IShippingServices;
import utils.BaseUtils;
import utils.ImageUtils;
import utils.ValidataUtils;
import valueobjects.loyalty.Coupon;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.cart.CartCreateRequest;
import valueobjects.order_api.cart.CartGetRequest;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.SingleCartItem;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import valueobjects.price.Price;
import valuesobject.mobile.member.MobileContext;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import context.WebContext;
import dto.Country;
import dto.Storage;
import dto.interaction.ProductCollect;
import dto.mobile.AddressInfo;
import dto.mobile.CartItemInfo;
import dto.mobile.ShippingMethodInfo;
import dto.product.ProductBase;
import events.member.LoginEvent;
import extensions.InjectorInstance;
import facades.cart.Cart;

public class CartInfoService {

	@Inject
	ICartService cartService;

	@Inject
	ICountryService countryService;

	@Inject
	IShippingServices shippingService;

	@Inject
	IFreightService freightService;

	@Inject
	IPointsService pointsService;

	@Inject
	ICouponService couponService;

	@Inject
	ICurrencyService currencyService;

	@Inject
	IShippingMethodService shippingMethodService;

	@Inject
	ICollectService collectService;

	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	IProductEnquiryService productEnquiryService;

	@Inject
	ILoginService webloginService;

	@Inject
	LoginService loginService;

	@Inject
	MobileService mobileService;

	@Inject
	AddressService addressService;

	public Map<String, Object> viewCart(String uuid) {
		Map<String, Object> result = new HashMap<String, Object>();
		Cart cart = getCurrentCart(uuid, true);
		// 购物车目录
		List<CartItemInfo> items = getCartItemInfo(cart);
		if (items != null && items.size() > 0) {
			result.put("cartid", cart.getId());
			result.put("list", items);
			List<ExtraLineView> extras = cart.convertExtras();
			if (extras != null && extras.size() > 0) {
				result.put("extras", extras);
			} else {
				result.put("extras", new ArrayList<ExtraLineView>());
			}
			// 用户默认地址
			String email = loginService.getLoginMemberEmail();
			AddressInfo defauladdress = addressService
					.getDefaultShippingAddress(email);
			if (defauladdress == null) {
				result.put("aid", 0);
				result.put("addr", "");
				result.put("tel", "");
				result.put("name", "");
			} else {
				Country country = countryService
						.getCountryByCountryId(defauladdress.getCountry());
				if (country != null) {
					String address = defauladdress.getStreet() + " "
							+ defauladdress.getCity() + " "
							+ defauladdress.getProvince() + " "
							+ country.getCname();
					result.put("aid",
							ValidataUtils.validataInt(defauladdress.getIid()));
					result.put("addr", address);
					result.put("name", defauladdress.getFirstname() + " "
							+ defauladdress.getLastname());
					result.put("tel", defauladdress.getTelephone());
					// 托运方式
					List<ShippingMethodInfo> methods = getShippingMethods(cart,
							country);
					if (methods != null && methods.size() > 0) {
						result.put("shiplist", methods);
					} else {
						result.put("shiplist", new ArrayList<>());
					}
				}
			}
			// 可用积分数量
			int points = pointsService.getUsefulPoints(email,
					mobileService.getWebSiteID());
			result.put("point", points);
		}
		return result;
	}

	/**
	 * 根据地址ID获取送货方式列表
	 * 
	 * @param uuid
	 * @param aid
	 * @return
	 */
	public Map<String, Object> getShippingMethods(String uuid, int aid) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> defauladdress = addressService
				.getMemberAddressMapById(aid);
		if (defauladdress != null) {
			Integer icountry = (Integer) defauladdress.get("country");
			if (icountry != null) {
				Country country = countryService
						.getCountryByCountryId(icountry);
				Cart cart = getCurrentCart(uuid, true);
				// 托运方式
				List<ShippingMethodInfo> methods = getShippingMethods(cart,
						country);
				if (methods != null && methods.size() > 0) {
					result.put("shiplist", methods);
				} else {
					result.put("shiplist", new ArrayList<>());
				}
			}
		}
		return result;
	}

	/**
	 * 获取购物车商品
	 * 
	 * @param uuid
	 * @return
	 */
	public Map<String, Object> viewCartItem(String uuid) {
		Map<String, Object> result = new HashMap<String, Object>();
		Cart cart = getCurrentCart(uuid, true);
		// 购物车目录
		List<CartItemInfo> items = getCartItemInfo(cart);
		if (items != null && items.size() > 0) {
			result.put("cartid", cart.getId());
			result.put("list", items);
		}
		return result;

	}

	/**
	 * 获取匹配购物车的优惠券
	 * 
	 * @param cart
	 * @param email
	 * @return
	 */
	public List<Map<String, Object>> getMemberCoupon(String uuid, String email) {
		Cart cart = getCurrentCart(uuid, true);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		WebContext context = mobileService.getWebContext();
		List<Coupon> coupons = couponService.getMyUsableCoupon(email,
				cart.getId(), context);
		coupons.forEach(c -> {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", c.getCode());
			if (c.isCash()) {
				map.put("value", c.getAmount() + " "
						+ c.getCurrency().getCsymbol());
			} else {
				map.put("value", BaseUtils.money(c.getPercent()) + "% OFF");
			}

			result.add(map);
		});
		return result;
	}

	/**
	 * 获取 匹配购物车的运输方式
	 * 
	 * @param cart
	 * @param defauladdress
	 * @return
	 */
	public List<ShippingMethodInfo> getShippingMethods(Cart cart,
			Country country) {
		Storage storage = shippingService.getShippingStorage(
				mobileService.getWebSiteID(), country, cart.getListingIDs());
		Double weight = freightService.getTotalWeight(cart);
		Double shippingWeight = freightService.getTotalWeight(cart, true);
		Boolean isSpecial = shippingMethodService.isSpecial(cart
				.getListingIDs());
		ShippingMethodRequst requst = new ShippingMethodRequst(
				storage.getIid(), country.getCshortname(), weight,
				shippingWeight, mobileService.getLanguageID(),
				cart.getBaseTotal(), cart.getListingIDs(), isSpecial,
				mobileService.getCurrency(), mobileService.getWebSiteID(),
				false);
		// 托运方式
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
	}

	public List<CartItemInfo> getCartItemInfo(Cart cart) {
		List<CartItem> list = cart.getAllItems();
		if (list != null && list.size() > 0) {
			List<CartItemInfo> resultList = Lists.transform(list,
					new Function<CartItem, CartItemInfo>() {
						@Override
						public CartItemInfo apply(CartItem cartItem) {
							CartItemInfo itemInfo = new CartItemInfo();
							itemInfo.setExtra(ValidataUtils
									.validataStr(cartItem.getAddition()));
							itemInfo.setIid(cartItem.getCid());
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

	private CartGetRequest getCartRequest(String uuid) {
		String email = null;
		if (loginService.isLogin()) {
			email = loginService.getLoginMemberEmail();
		}
		int siteid = mobileService.getWebSiteID();
		int lang = mobileService.getLanguageID();
		String ccy = mobileService.getCurrency();
		return new CartGetRequest(email, uuid, siteid, lang, ccy);
	}

	public Cart getCurrentCart(String uuid, boolean createIfNotExist) {
		CartGetRequest req = getCartRequest(uuid);
		Cart cart = null;
		if (createIfNotExist) {
			cart = getOrCreateCart(req);
		} else {
			cart = getCart(req);
		}
		if (cart != null) {
			InjectorInstance.getInjector().injectMembers(cart);
		}
		return cart;
	}

	/**
	 * 获取或这新建一个购物车
	 * 
	 * @return
	 */
	private Cart getOrCreateCart(CartGetRequest getReq) {
		Cart c = getCart(getReq);
		if (c == null) {
			c = cartLifecycle.createCart(new CartCreateRequest(getReq
					.getEmail(), getReq.getLtc(), getReq.getSiteID(), getReq
					.getLanguageID(), getReq.getCcy()));
		}
		return c;
	}

	private Cart getCart(CartGetRequest getReq) {
		return cartLifecycle.getCart(getReq);
	}

	/**
	 * 清空 或者 删除购物车item货物
	 * 
	 * @param lId
	 * @param isall
	 * @return
	 */
	public boolean delCart(String uuid, String lId, String isall) {
		Cart cart = getCurrentCart(uuid, true);
		String email = null;
		if (loginService.isLogin()) {
			email = loginService.getLoginMemberEmail();
		}
		if (isall != null && "1".equals(isall)) {
			cart.clear();
			return true;
		}
		List<String> listingIDs = cart.getListingIDsByItemID(lId);
		boolean flag = cart.deleteItem(lId);
		if (flag) {
			cart.addItemHistory(listingIDs, email, uuid);
		}
		return flag;
	}

	/**
	 * 删除购物车 物品 item 或item下的一种商品
	 * 
	 * @param iid
	 *            ItemID
	 * @param lid
	 *            listingID
	 * @param isMain
	 */
	public boolean deleteItemList(String uuid, String iid, String lid,
			Boolean isMain) {
		if (StringUtils.isEmpty(iid) || StringUtils.isEmpty(lid)) {
			return false;
		}
		String email = null;
		if (loginService.isLogin()) {
			email = loginService.getLoginMemberEmail();
		}
		Cart cart = getCurrentCart(uuid, true);
		boolean flag = cart.deleteItem(iid);
		// if (!isMain) {
		// flag = cartService.deleteCartItemList(iid, lid);
		// } else if (isMain) {
		// flag = cartService.deleteMainItemList(iid, lid, cart);
		// }
		// if (flag) {
		// List<String> listingList = Lists.newArrayList();
		// listingList.add(lid);
		// cart.addItemHistory(listingList, email, uuid);
		// }
		return flag;
	}

	/**
	 * 添加购物车
	 * 
	 * @return
	 */
	public Map addCartItem(String uuid, String clistingid, int num) {
		// Single 代表单品
		SingleCartItem cartItem = new SingleCartItem();
		cartItem.setClistingid(clistingid);
		cartItem.setIqty(num);
		cartItem.setBismain(false);
		cartItem.setDevice(mobileService.getAppName());
		Cart cart = getCurrentCart(uuid, true);
		return cart.addItem(cartItem);
	}

	/**
	 * 修改数量
	 * 
	 * @param cid
	 * @param num
	 * @return
	 */
	public Map editNum(String uuid, String iid, Integer num) {
		Map<String, Object> result = new HashMap<String, Object>();
		Cart cart = getCurrentCart(uuid, true);
		if (cart != null) {
			Map resmap = cart.updateItemQty(iid, num);
			if (resmap.get("res").equals("success")) {
				Map<String, Object> map = cartService.getPriceByCartItemId(iid,
						num);
				result.putAll(map);
			}
			result.putAll(resmap);
		}
		return result;
	}

	public int getItemCount(String uuid) {
		if (StringUtils.isNotBlank(uuid)) {
			Cart cart = getCurrentCart(uuid, true);
			if (cart != null) {
				return cart.getAllItems().size();
			}
		}
		return 0;
	}

	public void synchroMemberCart(String uuid, String email) {
		MobileContext ctx = mobileService.getMobileContext();
		LoginEvent event = new LoginEvent(uuid, uuid, ctx == null ? ""
				: ctx.getIp(), mobileService.getWebSiteID(), email, "app");
		webloginService.executeLoginProcess(event);
	}

	public Cart getCartById(String cartId) {
		return cartLifecycle.getCart(cartId);
	}

	/**
	 * 老购物车生成新购物车
	 * 
	 * @param oldCart
	 * @return true
	 */
	public List<valueobjects.cart.CartItem> transformCart(List<CartItem> oldCart) {
		List<valueobjects.cart.CartItem> newCart = new ArrayList<valueobjects.cart.CartItem>();
		// cart属性
		String cid;
		String clistingid;
		String cuuid;
		String cmemberemail;
		String ctitle;
		String cimageurl;
		String curl;
		Integer iqty;
		Price price;
		Boolean bismain;
		Date dcreatedate;
		String sku;
		String addition;
		Map<String, String> attributeMap;
		Integer storageID;
		String device;
		// price属性
		double unitBasePrice;
		double unitPrice;
		double dprice;
		Double discount;
		Date validFrom;
		Date validTo;
		String currency;
		String symbol = "US$";
		boolean isDiscounted;

		for (CartItem cartItem : oldCart) {
			cid = cartItem.getCid();
			clistingid = cartItem.getClistingid();
			cuuid = cartItem.getCuuid();
			cmemberemail = cartItem.getCmemberemail();
			ctitle = cartItem.getCtitle();
			cimageurl = cartItem.getCimageurl();
			curl = cartItem.getCurl();
			iqty = cartItem.getIqty();
			price = cartItem.getPrice();
			bismain = cartItem.getBismain();
			dcreatedate = cartItem.getDcreatedate();
			sku = cartItem.getSku();
			addition = cartItem.getAddition();
			attributeMap = cartItem.getAttributeMap();
			storageID = cartItem.getStorageID();
			device = cartItem.getDevice();
			Logger.debug("cid = {}", cid);
			Logger.debug("clistingid = {}", clistingid);
			Logger.debug("cuuid = {}", cuuid);
			Logger.debug("cmemberemail = {}", cmemberemail);
			Logger.debug("ctitle = {}", ctitle);
			Logger.debug("cimageurl = {}", cimageurl);
			Logger.debug("curl = {}", curl);
			Logger.debug("bismain = {}", bismain);
			Logger.debug("sku = {}", sku);
			Logger.debug("storageID = {}", storageID);

			valueobjects.cart.CartItem nc = new valueobjects.cart.CartItem();
			nc.setCid(cid);
			nc.setClistingid(clistingid);
			nc.setCuuid(cuuid);
			nc.setCmemberemail(cmemberemail);
			nc.setCtitle(ctitle);
			nc.setCimageurl(cimageurl);
			nc.setCurl(curl);
			nc.setIqty(iqty);
			nc.setBismain(bismain);
			nc.setDcreatedate(dcreatedate);
			nc.setSku(sku);
			nc.setAddition(addition);
			nc.setAttributeMap(attributeMap);
			nc.setStorageID(storageID);
			nc.setCdevice(device);

			unitBasePrice = price.getUnitBasePrice();
			unitPrice = price.getUnitPrice();
			dprice = price.getPrice();
			discount = price.getDiscount();
			validFrom = price.getValidFrom();
			validTo = price.getValidTo();
			currency = price.getCurrency();
			symbol = price.getSymbol();
			isDiscounted = price.isDiscounted();
			valueobjects.cart.Price newPrice = new valueobjects.cart.Price();
			newPrice.setUnitBasePrice(unitBasePrice);
			newPrice.setUnitPrice(unitPrice);
			newPrice.setPrice(dprice);
			newPrice.setDiscount(discount);
			newPrice.setValidFrom(validFrom);
			newPrice.setValidTo(validTo);
			newPrice.setCurrency(currency);
			newPrice.setSymbol(symbol);
			newPrice.setDiscounted(isDiscounted);
			nc.setPrice(newPrice);
			newCart.add(nc);
		}

		return newCart;
	}

	/**
	 * 删除购物车商品
	 * 
	 * @param oldCart
	 * @return true
	 */
	public Integer deleteCart(String cartId) {
		Integer res = -1;
		res = cartService.updateCartIsCreateOrder(cartId);
		if (res <= 0) {
			Logger.error("deleteCart updateCartIsCreateOrder error");
		}
		cartService.delCartByCid(cartId);
		return res;
	}
}
