package services.mobile.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import mapper.base.CountryMapper;
import mapper.cart.CartBaseMapper;
import mapper.cart.CartItemListMapper;
import mapper.cart.CartItemMapper;
import mapper.interaction.ProductCollectMapper;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Collections2;
import org.springframework.beans.BeanUtils;

import play.Logger;
import services.base.CurrencyService;
import services.base.utils.Utils;
import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.cart.ICartLifecycleService;
import services.cart.ICartService;
import services.common.UUIDGenerator;
import services.loyalty.IPointsService;
import services.loyalty.coupon.CouponCodeService;
import services.loyalty.coupon.ICouponService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.personal.AddressService;
import services.order.IFreightService;
import services.product.ProductLabelService;
import services.search.criteria.ProductLabelType;
import services.shipping.IShippingMethodService;
import services.shipping.ShippingServices;
import utils.ValidataUtils;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.cart.CartCreateRequest;
import valueobjects.order_api.cart.CartGetRequest;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.SingleCartItem;
import valueobjects.order_api.shipping.ShippingMethodRequst;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import dao.product.IProductBaseEnquiryDao;
import dto.Country;
import dto.Currency;
import dto.Storage;
import dto.cart.CartBase;
import dto.cart.CartItemList;
import dto.interaction.ProductCollect;
import dto.mobile.AddressInfo;
import dto.mobile.CartItemInfo;
import dto.mobile.ShippingMethodInfo;
import entity.loyalty.Coupon;
import entity.loyalty.CouponRule;
import entity.loyalty.business.CouponCodeBo;
import enums.loyalty.coupon.manager.CouponRuleBack;
import extensions.InjectorInstance;
import extensions.loyalty.campaign.rule.memberactive.ConponActionRule;
import extensions.loyalty.campaign.rule.memberactive.CouponRuleActionParameter;
import facades.cart.Cart;
import facades.cart.CartItemComparator;

public class CartInfoService {

	@Inject
	ICartService cartService;

	@Inject
	LoginService loginService;

	@Inject
	CountryMapper countryMapper;

	@Inject
	CartBaseMapper cartBaseMapper;

	@Inject
	MobileService mobileService;

	@Inject
	AddressService addressService;

	@Inject
	ShippingServices shippingService;

	@Inject
	IFreightService freightService;

	@Inject
	IPointsService pointsService;

	// @Inject
	// ICouponService couponService;

	@Inject
	CouponCodeService codeService;

	@Inject
	CampaignContextFactory ctxFactory;

	@Inject
	CurrencyService currencyService;

	@Inject
	IProductBaseEnquiryDao productBaseEnquityDao;

	@Inject
	IShippingMethodService shippingMethodService;

	@Inject
	ProductCollectMapper productCollectMapper;

	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	CartItemListMapper cartBaseListMapper;

	@Inject
	CartItemMapper cartItemMapper;

	@Inject
	private ProductLabelService productLabelService;

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
				Country country = countryMapper
						.getCountryByCountryId(defauladdress.getCountry());
				if (country != null) {
					String address = defauladdress.getStreet() + " "
							+ defauladdress.getCity() + " "
							+ defauladdress.getProvince() + " "
							+ country.getCname();
					result.put("aid",
							ValidataUtils.validataInt(defauladdress.getIid()));
					result.put("addr", address);
					result.put("name", defauladdress.getFirstname() + "."
							+ defauladdress.getFirstname() + "."
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
				Country country = countryMapper.getCountryByCountryId(icountry);
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
		// List<Coupon> coupons = couponService.selectMyCouponUnused(email);
		List<Integer> codeIds = new ArrayList<Integer>();
		// FluentIterable.from(coupons).forEach(c ->
		// codeIds.add(c.getCodeId()));

		// 去获取配置的rule
		List<CouponCodeBo> codes = codeService.getCouponCodesByCodeIds(codeIds);
		FluentIterable.from(codes).forEach(c -> {
			c.toString();
		});
		// 找出当前符合rule的可用coupon
		List<ConponActionRule> actionRules = new ArrayList<ConponActionRule>(
				codes.size());
		FluentIterable
				.from(codes)
				.forEach(
						c -> {
							CouponRule r = c.getCouponRule();
							if (r != null) {
								ConponActionRule actionRule = InjectorInstance
										.getInjector().getInstance(
												ConponActionRule.class);
								actionRule.setRuleId(r.getIid());
								CouponRuleActionParameter paras = new CouponRuleActionParameter(
										c.getIid());
								paras.setCode(c.getCcode());
								BeanUtils.copyProperties(r, paras);
								actionRule.setParameter(paras);
								actionRules.add(actionRule);
							}
						});
		// 开始过滤满足rule的优惠券
		CampaignContext context = ctxFactory.createContext(null, null);
		context.setActionOn(cart);
		FluentIterable<ConponActionRule> usableRule = FluentIterable.from(
				actionRules).filter(c -> c.match(context, null));
		String usercurrency = mobileService.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(usercurrency);
		usableRule
				.forEach(c -> {
					Double amount = c.getParameter().getFcouponamount();
					// 如果是现金券 转换币种为当前币种
					if (CouponRuleBack.CouponType.CASH == c.getParameter()
							.getType()) {

						if (!StringUtils.isEmpty(usercurrency)) {
							try {
								int currentCurrencyId = c.getParameter()
										.getCcurrency();
								Currency currentCurrency = currencyService
										.getCurrencyById(currentCurrencyId);
								if (!usercurrency.equals(currentCurrency
										.getCcode())) {
									amount = currencyService.exchange(amount,
											currentCurrency.getCcode(),
											usercurrency);
								}
							} catch (Exception e) {
								Logger.error("exchange currency failed", e);
							}
						}
					}
					Map<String, Object> map = new HashMap<String, Object>();
					if (CouponRuleBack.CouponType.CASH == c.getParameter()
							.getType()) {
						map.put("value",
								Utils.money(amount) + " "
										+ currency.getCsymbol());
					} else {
						double discount = c.getParameter().getFdiscount();
						map.put("value", Utils.money(discount) + "% OFF");
					}
					map.put("code", c.getParameter().getCode());
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
				hasAllFreeShipping(cart.getListingIDs()));
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
									.validataStr(cartItem.getCimageurl()));
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
							int stock = productBaseEnquityDao
									.getProductQtyByListingID(cartItem
											.getClistingid());
							itemInfo.setStock(stock);
							// 判断该商品是否收藏
							if (loginService.isLogin()) {
								List<ProductCollect> collects = productCollectMapper
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
		if (createIfNotExist) {
			return getOrCreateCart(req);
		} else {
			return getCart(req);
		}
	}

	/**
	 * 获取或这新建一个购物车
	 * 
	 * @return
	 */
	private Cart getOrCreateCart(CartGetRequest getReq) {
		Cart c = getCart(getReq);
		if (c == null) {
			c = createCart(new CartCreateRequest(getReq.getEmail(),
					getReq.getLtc(), getReq.getSiteID(),
					getReq.getLanguageID(), getReq.getCcy()));
		}
		return c;
	}

	private Cart createCart(CartCreateRequest createReq) {
		String id = null;
		CartBase cbase = new CartBase();
		cbase.setCid(UUIDGenerator.createAsString());
		cbase.setBgenerateorders(false);
		cbase.setCcreateuser(createReq.getEmail());
		cbase.setCmemberemail(createReq.getEmail());
		cbase.setCuuid(createReq.getLtc());
		cbase.setDcreatedate(new Date());
		cbase.setIwebsiteid(createReq.getSiteID());
		cartBaseMapper.insertSelective(cbase);
		id = cbase.getCid();
		Logger.debug("Cart {} created", id);
		return createCartInstanceWithInjectedMembers(
				id,
				createReq.getSiteID() != null ? createReq.getSiteID()
						: mobileService.getWebSiteID(),
				createReq.getLanguageID() != null ? createReq.getLanguageID()
						: mobileService.getLanguageID(),
				createReq.getCcy() != null ? createReq.getCcy() : mobileService
						.getCurrency());
	}

	private Cart createCartInstanceWithInjectedMembers(String id, int siteID,
			int languageID, String ccy) {
		Cart cart = new Cart(id, siteID, languageID, ccy);
		InjectorInstance.getInjector().injectMembers(cart);
		return cart;
	}

	private Cart getCart(CartGetRequest getReq) {
		String id = null;
		CartBase base = null;
		if (getReq.isAnonymous()) {
			base = cartBaseMapper.selectByUuid(getReq.getLtc(), false,
					getReq.getSiteID());
		} else {
			base = cartBaseMapper.selectByEmail(getReq.getEmail(), false,
					getReq.getSiteID());
		}
		if (base != null) {
			id = base.getCid();
			return createCartInstanceWithInjectedMembers(
					id,
					getReq.getSiteID() != null ? getReq.getSiteID()
							: mobileService.getWebSiteID(),
					getReq.getLanguageID() != null ? getReq.getLanguageID()
							: mobileService.getLanguageID(),
					getReq.getCcy() != null ? getReq.getCcy() : mobileService
							.getCurrency());
		}
		return null;
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
		boolean flag = false;
		Cart cart = getCurrentCart(uuid, true);
		if (!isMain) {
			flag = cartService.deleteCartItemList(iid, lid);
		} else if (isMain) {
			flag = cartService.deleteMainItemList(iid, lid, cart);
		}
		if (flag) {
			List<String> listingList = Lists.newArrayList();
			listingList.add(lid);
			cart.addItemHistory(listingList, email, uuid);
		}
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
	public Map<String, Object> editNum(String uuid, String iid, Integer num) {
		Map<String, Object> result = new HashMap<String, Object>();
		Cart cart = getCurrentCart(uuid, true);
		if (cart != null) {
			Map<String, Object> resmap = cart.updateItemQty(iid, num);
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
		try {
			CartGetRequest request = new CartGetRequest(email, uuid,
					mobileService.getWebSiteID(),
					mobileService.getLanguageID(), mobileService.getCurrency());
			Cart cart = cartLifecycle.getOrCreateCart(request);
			List<CartItemList> cartItemList = cartBaseListMapper
					.getCartItemListByUUID(request.getLtc(),
							request.getSiteID());
			List<dto.cart.CartItem> cartItem = cartItemMapper
					.getCartItemsByUUID(request.getLtc(), request.getSiteID());
			Multimap<String, CartItemList> cartItemListIndex = Multimaps.index(
					cartItemList, cilist -> cilist.getCcartitemid());
			Map<String, dto.cart.CartItem> cartItemIndex = Maps.uniqueIndex(
					cartItem, i -> i.getCid());
			Map<String, CartItem> result = Maps.transformValues(cartItemIndex,
					ci -> cartService.convertFromEntityLite(ci,
							cartItemListIndex));
			List<valueobjects.order_api.cart.CartItem> resultItems = Lists
					.newArrayList(result.values());
			Logger.debug("nologin resultItems ===={}", resultItems.size());
			List<valueobjects.order_api.cart.CartItem> existingItems = cartService
					.getCartItemsLiteByCartId(cart.getId());
			Logger.debug("login existingItems ===={}", existingItems.size());
			CartItemComparator cc = new CartItemComparator();
			for (valueobjects.order_api.cart.CartItem ct : resultItems) {
				Logger.debug("nologin cartitemcid ===={}", ct.getCid());
				Collection<valueobjects.order_api.cart.CartItem> dups = Collections2
						.filter(existingItems, i -> cc.compare(i, ct) == 0);
				Logger.debug("Dups size: {}", dups.size());
				if (dups.size() == 0) {
					cartItemMapper.updateItemForTransCart(ct.getCid(),
							cart.getId());
				} else if (dups.size() > 0) {
					valueobjects.order_api.cart.CartItem ci = dups.iterator()
							.next();
					Logger.debug("exist cartitem ===={}", ct.getCid());
					if (ct.getIqty() > ci.getIqty()) {
						cartService.updateItemQty(ci.getCid(), ct.getIqty());
					}
					cartService.delCartByCid(ct.getCid()); // 清空未登录时的购物车内容
				}
			}
		} catch (Exception e) {
			Logger.error("Cart Upgrade Exception", e);
		}
	}

	/**
	 * 所有免邮
	 * 
	 * @return
	 */
	private boolean hasAllFreeShipping(List<String> listingids) {
		// ~ 所有免邮
		List<String> allfp = productLabelService.getListByListingIdsAndType(
				listingids, ProductLabelType.AllFreeShipping.toString());
		return (allfp != null && allfp.size() > 0);
	}
}
