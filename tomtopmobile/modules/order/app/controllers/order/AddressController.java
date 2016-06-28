package controllers.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import plugins.mobile.order.provider.OrderShippingMethodProvider;
import services.ICountryService;
import services.ICurrencyService;
import services.base.FoundationService;
import services.cart.ICartServices;
import services.member.address.IAddressService;
import services.mobile.order.ShippingApiServices;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import services.shipping.IShippingMethodService;
import valueobjects.base.LoginContext;
import valueobjects.cart.CartItem;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.payment.PaymentContext;

import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.Country;
import dto.Currency;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.order.OrderDetail;
import enums.mobile.member.AddressType;
import forms.mobile.member.AddressForm;

/**
 * 
 * @author lijun
 *
 */
public class AddressController extends Controller {

	@Inject
	IAddressService addressService;

	@Inject
	FoundationService foundationService;

	@Inject
	private ICountryService countryService;
	@Inject
	private OrderShippingMethodProvider prvider;
	@Inject
	private IOrderEnquiryService orderEnquiry;
	@Inject
	private ICurrencyService currencyService;
	
	@Inject
	ShippingApiServices shippingApiServices;
	
	@Inject
	IOrderService orderService;
	
	@Inject
	ICartServices icartServices;
	
	/**
	 * 增加shipping address
	 * 
	 * @return
	 */
	public Result AddshippingAddress() {
		boolean succeed = true;
		Map<String, Object> feedback = Maps.newHashMap();

		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			return badRequest();
		}
		Form<AddressForm> form = Form.form(AddressForm.class).bindFromRequest();
		if (form.hasErrors()) {
			Map<String, List<ValidationError>> errors = form.errors();
			feedback.put("succeed", false);
			FluentIterable.from(errors.keySet()).forEach(k -> {
				feedback.put(k, "empty");
			});
			Logger.debug("{}", feedback.toString());
			return ok(Json.toJson(feedback));
		}

		AddressForm saf = form.get();
		// 验证国家在系统中是否存在
		Integer countryId = saf.getCountryId();
		Country country = foundationService.getCountry(countryId);
		if (country == null) {
			succeed = false;
			feedback.put("countryId", "invalid");
		}
		String street = saf.getStreet();
		if (street.length() == 0) {
			succeed = false;
			feedback.put("street", "empty");
		}
		String state = saf.getState();
		if (state.length() == 0) {
			succeed = false;
			feedback.put("state", "empty");
		}
		String city = saf.getCity();
		if (city.length() == 0) {
			succeed = false;
			feedback.put("city", "empty");
		}
		String postalCode = saf.getPostalCode();
		if (postalCode.length() == 0) {
			succeed = false;
			feedback.put("postalCode", "empty");
		}
		String phoneNumber = saf.getPhoneNumber();
		if (phoneNumber.length() == 0) {
			succeed = false;
			feedback.put("phoneNumber", "empty");
		}
		String firstName = saf.getFirstName();
		if (firstName.length() == 0) {
			succeed = false;
			feedback.put("firstName", "empty");
		}
		String laseName = saf.getLastName();
		if (laseName.length() == 0) {
			succeed = false;
			feedback.put("laseName", "empty");
		}
		// validate type
		AddressType type = AddressType.getType(saf.getType());
		if (type == null) {
			succeed = false;
			feedback.put("type", "invalid");
		}

		if (!succeed) {
			feedback.put("succeed", succeed);
			return ok(Json.toJson(feedback));
		}

		MemberAddress address = new MemberAddress();
		address.setCstreetaddress(saf.getStreet());
		address.setIcountry(saf.getCountryId());
		address.setCprovince(saf.getState());
		address.setCcity(saf.getCity());
		address.setCpostalcode(saf.getPostalCode());
		address.setCtelephone(saf.getPhoneNumber());
		String email = loginCtx.getMemberID();
		address.setCmemberemail(email);
		address.setCfirstname(saf.getFirstName());
		address.setClastname(saf.getLastName());

		// 如果当前用户没有地址,则把地址设置为默认地址
		if (AddressType.SHIPPING == type) {
			Integer sCount = addressService
					.getShippingAddressCountByEmail(email);
			if (sCount == null || sCount == 0) {
				address.setBdefault(true);
			}
		} else if (AddressType.BILLING == type) {
			Integer bCount = addressService.getOrderAddressCountByEmail(email);
			if (bCount == null || bCount == 0) {
				address.setBdefault(true);
			}
		}

		address.setIaddressid(type.getCode());
		Boolean result = addressService.addNewAddress(address);
		if (!result) {
			succeed = false;
		}
		feedback.put("succeed", succeed);

		return ok(Json.toJson(feedback));

	}
	
	public Result refreshShipMethodForGuest(String orderNumber,
			String shipToCountryCode) {
		Logger.debug("orderNumber == " + orderNumber);
		Logger.debug("shipToCountryCode == " + shipToCountryCode);
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

				Integer storageID = order.getIstorageid();
				if (storageID == null) {
					Logger.debug("order:{} storageID is null",
							order.getCordernumber());
					return badRequest();
				}
				//ShippingMethodInformations fragment = (ShippingMethodInformations) prvider
				//		.getExistingFragment(order, storageID, country, details);
				//OrderContext oc = new OrderContext(memberEmail, cart);
				ShippingMethodInformations fragment = (ShippingMethodInformations) prvider.
						getExistingFragment(order, storageID, country, details);
				return ok(views.html.mobile.paypal.paypal_shipping_method.render(
						fragment, currency));
				//return ok(views.html.mobile.order.shipping_method_information.render(fragment, currency));
			}
		} catch (Exception e) {
			Logger.error("ShippingAddress refreshShippingMethod", e);
		}
		return badRequest();
	}
	
	
	
	/**
	 * 
	 * 新版本 获取物流方式
	 * @param shipToCountryCode
	 * @param storageid
	 * @return
	 */
	public Result getNewShipMethod(Double totalPrice,
			Integer storageid, String orderNumber, Integer addressId,
			String countryCode) {
		Map<String,Object> mjson = new HashMap<String,Object>();
		int site = foundationService.getSiteID();
		int lang = foundationService.getLanguage();
		String currency = this.foundationService.getCurrency();

//		if (country==null || "".equals(country)) {
//			mjson.put("succeed", false);
//			mjson.put("error", "country is null");
//			return ok(Json.toJson(mjson));
//		}
		Country country = null;
		if(countryCode!=null && !"".equals(countryCode)){
			country = countryService
					.getCountryByShortCountryName(countryCode);
		}else if(addressId!=null){
			MemberAddress address = addressService.getMemberAddressById(addressId);
			Integer countryId = address.getIcountry();
			country = foundationService.getCountry(countryId);
		}
		if (country == null) {
			mjson.put("succeed", false);
			mjson.put("error", "country is null");
			return ok(Json.toJson(mjson));
		}
		if (totalPrice==null) {
			mjson.put("succeed", false);
			mjson.put("error", "totalPrice is null");
			return ok(Json.toJson(mjson));
		}

		List<CartItem> items = Lists.newArrayList();
		//ec支付的时候 用订单号取
		if(orderNumber!=null && !"".equals(orderNumber)){
			PaymentContext paymentCtx = orderService.getPaymentContext(orderNumber, lang);
			Order order = paymentCtx.getOrder().getOrder();
			List<OrderItem> orderList = orderService.getOrderDetailByOrder(order,lang);
			items = this.transToNewCartItems(orderList);
			if(items.size()==0){
				mjson.put("succeed", false);
				mjson.put("error", "items is empty");
				return ok(Json.toJson(mjson));
			}
		}else{
			items = icartServices
					.getAllItems(site, lang, currency);
			if (items == null || items.size() == 0) {
				mjson.put("succeed", false);
				mjson.put("error", "items is empty");
				return ok(Json.toJson(mjson));
			}
			//过滤仓库id
			items = Lists.newArrayList(Collections2.filter(items, c -> c.getStorageID()==storageid));
			if(items.size()==0){
				mjson.put("succeed", false);
				mjson.put("error", "items is empty");
				return ok(Json.toJson(mjson));
			}
		}
		
		// ship method
		List<valueobjects.order.ShippingMethod> method = shippingApiServices.getShipMethod(
				country.getCshortname(), storageid, lang, items, currency, totalPrice);
		if(method==null){
			mjson.put("succeed", false);
			mjson.put("error", "data is empty");
			return ok(Json.toJson(mjson));
		}
		return ok(Json.toJson(method));
	}
	
	
	private List<valueobjects.cart.CartItem> transToNewCartItems(List<OrderItem> olist){
		List<valueobjects.cart.CartItem> nlist = Lists.newArrayList();
		for(OrderItem c : olist){
			valueobjects.cart.CartItem ci = new valueobjects.cart.CartItem();
			ci.setSku(c.getSku());
			ci.setClistingid(c.getClistingid());
			ci.setIqty(c.getIqty());
			nlist.add(ci);
		}
		return nlist;
	}
}
