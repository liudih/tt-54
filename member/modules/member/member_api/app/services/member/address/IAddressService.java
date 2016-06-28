package services.member.address;

import java.util.List;

import dto.member.MemberAddress;

public interface IAddressService {

	public List<MemberAddress> getMemberShippingAddressByEmail(String email);

	public List<MemberAddress> getShippingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage);

	public List<MemberAddress> getBillingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage);

	public MemberAddress getMemberAddressById(Integer id);

	public Integer getOrderAddressCountByEmail(String email);

	public boolean deleteAddressById(Integer id);

	public boolean updateMemberShippingAddress(MemberAddress memberAddress);

	public boolean setDefaultShippingaddress(Integer id, String email,
			Integer addressType);

	public MemberAddress getDefaultShippingAddress(String memberEmail);

	public boolean addNewAddress(MemberAddress memberAddress);

	public List<MemberAddress> getOrderAddressByEmail(String email);

	public MemberAddress getDefaultOrderAddress(String email);

	public Integer getShippingAddressCountByEmail(String email);

}