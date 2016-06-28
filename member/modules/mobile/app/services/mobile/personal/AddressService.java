package services.mobile.personal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.base.CountryMapper;
import mapper.member.MemberAddressMapper;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import utils.ValidataUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import dto.Country;
import dto.member.MemberAddress;
import dto.mobile.AddressBaseInfo;
import dto.mobile.AddressInfo;

public class AddressService {
	@Inject
	MemberAddressMapper memberAddressMapper;

	@Inject
	CountryMapper countryMapper;

	/**
	 * 设置默认地址
	 * 
	 * @param id
	 * @param email
	 * @param addressType
	 * @return
	 */
	public boolean setDefaultAddress(Integer id, String email,
			Integer addressType) {
		if (addressType != 1 && addressType != 2) {
			return false;
		}
		if (StringUtils.isNotBlank(email) && id != null) {
			MemberAddress adress = memberAddressMapper.getMemberAddressById(id);
			if (adress != null
					&& email.equalsIgnoreCase(adress.getCmemberemail())) {
				try {
					memberAddressMapper.setDefaultShippingaddress(id, email,
							addressType);
					memberAddressMapper.setNotDefaultShippingaddress(id, email,
							addressType);
					return true;
				} catch (Exception e) {
					Logger.error("set default shipping address error", e);
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 获取收货默认地址
	 * 
	 * @param memberEmail
	 * @return
	 */
	public AddressInfo getDefaultShippingAddress(String memberEmail) {
		MemberAddress memberAddress = memberAddressMapper
				.getDefaultShippingAddress(memberEmail);
		if (memberAddress != null) {
			return getAddressInfo(memberAddress);
		}
		return null;
	}

	private AddressInfo getAddressInfo(MemberAddress address) {
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setAddressid(ValidataUtils.validataInt(address
				.getIaddressid()));
		addressInfo.setCity(ValidataUtils.validataStr(address.getCcity()));
		addressInfo
				.setCountry(ValidataUtils.validataInt(address.getIcountry()));
		addressInfo.setEmail(ValidataUtils.validataStr(address
				.getCmemberemail()));
		addressInfo.setFirstname(ValidataUtils.validataStr(address
				.getCfirstname()));
		addressInfo.setIid(ValidataUtils.validataInt(address.getIid()));
		addressInfo.setIsdefault(ValidataUtils.validataBoolean(address
				.getBdefault()));
		addressInfo.setLastname(ValidataUtils.validataStr(address
				.getClastname()));
		addressInfo.setMiddlename(ValidataUtils.validataStr(address
				.getCmiddlename()));
		addressInfo.setProvince(ValidataUtils.validataStr(address
				.getCprovince()));
		addressInfo.setStreet(ValidataUtils.validataStr(address
				.getCstreetaddress()));
		addressInfo.setTelephone(ValidataUtils.validataStr(address
				.getCtelephone()));
		addressInfo.setVatnumber(ValidataUtils.validataStr(address
				.getCvatnumber()));
		return addressInfo;
	}

	/**
	 * 修改地址
	 * 
	 * @param memberAddress
	 * @return
	 */
	public boolean updateMemberAddress(MemberAddress memberAddress) {
		if (!checkAddress(memberAddress)) {
			return false;
		}
		int result = memberAddressMapper.updateByPrimaryKey(memberAddress);
		if (memberAddress.getBdefault() == true) {

			memberAddressMapper.setNotDefaultShippingaddress(
					memberAddress.getIid(), memberAddress.getCmemberemail(),
					memberAddress.getIaddressid());
		}
		return result >= 0 ? true : false;
	}

	/**
	 * 获取用户收货地址
	 * 
	 * @param email
	 * @return
	 */
	public List<AddressBaseInfo> getMemberShippingAddressByEmail(String email,
			Integer type) {
		List<MemberAddress> memberAddress = null;
		if (type == 1) {
			memberAddress = memberAddressMapper
					.getAllShippingAddressByEmail(email);
		} else if (type == 2) {
			memberAddress = memberAddressMapper.getOrderAddressByEmail(email);
		} else {
			return null;
		}

		List<AddressBaseInfo> addressInfos = Lists.transform(memberAddress,
				new Function<MemberAddress, AddressBaseInfo>() {
					@Override
					public AddressBaseInfo apply(MemberAddress address) {
						return getAddressBaseInfo(address);
					}
				});
		return addressInfos;
	}

	/**
	 * 添加地址
	 * 
	 * @param memberAddress
	 * @param addressType
	 *            1是收货地址 ，2是账单地址
	 * @return
	 */
	public boolean addNewAddress(MemberAddress memberAddress) {
		if (!checkAddress(memberAddress)) {
			return false;
		}
		Integer sCount = getShippingAddressCountByEmail(memberAddress
				.getCmemberemail());
		if (sCount == 0) {
			memberAddress.setBdefault(true);
		}
		int result = memberAddressMapper.insertSelective(memberAddress);
		if (memberAddress.getBdefault()) {
			memberAddressMapper.setNotDefaultShippingaddress(
					memberAddress.getIid(), memberAddress.getCmemberemail(),
					memberAddress.getIaddressid());
		}

		return result > 0 ? true : false;
	}

	private boolean checkAddress(MemberAddress memberAddress) {
		if (memberAddress.getIaddressid() != 1
				&& memberAddress.getIaddressid() != 2) {
			return false;
		}
		// 必填项
		if (StringUtils.isEmpty(memberAddress.getCfirstname())
				|| memberAddress.getCfirstname().length() > 50) {
			return false;
		}
		if (StringUtils.isEmpty(memberAddress.getCcity())
				|| memberAddress.getCcity().length() > 20) {
			return false;
		}
		if (StringUtils.isEmpty(memberAddress.getCtelephone())
				|| memberAddress.getCtelephone().length() > 20) {
			return false;
		}
		if (StringUtils.isEmpty(memberAddress.getCstreetaddress())
				|| memberAddress.getCstreetaddress().length() > 100) {
			return false;
		}
		if (StringUtils.isEmpty(memberAddress.getCpostalcode())
				|| memberAddress.getCpostalcode().length() > 40) {
			return false;
		}
		if (StringUtils.isEmpty(memberAddress.getCprovince())
				|| memberAddress.getCprovince().length() > 30) {
			return false;
		}
		if (memberAddress.getIcountry() == null) {
			return false;
		}

		// 非必填项
		if (StringUtils.isNotEmpty(memberAddress.getCmiddlename())
				&& memberAddress.getCmiddlename().length() > 50) {
			return false;
		}
		if (StringUtils.isNotEmpty(memberAddress.getClastname())
				&& memberAddress.getClastname().length() > 50) {
			return false;
		}
		if (StringUtils.isNotEmpty(memberAddress.getCcompany())
				&& memberAddress.getCcompany().length() > 50) {
			return false;
		}
		if (StringUtils.isNotEmpty(memberAddress.getCfax())
				&& memberAddress.getCfax().length() > 20) {
			return false;
		}
		return true;
	}

	/**
	 * 删除地址
	 * 
	 * @param id
	 * @param email
	 * @return
	 */
	public boolean delAddress(int id, String email) {
		if (StringUtils.isNotBlank(email)) {
			MemberAddress adress = memberAddressMapper.getMemberAddressById(id);
			if (adress != null
					&& email.equalsIgnoreCase(adress.getCmemberemail())) {
				int result = memberAddressMapper.deleteByPrimaryKey(id);
				if (result > 0) {
					return true;
				}
			}
		}
		return false;
	}

	public List<MemberAddress> getOrderAddressByEmail(String email) {
		return memberAddressMapper.getOrderAddressByEmail(email);
	}

	public MemberAddress getDefaultOrderAddress(String email) {
		return memberAddressMapper.getDefaultOrderAddress(email);
	}

	public MemberAddress getMemberAddressById(Integer id) {
		return memberAddressMapper.getMemberAddressById(id);
	}

	public Integer getShippingAddressCountByEmail(String email) {
		return memberAddressMapper.getShippingAddressCountByEmail(email);
	}

	public Map<String, Object> getMemberAddressMapById(Integer id) {
		MemberAddress memberAddress = memberAddressMapper
				.getMemberAddressById(id);
		Map<String, Object> address = getAddressMap(memberAddress);
		return address;
	}

	public AddressBaseInfo getDefaultAddress(String email, Integer type) {
		MemberAddress memberAddress = null;
		if (type == 1) {
			memberAddress = memberAddressMapper
					.getDefaultShippingAddress(email);
		} else if (type == 2) {
			memberAddress = memberAddressMapper.getDefaultOrderAddress(email);
		} else {
			return null;
		}
		return getAddressBaseInfo(memberAddress);
	}

	private AddressBaseInfo getAddressBaseInfo(MemberAddress memberAddress) {
		if (memberAddress == null)
			return null;
		AddressBaseInfo addressBaseInfo = new AddressBaseInfo();
		addressBaseInfo.setAid(memberAddress.getIid() == null ? ""
				: memberAddress.getIid().toString());
		Country country = countryMapper.getCountryByCountryId(memberAddress
				.getIcountry());
		String countryName = "";
		if (country != null) {
			countryName = StringUtils.isNotEmpty(country.getCname()) ? (country
					.getCname() + ",") : "";
		}
		String province = StringUtils.isNotEmpty(memberAddress.getCprovince()) ? (memberAddress
				.getCprovince() + ",") : "";
		String city = StringUtils.isNotEmpty(memberAddress.getCcity()) ? (memberAddress
				.getCcity() + ",") : "";
		String street = StringUtils.isNotEmpty(memberAddress
				.getCstreetaddress()) ? memberAddress.getCstreetaddress() : "";
		String address = countryName + province + city + street;
		addressBaseInfo.setAddr(address);
		addressBaseInfo
				.setIsdef((memberAddress.getBdefault() != null && memberAddress
						.getBdefault()) ? 1 : 0);
		String fName = getStr(memberAddress.getCfirstname());
		String mName = getStr(memberAddress.getCmiddlename());
		String lName = getStr(memberAddress.getClastname());
		String name = fName + "." + mName + "." + lName;
		addressBaseInfo.setName(getStr(name));
		addressBaseInfo.setTel(getStr(memberAddress.getCtelephone()));
		addressBaseInfo.setVatno(getStr(memberAddress.getCvatnumber()));
		return addressBaseInfo;
	}

	private String getStr(String str) {
		return StringUtils.isNotEmpty(str) ? str : "";
	}

	private Map<String, Object> getAddressMap(MemberAddress memberAddress) {
		Map<String, Object> address = new HashMap<>();
		address.put("aid", ValidataUtils.validataInt(memberAddress.getIid()));
		address.put("fname",
				ValidataUtils.validataStr(memberAddress.getCfirstname()));
		address.put("mname",
				ValidataUtils.validataStr(memberAddress.getCmiddlename()));
		address.put("lname",
				ValidataUtils.validataStr(memberAddress.getClastname()));
		address.put("company",
				ValidataUtils.validataStr(memberAddress.getCcompany()));
		address.put("city", ValidataUtils.validataStr(memberAddress.getCcity()));
		address.put("street",
				ValidataUtils.validataStr(memberAddress.getCstreetaddress()));
		address.put("country",
				ValidataUtils.validataInt(memberAddress.getIcountry()));
		address.put("provice",
				ValidataUtils.validataStr(memberAddress.getCprovince()));
		address.put("postal",
				ValidataUtils.validataStr(memberAddress.getCpostalcode()));
		address.put("tel",
				ValidataUtils.validataStr(memberAddress.getCtelephone()));
		address.put("fax", ValidataUtils.validataStr(memberAddress.getCfax()));
		address.put("vatno",
				ValidataUtils.validataStr(memberAddress.getCvatnumber()));
		address.put("isdef", memberAddress.getBdefault() ? 1 : 0);
		address.put("type",
				ValidataUtils.validataInt(memberAddress.getIaddressid()));
		return address;
	}

}
