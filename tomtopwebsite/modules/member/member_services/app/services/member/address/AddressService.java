package services.member.address;

import java.util.List;

import javax.inject.Inject;

import mapper.member.MemberAddressMapper;
import play.Logger;
import dto.member.MemberAddress;

public class AddressService implements IAddressService {

	@Inject
	MemberAddressMapper memberAddressMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getMemberShippingAddressByEmail
	 * (java.lang.String)
	 */
	public List<MemberAddress> getMemberShippingAddressByEmail(String email) {
		return memberAddressMapper.getAllShippingAddressByEmail(email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getShippingAddressByPage(java
	 * .lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<MemberAddress> getShippingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage) {
		return memberAddressMapper.getShippingAddressByPage(email, pageIndex,
				recordPerPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getBillingAddressByPage(java.
	 * lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<MemberAddress> getBillingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage) {
		return memberAddressMapper.getBillingAddressByPage(email, pageIndex,
				recordPerPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getMemberAddressById(java.lang
	 * .Integer)
	 */
	public MemberAddress getMemberAddressById(Integer id) {
		return memberAddressMapper.getMemberAddressById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getOrderAddressCountByEmail(java
	 * .lang.String)
	 */
	public Integer getOrderAddressCountByEmail(String email) {
		return memberAddressMapper.getOrderAddressCountByEmail(email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#deleteAddressById(java.lang.Integer
	 * )
	 */
	public boolean deleteAddressById(Integer id) {
		int result = memberAddressMapper.deleteByPrimaryKey(id);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#updateMemberShippingAddress(dto
	 * .member.MemberAddress)
	 */
	public boolean updateMemberShippingAddress(MemberAddress memberAddress) {
		int result = memberAddressMapper
				.updateByPrimaryKeySelective(memberAddress);
		if (null != memberAddress.getBdefault()
				&& true == memberAddress.getBdefault()) {

			memberAddressMapper.setNotDefaultShippingaddress(
					memberAddress.getIid(), memberAddress.getCmemberemail(),
					memberAddress.getIaddressid());
		}
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#setDefaultShippingaddress(java
	 * .lang.Integer, java.lang.String, java.lang.Integer)
	 */
	public boolean setDefaultShippingaddress(Integer id, String email,
			Integer addressType) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getDefaultShippingAddress(java
	 * .lang.String)
	 */
	public MemberAddress getDefaultShippingAddress(String memberEmail) {
		return memberAddressMapper.getDefaultShippingAddress(memberEmail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.address.IAddressService#addNewAddress(dto.member.
	 * MemberAddress)
	 */
	public boolean addNewAddress(MemberAddress memberAddress) {

		int result = memberAddressMapper.insertSelective(memberAddress);
		if (memberAddress.getBdefault() != null
				&& true == memberAddress.getBdefault()
				&& memberAddress.getIaddressid() != null) {
			memberAddressMapper.setNotDefaultShippingaddress(
					memberAddress.getIid(), memberAddress.getCmemberemail(),
					memberAddress.getIaddressid());
		}
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getOrderAddressByEmail(java.lang
	 * .String)
	 */
	public List<MemberAddress> getOrderAddressByEmail(String email) {
		return memberAddressMapper.getOrderAddressByEmail(email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getDefaultOrderAddress(java.lang
	 * .String)
	 */
	public MemberAddress getDefaultOrderAddress(String email) {
		return memberAddressMapper.getDefaultOrderAddress(email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.address.IAddressService#getShippingAddressCountByEmail
	 * (java.lang.String)
	 */
	public Integer getShippingAddressCountByEmail(String email) {
		return memberAddressMapper.getShippingAddressCountByEmail(email);
	}
	
	
	public Integer getBillAddressCountByEmailAndShipAddressId(String email, Integer ishipAddressId) {
		return memberAddressMapper.getBillAddressCountByEmailAndShipAddressId(email, ishipAddressId);
	}
	
	public MemberAddress getBillAddressByEmailAndShipAddressId(String email, Integer ishipAddressId) {
		return memberAddressMapper.getBillAddressByEmailAndShipAddressId(email, ishipAddressId);
	}

}
