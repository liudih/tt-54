package controllers.mobile.personal;

import interceptor.auth.LoginAuth;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.With;
import services.mobile.member.LoginService;
import services.mobile.personal.AddressService;
import utils.ValidataUtils;
import valuesobject.mobile.BaseInfoJson;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseListJson;
import valuesobject.mobile.BaseResultType;
import controllers.mobile.TokenController;
import dto.member.MemberAddress;
import dto.mobile.AddressBaseInfo;
import forms.mobile.AddressForm;

@With(LoginAuth.class)
public class AddressController extends TokenController {

	@Inject
	LoginService loginService;

	@Inject
	AddressService addressService;

	public Result getMemberAddress(Integer type) {
		try {
			if (loginService.isLogin() && type != null) {
				String email = loginService.getLoginMemberEmail();
				List<AddressBaseInfo> list = addressService
						.getMemberShippingAddressByEmail(email, type);
				if (list != null && list.size() > 0) {
					BaseListJson<AddressBaseInfo> result = new BaseListJson<>();
					result.setRe(BaseResultType.SUCCESS);
					result.setMsg(BaseResultType.SUCCESSMSG);
					result.setList(list);
					return ok(Json.toJson(result));
				}
			}
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ERROR);
			result.setMsg(BaseResultType.NODATA);
			return ok(Json.toJson(result));
		} catch (Exception e) {
			Logger.error("AddressController.getMemberAddress Exception", e);
			e.printStackTrace();
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}

	}

	/**
	 * 添加或修改地址 （包括 账单地址 和 收货地址）
	 * 
	 * @return
	 */
	public Result addOrUpdateMemberAddress() {
		BaseJson result = new BaseJson();
		try {
			Form<AddressForm> form = Form.form(AddressForm.class)
					.bindFromRequest();
			AddressForm addressForm = form.get();
			if (form.hasErrors()) {
				result.setRe(BaseResultType.ADDRESS_INPUT_FROM_ERROR_CODE);
				result.setMsg(BaseResultType.ADDRESS_INPUT_FROM_ERROR_MSG);
				return ok(Json.toJson(result));
			}
			result = validateAddressForm(addressForm);
			if (result.getRe() != BaseResultType.SUCCESS) {
				return ok(Json.toJson(result));
			}
			boolean flag = false;
			if (loginService.isLogin()) {
				String email = loginService.getLoginMemberEmail();
				addressForm.setEmail(email);
				if (addressForm.getAid() == null || addressForm.getAid() == 0) {
					// 添加
					MemberAddress memberAddress = new MemberAddress();
					copyByAddressForm(memberAddress, addressForm);
					flag = addressService.addNewAddress(memberAddress);
				} else {
					// 修改
					MemberAddress memberAddress = addressService
							.getMemberAddressById(addressForm.getAid());
					if (null != memberAddress) {
						copyByAddressForm(memberAddress, addressForm);
						flag = addressService
								.updateMemberAddress(memberAddress);
					}
				}
			}
			if (flag) {
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				return ok(Json.toJson(result));
			}
			result.setRe(BaseResultType.FAILURE);
			result.setMsg(BaseResultType.OPERATE_FAIL);
			return ok(Json.toJson(result));
		} catch (Exception e) {
			Logger.error(
					"AddressController.addOrUpdateMemberAddress Exception", e);
			e.printStackTrace();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
	}

	/**
	 * copy Address bean
	 * 
	 * @param addressForm
	 * @return
	 */
	private void copyByAddressForm(MemberAddress memberAddress,
			AddressForm addressForm) {
		memberAddress.setBdefault(addressForm.getIsdef() == 1 ? true : false);
		memberAddress.setCcity(addressForm.getCity());
		memberAddress.setCcompany(addressForm.getCompany());
		memberAddress.setCfax(addressForm.getFax());
		memberAddress.setCfirstname(addressForm.getFname());
		memberAddress.setClastname(addressForm.getLname());
		memberAddress.setCmemberemail(addressForm.getEmail());
		memberAddress.setCmiddlename(addressForm.getMname());
		memberAddress.setCpostalcode(addressForm.getPostal());
		memberAddress.setCprovince(addressForm.getProvice());
		memberAddress.setCstreetaddress(addressForm.getStreet());
		memberAddress.setCtelephone(addressForm.getTel());
		memberAddress.setCvatnumber(addressForm.getVatno());
		memberAddress.setIaddressid(addressForm.getType());
		memberAddress.setIcountry(addressForm.getCountry());
	}

	/**
	 * 删除地址
	 * 
	 * @param id
	 * @return
	 */
	public Result delAddress(Integer aid) {
		BaseJson result = new BaseJson();
		try {
			if (loginService.isLogin()) {
				String email = loginService.getLoginMemberEmail();
				if (StringUtils.isNotBlank(email) && aid != null) {
					boolean flag = addressService.delAddress(aid, email);
					if (flag) {
						result.setRe(BaseResultType.SUCCESS);
						result.setMsg(BaseResultType.SUCCESSMSG);
						return ok(Json.toJson(result));
					}
				}
			}
			result.setRe(BaseResultType.FAILURE);
			result.setMsg(BaseResultType.OPERATE_FAIL);
			return ok(Json.toJson(result));
		} catch (Exception e) {
			Logger.error("AddressController.delAddress Exception", e);
			e.printStackTrace();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
	}

	/**
	 * 设置默认地址
	 * 
	 * @return
	 */
	public Result setDefaultShipAddress(Integer aid, Integer type) {
		BaseJson result = new BaseJson();
		try {
			if (aid != null && type != null && loginService.isLogin()) {
				String email = loginService.getLoginMemberEmail();
				boolean flag = addressService.setDefaultAddress(aid, email,
						type);
				if (flag) {
					result.setRe(BaseResultType.SUCCESS);
					result.setMsg(BaseResultType.SUCCESSMSG);
					return ok(Json.toJson(result));
				}
			}
			result.setRe(BaseResultType.FAILURE);
			result.setMsg(BaseResultType.OPERATE_FAIL);
			return ok(Json.toJson(result));
		} catch (Exception e) {
			Logger.error("AddressController.setDefaultShipAddress Exception", e);
			e.printStackTrace();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
	}

	/**
	 * 查询收货地址详情
	 * 
	 * @return
	 */
	public Result findAddress(Integer aid) {
		try {
			if (loginService.isLogin() && aid != null) {
				Map<String, Object> memberAddress = addressService
						.getMemberAddressMapById(aid);
				BaseInfoJson<Map<String, Object>> result = new BaseInfoJson<>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setInfo(memberAddress);
				return ok(Json.toJson(result));
			}
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ERROR);
			result.setMsg(BaseResultType.NODATA);
			return ok(Json.toJson(result));
		} catch (Exception e) {
			Logger.error("AddressController.findAddress Exception", e);
			e.printStackTrace();
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
	}

	/**
	 * 查询默认地址
	 * 
	 * @return
	 */
	public Result findDefaultAddress(Integer type) {
		try {
			if (loginService.isLogin() && type != null) {
				String email = loginService.getLoginMemberEmail();
				AddressBaseInfo addressBaseInfo = addressService
						.getDefaultAddress(email, type);
				BaseInfoJson<AddressBaseInfo> result = new BaseInfoJson<>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setInfo(addressBaseInfo);
				return ok(Json.toJson(result));
			}
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ERROR);
			result.setMsg(BaseResultType.NODATA);
			return ok(Json.toJson(result));
		} catch (Exception e) {
			Logger.error("AddressController.findDefaultAddress Exception", e);
			e.printStackTrace();
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
	}

	private BaseJson validateAddressForm(AddressForm addressForm) {
		BaseJson result = new BaseJson();
		if (ValidataUtils.validateNull(addressForm.getFname()) == false) {
			result.setRe(BaseResultType.ADDRESS_FIRST_NAME_EMPTY_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_FIRST_NAME_EMPTY_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateLength(addressForm.getFname(), 50) == false) {
			result.setRe(BaseResultType.ADDRESS_FIRST_NAME_LENGTH_OVER_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_FIRST_NAME_LENGTH_OVER_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateLength(addressForm.getLname(), 50) == false) {
			result.setRe(BaseResultType.ADDRESS_LAST_NAME_LENGTH_OVER_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_LAST_NAME_LENGTH_OVER_ERROR_MSG);
			return result;
		}
		if (addressForm.getCountry() == 0) {
			result.setRe(BaseResultType.ADDRESS_COUNTRY_IS_EMPTY_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_COUNTRY_IS_EMPTY_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateNull(addressForm.getStreet()) == false) {
			result.setRe(BaseResultType.ADDRESS_STREET_IS_EMPTY_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_STREET_IS_EMPTY_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateLength(addressForm.getStreet(), 150) == false) {
			result.setRe(BaseResultType.ADDRESS_STREET_LENGTH_OVER_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_STREET_LENGTH_OVER_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateNull(addressForm.getProvice()) == false) {
			result.setRe(BaseResultType.ADDRESS_PROVINCE_IS_EMPTY_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_PROVINCE_IS_EMPTY_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateLength(addressForm.getProvice(), 150) == false) {
			result.setRe(BaseResultType.ADDRESS_PROVINCE_LENGTH_OVER_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_PROVINCE_LENGTH_OVER_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateNull(addressForm.getCity()) == false) {
			result.setRe(BaseResultType.ADDRESS_CITY_IS_EMPTY_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_CITY_IS_EMPTY_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateLength(addressForm.getCity(), 60) == false) {
			result.setRe(BaseResultType.ADDRESS_CITY_LENGTH_OVER_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_CITY_LENGTH_OVER_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateNull(addressForm.getPostal()) == false) {
			result.setRe(BaseResultType.ADDRESS_POSTAL_IS_EMPTY_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_POSTAL_IS_EMPTY_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateLength(addressForm.getPostal(), 60) == false) {
			result.setRe(BaseResultType.ADDRESS_POSTAL_LENGTH_OVER_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_POSTAL_LENGTH_OVER_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateNull(addressForm.getTel()) == false) {
			result.setRe(BaseResultType.ADDRESS_PHONE_IS_EMPTY_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_PHONE_IS_EMPTY_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateLength(addressForm.getTel(), 30) == false) {
			result.setRe(BaseResultType.ADDRESS_PHONE_LENGTH_OVER_ERROR_CODE);
			result.setMsg(BaseResultType.ADDRESS_PHONE_LENGTH_OVER_ERROR_MSG);
			return result;
		}
		result.setRe(BaseResultType.SUCCESS);
		return result;
	}
}
