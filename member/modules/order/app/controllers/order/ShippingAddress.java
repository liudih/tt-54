package controllers.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Iterators;
import org.elasticsearch.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.cart.ICartLifecycleService;
import services.cart.ICartServices;
import services.member.address.AddressService;
import services.member.login.LoginService;
import services.order.IOrderEnquiryService;
import services.order.IPreparatoryDetailService;
import services.order.IPreparatoryOrderService;
import services.order.fragment.pretreatment.AddressPretreatment;
import services.order.fragment.provider.OrderShippingMethodProvider;
import services.order.fragment.provider.PreparatoryOrderProvider;
import services.shipping.IShippingMethodService;
import services.shipping.IShippingServices;
import services.shipping.ShippingMethodService;
import valueobjects.base.LoginContext;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import dto.Country;
import dto.Currency;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.PreparatoryDetail;
import dto.order.PreparatoryOrder;
import facades.cart.Cart;
import forms.member.address.ShippingAddressForm;

import com.google.common.collect.Collections2;

import services.IStorageService;

public class ShippingAddress extends Controller {

	@Inject
	private ICartLifecycleService cartService;
	@Inject
	private OrderShippingMethodProvider prvider;
	@Inject
	private CountryService countryService;
	@Inject
	private LoginService loginService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private FoundationService foundationService;
	@Inject
	private AddressService addressService;
	@Inject
	private IOrderEnquiryService orderEnquiry;
	@Inject
	private AddressPretreatment addressPretreatment;
	@Inject
	private IPreparatoryDetailService preparatoryDetailService;
	@Inject
	private IPreparatoryOrderService preparatoryOrderService;
	@Inject
	private PreparatoryOrderProvider preparatoryOrderProvider;
	@Inject
	private IShippingMethodService shippingMethodService;
	// add by lijun
	@Inject
	IShippingServices shippingServices;

	@Inject
	ICartServices icartServices;

	@Inject
	IStorageService iStorageService;

	final static int SHIPPING_ADDRESS_TYPE = 1;

	final static int ORDER_ADDRESS_TYPE = 2;

	final static int NOT_ERROR = 0;

	final static int REQUIRED_ERROR = 1;

	final static int SERVER_ERROR = 2;

	public Result modifyShippingAddress() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Form<ShippingAddressForm> form = Form.form(ShippingAddressForm.class)
				.bindFromRequest();
		resultMap.put("actionType", "modify");
		String email = loginService.getLoginData().getEmail();
		Map<Integer, Country> countryMap = Maps.newHashMap();
		if (form.hasErrors()) {
			resultMap.put("errorCode", REQUIRED_ERROR);
			resultMap.put("required", form.errorsAsJson());
			return ok(Json.toJson(resultMap));
		}

		ShippingAddressForm addressForm = form.get();
		String addressType = addressForm.getAddressType();

		// 后台校验
		MemberAddress memberAddress = addressService
				.getMemberAddressById(addressForm.getIid());
		if (null == memberAddress) {
			resultMap.put("errorCode", REQUIRED_ERROR);
			return ok(Json.toJson(resultMap));
		}

		MemberAddress address = new MemberAddress();
		BeanUtils.copyProperties(addressForm, address);

		boolean defaultAddr = addressForm.getBdefault() == null ? false : true;
		address.setBdefault(defaultAddr);
		address.setCmemberemail(loginService.getLoginData().getEmail());

		if (addressType.equals("shipping")) {
			address.setIaddressid(SHIPPING_ADDRESS_TYPE);
			Integer sCount = addressService
					.getShippingAddressCountByEmail(email);
			// 当只有一个地址时，不管客户是否选择默认，都设置为默认
			if (sCount == 1) {
				address.setBdefault(true);
			} else {
				address.setBdefault(defaultAddr);
			}
			resultMap.put("addressType", "shipping");
		} else {
			address.setIaddressid(ORDER_ADDRESS_TYPE);
			Integer oCount = addressService.getOrderAddressCountByEmail(email);
			if (oCount == 0) {
				address.setBdefault(true);
			} else {
				address.setBdefault(defaultAddr);
			}
			resultMap.put("addressType", "billing");
		}

		boolean result = addressService.updateMemberShippingAddress(address);
		if (result) {
			countryMap
					.put(address.getIcountry(), countryService
							.getCountryByCountryId(address.getIcountry()));
			resultMap.put("modifyId", address.getIid());
			resultMap.put("isdefault", address.getBdefault());
			resultMap.put("errorCode", NOT_ERROR);
			String modifyActionUrl = controllers.order.routes.ShippingAddress
					.modifyShippingAddress().url();
			List<Country> countries = countryService.getAllCountries();
			Html html = null;
			if (addressType.equals("shipping")) {
				html = views.html.member.address.shipping_address_table_tr
						.render(address, countryMap, countries, modifyActionUrl);
			} else {
				html = views.html.member.address.billing_address_table_tr
						.render(address, countryMap, countries, modifyActionUrl);
			}
			resultMap.put("addressHtml", html.toString());
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", SERVER_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result addNewShippingAddress() {
		String email = loginService.getLoginData().getEmail();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Form<ShippingAddressForm> form = Form.form(ShippingAddressForm.class)
				.bindFromRequest();
		Map<Integer, Country> countryMap = Maps.newHashMap();
		resultMap.put("actionType", "add");

		if (form.hasErrors()) {
			resultMap.put("errorCode", REQUIRED_ERROR);
			resultMap.put("required", form.errorsAsJson());
			return ok(Json.toJson(resultMap));
		}

		ShippingAddressForm addressForm = form.get();
		MemberAddress shippingAddress = new MemberAddress();
		BeanUtils.copyProperties(addressForm, shippingAddress);
		shippingAddress.setCmemberemail(email);
		boolean defaultAddr = addressForm.getBdefault() == null ? false : true;
		boolean copytoBillingAddress = addressForm.getCopytoBillingAddress() == null ? false
				: true;
		String addressType = addressForm.getAddressType();
		Integer sCount = addressService.getShippingAddressCountByEmail(email);
		// 当第一次添加地址时，不管客户是否选择默认，都设置为默认
		if (sCount == 0) {
			shippingAddress.setBdefault(true);
		} else {
			shippingAddress.setBdefault(defaultAddr);
		}

		if (addressType.equals("shipping")) {
			shippingAddress.setIaddressid(SHIPPING_ADDRESS_TYPE);
		} else {
			shippingAddress.setIaddressid(ORDER_ADDRESS_TYPE);
		}

		boolean result = addressService.addNewAddress(shippingAddress);
		List<Country> countryList = countryService.getAllCountries();
		countryMap.put(shippingAddress.getIcountry(), countryService
				.getCountryByCountryId(shippingAddress.getIcountry()));
		String actionUrl = controllers.order.routes.ShippingAddress
				.modifyShippingAddress().url();

		if (addressType.equals("shipping")) {
			// 当在添加收货地址时勾选了设置为账单地址，或者在没有账单地址的时候，会添加一个与新添加的收货地址一样的账单地址
			Integer orderAddressCount = addressService
					.getOrderAddressCountByEmail(email);
			if (orderAddressCount == 0 || copytoBillingAddress == true) {
				MemberAddress orderAddress = new MemberAddress();
				BeanUtils.copyProperties(addressForm, orderAddress);
				orderAddress.setCmemberemail(email);
				orderAddress.setIaddressid(ORDER_ADDRESS_TYPE);
				orderAddress.setBdefault(true);
				boolean saveOrderAddressResult = addressService
						.addNewAddress(orderAddress);
				if (saveOrderAddressResult == false) {
					resultMap.put("errorCode", SERVER_ERROR);
					return ok(Json.toJson(resultMap));
				}
				Html orderHtml = views.html.member.address.billing_address_table_tr
						.render(orderAddress, countryMap, countryList,
								actionUrl);
				resultMap.put("orderAddressHtml", orderHtml.toString());
				resultMap.put("addressType", "both");
				resultMap.put("orderAddressId", orderAddress.getIid());
			} else {
				resultMap.put("addressType", "shipping");
			}

			if (result) {
				Html html = views.html.member.address.shipping_address_table_tr
						.render(shippingAddress, countryMap, countryList,
								actionUrl);
				resultMap.put("errorCode", NOT_ERROR);
				resultMap.put("addressHtml", html.toString());
				resultMap.put("isdefault", shippingAddress.getBdefault());
				resultMap.put("addedId", shippingAddress.getIid());
				return ok(Json.toJson(resultMap));
			}
		} else if (addressType.equals("billing")) {
			if (result) {
				countryMap.put(shippingAddress.getIcountry(), countryService
						.getCountryByCountryId(shippingAddress.getIcountry()));
				String modifyActionUrl = controllers.order.routes.ShippingAddress
						.modifyShippingAddress().url();
				Html html = views.html.member.address.billing_address_table_tr
						.render(shippingAddress, countryMap, countryList,
								modifyActionUrl);
				resultMap.put("errorCode", NOT_ERROR);
				resultMap.put("orderAddressHtml", html.toString());
				resultMap.put("isdefault", shippingAddress.getBdefault());
				resultMap.put("addedId", shippingAddress.getIid());
				resultMap.put("addressType", "billing");
				return ok(Json.toJson(resultMap));
			}
		}
		resultMap.put("errorCode", SERVER_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result refreshShippingMethod(String cartId, Integer addressId) {
		try {
			Cart cart = cartService.getCart(cartId);
			if (null != cart) {
				MemberAddress address = addressService
						.getMemberAddressById(addressId);
				if (null != address) {
					Currency currency = currencyService
							.getCurrencyByCode(foundationService.getCurrency());
					ShippingMethodInformations fragment = (ShippingMethodInformations) prvider
							.getFragment(cart, address);
					return ok(views.html.order.shipping_method_information
							.render(fragment, currency));
				}
			}
		} catch (Exception e) {
			Logger.error("ShippingAddress refreshShippingMethod", e);
		}
		return badRequest();
	}

	public Result refreshPreparaoryShipping(Integer orderID, Integer addressId) {
		try {
			PreparatoryOrder order = preparatoryOrderService.getByID(orderID);
			MemberAddress address = addressService
					.getMemberAddressById(addressId);
			Currency cy = currencyService.getCurrencyByCode(foundationService
					.getCurrency());
			if (order != null && address != null) {
				Country country = countryService.getCountryByCountryId(address
						.getIcountry());
				if (country != null) {
					List<PreparatoryDetail> details = preparatoryDetailService
							.getByOrderID(orderID);
					int langID = foundationService.getLanguage();
					ShippingMethodRequst req = preparatoryOrderProvider
							.createRequset(order, details,
									country.getCshortname(), langID);
					ShippingMethodInformations infos = shippingMethodService
							.getShippingMethodInformations(req);
					return ok(views.html.order.shipping_info.render(infos, cy));
				}
			}
		} catch (Exception e) {
			Logger.error("ShippingAddress refreshShippingMethod", e);
		}
		return badRequest();
	}

	public Result refreshByOrder(String orderNumber, Integer addressId) {
		try {
			Order order = orderEnquiry.getOrderById(orderNumber);
			if (null != order) {
				List<OrderDetail> details = orderEnquiry
						.getOrderDetails(orderNumber);
				MemberAddress address = addressService
						.getMemberAddressById(addressId);
				if (null != address) {
					Currency currency = currencyService
							.getCurrencyByCode(foundationService.getCurrency());
					Country country = null;
					MemberAddress memberAddresses = addressService
							.getDefaultShippingAddress(order.getCmemberemail());
					if (null != memberAddresses) {
						Integer countryId = memberAddresses.getIcountry();
						country = countryService
								.getCountryByCountryId(countryId);
					} else {
						country = foundationService.getCountryObj();
					}
					Integer storageID = addressPretreatment.getStorageId(order,
							details, country);
					ShippingMethodInformations fragment = (ShippingMethodInformations) prvider
							.getExistingFragment(order, storageID, country,
									details);
					return ok(views.html.order.shipping_method_information
							.render(fragment, currency));
				}
			}
		} catch (Exception e) {
			Logger.error("ShippingAddress refreshShippingMethod", e);
		}
		return badRequest();
	}

	public Result refreshShipMethodForGuest(String orderNumber,
			String shipToCountryCode) {
		if (orderNumber == null || orderNumber.length() == 0
				|| shipToCountryCode == null || shipToCountryCode.length() == 0) {
			return badRequest();
		}
		// 某些国家被屏蔽
		Country country = countryService
				.getCountryByShortCountryName(shipToCountryCode);
		Boolean isShow = country.getBshow();
		if (isShow != null && !isShow) {
			Logger.debug("country:{} is not show,so can not get ship method",
					shipToCountryCode);
			return ok(Html.apply(""));
		}

		try {
			Order order = orderEnquiry.getOrderById(orderNumber);
			if (null != order) {
				List<OrderDetail> details = orderEnquiry
						.getOrderDetails(orderNumber);
				Currency currency = currencyService.getCurrencyByCode(order
						.getCcurrency());

				/*
				 * Integer storageID = addressPretreatment.getStorageId(order,
				 * details, country);
				 */
				Integer storageID = order.getIstorageid();
				if (storageID == null) {
					Logger.debug("order:{} storageID is null",
							order.getCordernumber());
					return badRequest();
				}
				ShippingMethodInformations fragment = (ShippingMethodInformations) prvider
						.getExistingFragment(order, storageID, country, details);
				return ok(views.html.order.shipping_method_information.render(
						fragment, currency));
			}
		} catch (Exception e) {
			Logger.error("ShippingAddress refreshShippingMethod", e);
		}
		return badRequest();
	}

	/**
	 * @author lijun
	 * @return
	 */
	public Result addShipAddress() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		LoginContext loginCtx = this.foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			resultMap.put("succeed", false);
			return ok(Json.toJson(resultMap));
		}
		String email = loginCtx.getMemberID();

		Form<ShippingAddressForm> form = Form.form(ShippingAddressForm.class)
				.bindFromRequest();

		resultMap.put("succeed", true);
		if (form.hasErrors()) {
			resultMap.put("succeed", false);
			resultMap.put("required", form.errorsAsJson());
			return ok(Json.toJson(resultMap));
		}

		ShippingAddressForm addressForm = form.get();

		MemberAddress shippingAddress = new MemberAddress();
		BeanUtils.copyProperties(addressForm, shippingAddress);

		shippingAddress.setCmemberemail(email);

		Integer id = shippingAddress.getIid();

		boolean result;

		if (id == null) {
			Integer sCount = addressService
					.getShippingAddressCountByEmail(email);
			// 当第一次添加地址时，不管客户是否选择默认，都设置为默认
			if (sCount == 0) {
				shippingAddress.setBdefault(true);
			} else {
				shippingAddress.setBdefault(false);
			}
			shippingAddress.setIaddressid(SHIPPING_ADDRESS_TYPE);
			result = addressService.addNewAddress(shippingAddress);
			// 添加bill地址
			MemberAddress billAddress = new MemberAddress();
			BeanUtils.copyProperties(shippingAddress, billAddress);
			billAddress.setIaddressid(ORDER_ADDRESS_TYPE);
			billAddress.setIid(null);
			result = addressService.addNewAddress(billAddress);
		} else {
			MemberAddress md = addressService.getMemberAddressById(id);
			String cemail = md.getCmemberemail();
			if (!email.equals(cemail)) {
				resultMap.put("succeed", false);
				return ok(Json.toJson(resultMap));
			}
			result = addressService
					.updateMemberShippingAddress(shippingAddress);
		}

		if (!result) {
			resultMap.put("succeed", false);
		}
		resultMap.put("id", shippingAddress.getIid());
		return ok(Json.toJson(resultMap));
	}

	/**
	 * @author lijun
	 * @param shipToCountryCode
	 * @return
	 */
	public Result getShipMethod(String shipToCountryCode, Integer storageid) {
		Logger.debug("shipToCountryCode:{}---{}", shipToCountryCode, storageid);
		Map<String, Object> mjson = new HashMap<String, Object>();
		if (shipToCountryCode == null || shipToCountryCode.length() == 0) {
			mjson.put("result", "shipToCountryCode is null");
			return ok(Json.toJson(mjson));
		}
		// 某些国家被屏蔽
		Country country = countryService
				.getCountryByShortCountryName(shipToCountryCode);
		Boolean isShow = country.getBshow();
		if (isShow != null && !isShow) {
			Logger.debug("country:{} is not show,so can not get ship method",
					shipToCountryCode);
			mjson.put("result", "country:" + shipToCountryCode + " is not show");
			return ok(Json.toJson(mjson));
		}
		int site = this.foundationService.getSiteID();
		int lang = this.foundationService.getLanguage();
		String currencyCode = this.foundationService.getCurrency();

		List<CartItem> items = this.icartServices.getAllItems(site, lang,
				currencyCode);
		if (items == null || items.size() == 0) {
			mjson.put("result", "Cart items is null");
			return ok(Json.toJson(mjson));
		}
		// 过滤仓库id
		Integer stid = storageid;
		items = Lists.newArrayList(Collections2.filter(items,
				c -> c.getStorageID() == stid));
		if (items.size() == 0) {
			mjson.put("result", "Cart items is null ");
			return ok(Json.toJson(mjson));
		}
		// 转为真实仓
		List<dto.Storage> storagelist = iStorageService.getAllStorages();
		List<dto.Storage> newstoragelist = Lists.newArrayList(Collections2
				.filter(storagelist, c -> c.getIparentstorage() == stid));
		newstoragelist.sort((p1, p2) -> p1.getIid().compareTo(p2.getIid()));
		if (newstoragelist != null && newstoragelist.size() > 0) {
			storageid = newstoragelist.get(0).getIid();
			Logger.debug("get real storage -- > {} -- {} ", stid, storageid);
			int tstorageid = storageid;
			items = Lists.transform(items, p -> {
				p.setStorageID(tstorageid);
				return p;
			});
		}

		try {
			// 判断所有商品是否是同一个仓库
			Integer firstStorage = items.get(0).getStorageID();
			Logger.debug("firstStorage -- > {} -- ", firstStorage);
			List<String> listingId = Lists.newLinkedList();
			FluentIterable
					.from(items)
					.forEach(
							item -> {
								if (item instanceof valueobjects.cart.SingleCartItem) {
									listingId.add(item.getClistingid());
								} else if (item instanceof valueobjects.cart.BundleCartItem) {
									List<valueobjects.cart.SingleCartItem> childs = ((valueobjects.cart.BundleCartItem) item)
											.getChildList();
									List<String> clisting = Lists.transform(
											childs, c -> c.getClistingid());
									listingId.addAll(clisting);
								}
							});

			/*
			 * boolean isSameStorage = shippingServices.isSameStorage(listingId,
			 * firstStorage.toString()); if (!isSameStorage) {
			 * Logger.debug("isSameStorage:{},so cant not get ship method",
			 * isSameStorage); mjson.put("result", "Storage is not same!");
			 * return ok(Json.toJson(mjson)); }
			 */

			Currency currency = currencyService.getCurrencyByCode(currencyCode);

			ShippingMethodInformations fragment = (ShippingMethodInformations) prvider
					.getFragment(items, shipToCountryCode, firstStorage);
			mjson.put("result", "success");
			mjson.put("html", views.html.order.shipping_method_information
					.render(fragment, currency).toString());
			return ok(Json.toJson(mjson));
		} catch (Exception e) {
			Logger.error("ShippingAddress refreshShippingMethod", e);
		}
		mjson.put("result", "error");
		return ok(Json.toJson(mjson));
	}

}
