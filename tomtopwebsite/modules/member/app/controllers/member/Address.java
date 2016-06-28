package controllers.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.ProvinceService;
import services.member.address.AddressService;
import services.member.login.ILoginService;
import valueobjects.base.Page;
import authenticators.member.MemberLoginAuthenticator;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

import dto.Country;
import dto.member.MemberAddress;
import forms.member.address.ShippingAddressForm;

public class Address extends Controller {

	@Inject
	CountryService countryService;

	@Inject
	ProvinceService provinceEnquiryService;

	@Inject
	ILoginService loginService;

	@Inject
	CurrencyService currencyService;

	@Inject
	AddressService addressService;

	final static int SHIPPING_ADDRESS_TYPE = 1;

	final static int ORDER_ADDRESS_TYPE = 2;

	final static int NOT_ERROR = 0;

	final static int REQUIRED_ERROR = 1;

	final static int SERVER_ERROR = 2;

	final static int recordPerPage = 10;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result shippingAddress(Integer pageIndex) {
		String memberEmail = loginService.getLoginData().getEmail();
		Map<Integer, Country> countryMap = Maps.newHashMap();
		List<MemberAddress> addressList = addressService
				.getShippingAddressByPage(memberEmail, pageIndex, recordPerPage);
		Integer total = addressService
				.getShippingAddressCountByEmail(memberEmail);
		Page<MemberAddress> shippingAddressPage = new Page<MemberAddress>(
				addressList, total, pageIndex, recordPerPage);

		if (null != addressList && addressList.size() > 0) {
			for (MemberAddress memberAddress : addressList) {
				countryMap.put(memberAddress.getIcountry(), countryService
						.getCountryByCountryId(memberAddress.getIcountry()));
			}
		}
		List<Country> allCountries = countryService.getAllCountries();
		String modifyUrl = controllers.member.routes.Address
				.modifyMemberAddress().url();
		String addUrl = controllers.member.routes.Address.addNewMemberAddress()
				.url();
		return ok(views.html.member.address.shipping_address_book_in_member
				.render(recordPerPage, shippingAddressPage, allCountries,
						countryMap, modifyUrl, addUrl));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result billingAddress(Integer pageIndex) {
		String memberEmail = loginService.getLoginData().getEmail();
		Map<Integer, Country> countryMap = Maps.newHashMap();
		List<MemberAddress> addressList = addressService
				.getBillingAddressByPage(memberEmail, pageIndex, recordPerPage);

		Integer total = addressService.getOrderAddressCountByEmail(memberEmail);
		Page<MemberAddress> shippingAddressPage = new Page<MemberAddress>(
				addressList, total, pageIndex, recordPerPage);

		if (null != addressList && addressList.size() > 0) {
			for (MemberAddress memberAddress : addressList) {
				countryMap.put(memberAddress.getIcountry(), countryService
						.getCountryByCountryId(memberAddress.getIcountry()));
			}
		}
		List<Country> allCountries = countryService.getAllCountries();
		String modifyUrl = controllers.member.routes.Address
				.modifyMemberAddress().url();
		String addUrl = controllers.member.routes.Address.addNewMemberAddress()
				.url();
		return ok(views.html.member.address.billing_address_book_in_member
				.render(recordPerPage, shippingAddressPage, allCountries,
						countryMap, modifyUrl, addUrl));
	}

	public Result addNewMemberAddress() {
		String memberEmail = loginService.getLoginData().getEmail();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Form<ShippingAddressForm> form = Form.form(ShippingAddressForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			resultMap.put("errorCode", REQUIRED_ERROR);
			resultMap.put("required", form.errorsAsJson());
			return ok(Json.toJson(resultMap));
		}
		ShippingAddressForm addressForm = form.get();
		MemberAddress address = new MemberAddress();
		BeanUtils.copyProperties(addressForm, address);
		address.setCmemberemail(memberEmail);
	
		boolean defaultAddr = addressForm.getBdefault() == null ? false : true;
		boolean copytoBillingAddress = addressForm.getCopytoBillingAddress() == null ? false
				: true;
		Integer sCount = addressService
				.getShippingAddressCountByEmail(memberEmail);
		if (sCount == 0) {
			address.setBdefault(true);
		} else {
			address.setBdefault(defaultAddr);
		}
		
		String addressType = addressForm.getAddressType();
		
		resultMap.put("actionType", "add");
		
		if (addressType.equals("shipping")) {
			
			resultMap.put("addressType", "shipping");
			
			address.setIaddressid(SHIPPING_ADDRESS_TYPE);
			boolean result = addressService.addNewAddress(address);
			resultMap.put("iid", address.getIid());
			// 当在添加收货地址时勾选了设置为账单地址，或者在没有账单地址的时候，会添加一个与新添加的收货地址一样的账单地址
			Integer orderAddressCount = addressService
					.getOrderAddressCountByEmail(memberEmail);
			
			if (orderAddressCount == 0 || copytoBillingAddress == true) {
				MemberAddress orderAddress = new MemberAddress();
				BeanUtils.copyProperties(addressForm, orderAddress);
				orderAddress.setCmemberemail(memberEmail);
				orderAddress.setIaddressid(ORDER_ADDRESS_TYPE);
				orderAddress.setBdefault(true);
				boolean saveOrderAddressResult = addressService
						.addNewAddress(orderAddress);
				
				if (saveOrderAddressResult == false) {
					resultMap.put("errorCode", SERVER_ERROR);
					return ok(Json.toJson(resultMap));
				}
			}
			if (result) {
				resultMap.put("errorCode", NOT_ERROR);
				return ok(Json.toJson(resultMap));
			}
		} else if (addressType.equals("billing")) {
			resultMap.put("addressType", "billing");
			
			MemberAddress orderAddress = new MemberAddress();
			BeanUtils.copyProperties(addressForm, orderAddress);
			orderAddress.setCmemberemail(memberEmail);
			orderAddress.setIaddressid(ORDER_ADDRESS_TYPE);
			orderAddress.setBdefault(defaultAddr);
			boolean saveOrderAddressResult = addressService
					.addNewAddress(orderAddress);
			resultMap.put("iid", orderAddress.getIid());
			if (saveOrderAddressResult) {
				resultMap.put("errorCode", NOT_ERROR);
				return ok(Json.toJson(resultMap));
			}
		}
		
		resultMap.put("errorCode", SERVER_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result modifyMemberAddress() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Form<ShippingAddressForm> form = Form.form(ShippingAddressForm.class)
				.bindFromRequest();
		resultMap.put("actionType", "modify");
		String email = loginService.getLoginData().getEmail();
//		Map<Integer, Country> countryMap = Maps.newHashMap();
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
			if (oCount == 1) {
				address.setBdefault(true);
			} else {
				address.setBdefault(defaultAddr);
			}
			resultMap.put("addressType", "billing");
		}

		boolean result = addressService.updateMemberShippingAddress(address);
		if (result) {
			resultMap.put("errorCode", NOT_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", SERVER_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result getAllCountry() {
		valueobjects.base.Country countrys = countryService.getAllCountry();
		JsonNode newJsonNode = play.libs.Json.toJson(countrys);
		return ok(newJsonNode);
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result getAllProvinceByCountryId() {
		JsonNode jsonNode = request().body().asJson();
		Integer countryId = jsonNode.get("countryValue").asInt();
		valueobjects.base.Province provinces = provinceEnquiryService
				.getProvincesByCountryId(countryId);
		JsonNode newJsonNode = play.libs.Json.toJson(provinces);
		return ok(newJsonNode);
	}

	public Result deleteMemberAddressBatchByIds() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String email = loginService.getLoginData().getEmail();
		JsonNode node = request().body().asJson();
		if (null != node && node.size() > 0) {
			for (int i = 0; i < node.size(); i++) {
				Integer addressId = node.get(i).asInt();
				MemberAddress address = addressService
						.getMemberAddressById(addressId);
				if (null != address && address.getCmemberemail().equals(email)) {
					boolean result = addressService
							.deleteAddressById(addressId);
					if (result == false) {
						resultMap.put("errorCode", SERVER_ERROR);
						return ok(Json.toJson(resultMap));
					}
				} else {
					resultMap.put("errorCode", REQUIRED_ERROR);
					return ok(Json.toJson(resultMap));
				}
			}
			resultMap.put("errorCode", NOT_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}

	// 删除地址
	@BodyParser.Of(BodyParser.Json.class)
	public Result deleteMemberAddressById() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JsonNode node = request().body().asJson();
		Integer id = node.get("id").asInt();
		String email = loginService.getLoginData().getEmail();
		MemberAddress address = addressService.getMemberAddressById(id);
		if (null != address && address.getCmemberemail().equals(email)) {
			boolean result = addressService.deleteAddressById(id);
			if (result) {
				resultMap.put("errorCode", NOT_ERROR);
				return ok(Json.toJson(resultMap));
			} else {
				resultMap.put("errorCode", SERVER_ERROR);
				return ok(Json.toJson(resultMap));
			}
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}

	// 设置默认收货地址
	@BodyParser.Of(BodyParser.Json.class)
	public Result setDefaultShippingaddress() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String email = loginService.getLoginData().getEmail();
		JsonNode node = request().body().asJson();
		Integer id = node.get("id").asInt();
		MemberAddress address = addressService.getMemberAddressById(id);
		if (null != address && address.getCmemberemail().equals(email)) {
			boolean result = addressService.setDefaultShippingaddress(id,
					email, address.getIaddressid());
			if (result) {
				resultMap.put("errorCode", NOT_ERROR);
				return ok(Json.toJson(resultMap));
			}
		}
		resultMap.put("errorCode", REQUIRED_ERROR);
		return ok(Json.toJson(resultMap));
	}
	
	public Result addOrUpdateBillAddress() {
		Map<String, Object> resultMap = Maps.newHashMap();
		String email = loginService.getLoginData().getEmail();
		JsonNode node = request().body().asJson();
		Integer shipaddressid = node.get("shipaddressid").asInt();

		Integer billaddresscount = addressService
				.getBillAddressCountByEmailAndShipAddressId(email,
						shipaddressid);
		
		if (billaddresscount == 0) {
			MemberAddress orderAddress = addressService
					.getMemberAddressById(shipaddressid);
			orderAddress.setIid(null);
			orderAddress.setIaddressid(ORDER_ADDRESS_TYPE);
			orderAddress.setBdefault(true);
			orderAddress.setIshipAddressId(shipaddressid);
			boolean saveOrderAddressResult = addressService
					.addNewAddress(orderAddress);

			if (saveOrderAddressResult == false) {
				resultMap.put("errorCode", SERVER_ERROR);
				return ok(Json.toJson(resultMap));
			}else{
				resultMap.put("iid", orderAddress.getIid());
			}
		} else {
			MemberAddress shipAddressClone = addressService
					.getMemberAddressById(shipaddressid);

			MemberAddress billAddress = addressService
					.getBillAddressByEmailAndShipAddressId(email, shipaddressid);
			
			shipAddressClone.setIid(billAddress.getIid());
			shipAddressClone.setIaddressid(2);
			shipAddressClone.setIshipAddressId(shipaddressid);
			addressService.updateMemberShippingAddress(shipAddressClone);
			resultMap.put("iid", shipAddressClone.getIid());
		}
		return ok(Json.toJson(resultMap));
	}
}
