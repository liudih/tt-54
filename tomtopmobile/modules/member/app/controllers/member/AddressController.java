package controllers.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.Country;
import dto.member.MemberAddress;
import enums.mobile.member.AddressType;
import forms.mobile.member.AddressForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.member.address.IAddressService;
import valueobjects.base.LoginContext;

/**
 * 用户收货地址
 * @author xiaoch
 *
 */
public class AddressController extends Controller {

	@Inject
	FoundationService fs;

	@Inject
	FoundationService foundationService;

	@Inject
	IAddressService addressService;

	/**
	 * 展示所有billing地址
	 * 
	 * @return
	 */
	public Result billingAddress() {
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			return badRequest();
		}
		String email = loginCtx.getMemberID();
		List<MemberAddress> memberAddresses = addressService
				.getOrderAddressByEmail(email);
		Map<Integer, Country> countryMap = fs.getCountryMap();

		return ok(views.html.member.address.billing_address.render(
				memberAddresses, countryMap));

	}

	/**
	 * 展示所有shipping 地址
	 * 
	 * @return
	 */
	public Result shippingAddress() {
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			return badRequest();
		}
		String email = loginCtx.getMemberID();
		List<MemberAddress> memberAddresses = addressService
				.getMemberShippingAddressByEmail(email);

		Map<Integer, Country> countryMap = fs.getCountryMap();

		return ok(views.html.member.address.shipping_address.render(
				memberAddresses, countryMap));
	}

	public Result delete(Integer id) {
		boolean result = false;
		if (null != id) {
			result = addressService.deleteAddressById(id);
		}
		Map<String, Object> map = Maps.newHashMap();
		if (result) {
			map.put("result", "success");
		} else {
			map.put("result", "error");
		}
		return ok(Json.toJson(map));

	}

	/**
	 * 设置默认收货地址
	 * 
	 * @return
	 */
	public Result setDefaultAddress(Integer id) {
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			return badRequest();
		}
		String email = loginCtx.getMemberID();
		boolean result = false;
		if (null != id) {
			MemberAddress address = addressService.getMemberAddressById(id);
			if (null != address) {
				Integer type = address.getIaddressid();
				result = setDefaultAddressUtil(id, email, type);
			}
		}
		Map<String, Object> map = Maps.newHashMap();
		if (result) {
			map.put("result", "success");
		} else {
			map.put("result", "error");
		}
		return ok(Json.toJson(map));
	}

	/**
	 * 设置默认收货地址工具方法
	 * 
	 * @return
	 */
	private boolean setDefaultAddressUtil(Integer id, String email, Integer type) {
		boolean result = false;
		if (null != id && !StringUtils.isEmpty(email) && null != type) {
			result = addressService.setDefaultShippingaddress(id, email, type);
		}
		return result;
	}

	/**
	 * 添加或更新shipping , billing 地址
	 * 
	 * @return
	 */
	public Result addOrUpdateAddress() {
		LoginContext loginCtx = foundationService.getLoginContext();
		if (!loginCtx.isLogin()) {
			return badRequest();
		}
		Form<AddressForm> form = Form.form(AddressForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest();
		}
		AddressForm addressForm = form.get();
		AddressType type = AddressType.getType(addressForm.getType());
		MemberAddress address = new MemberAddress();
		address.setCstreetaddress(addressForm.getStreet());
		address.setIcountry(addressForm.getCountryId());
		address.setCprovince(addressForm.getState());
		address.setCcity(addressForm.getCity());
		address.setCpostalcode(addressForm.getPostalCode());
		address.setCtelephone(addressForm.getPhoneNumber());
		address.setCfirstname(addressForm.getFirstName());
		address.setClastname(addressForm.getLastName());
		String email = loginCtx.getMemberID();
		address.setCmemberemail(email);
		address.setIid(addressForm.getId());
		// 修改是否为默认地址
		address.setBdefault(null != addressForm.getIsDefault()
				&& addressForm.getIsDefault() == true ? true : false);
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
		// 保存的地址类型
		address.setIaddressid(type.getCode());
		Boolean result = false;
		if (null != address.getIid()) {
			// 保存默认地址
			if (address.getBdefault() == true) {
				setDefaultAddressUtil(address.getIid(), email,
						address.getIaddressid());
			}
			result = addressService.updateMemberShippingAddress(address);

		} else {
			// 创建新地址
			result = addressService.addNewAddress(address);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (result) {
			map.put("result", "success");
		} else {
			map.put("result", "error");
		}
		return ok(Json.toJson(map));
	}
}
