package mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import dto.member.MemberAddress;

public interface MemberAddressMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(MemberAddress record);

	int insertSelective(MemberAddress record);

	MemberAddress selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(MemberAddress record);

	int updateByPrimaryKey(MemberAddress record);

	@Select("select * from t_member_address where iid = #{id}  limit 1")
	MemberAddress getMemberAddressById(Integer id);

	@Select("select * from t_member_address where cmemberemail = #{0} and  iaddressid = 1 order by bdefault desc,iid desc")
	List<MemberAddress> getAllShippingAddressByEmail(String email);

	@Select("select * from t_member_address where cmemberemail = #{0} and  iaddressid = 1 "
			+ "offset (#{1}-1)*#{2} limit #{2}")
	List<MemberAddress> getShippingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage);

	@Select("select * from t_member_address where cmemberemail = #{0} and iaddressid = 2 "
			+ "offset (#{1}-1)*#{2} limit #{2}")
	List<MemberAddress> getBillingAddressByPage(String email,
			Integer pageIndex, Integer recordPerPage);

	@Select("select count(iid) from t_member_address where cmemberemail = #{0} and iaddressid = 2")
	int getOrderAddressCountByEmail(String email);

	@Select("update t_member_address set bdefault = true where iid = #{0} and cmemberemail = #{1} and iaddressid =#{2}")
	void setDefaultShippingaddress(Integer id, String email, Integer addressType);

	@Select("update t_member_address set bdefault = false where iid <> #{0} and cmemberemail = #{1} and iaddressid = #{2}")
	void setNotDefaultShippingaddress(Integer id, String email,
			Integer addressType);

	@Select("select * from t_member_address where cmemberemail = #{memberemail} and bdefault = true and iaddressid =1 limit 1")
	MemberAddress getDefaultShippingAddress(String memberEmail);

	int insertBatch(List<MemberAddress> list);

	@Delete({ "<script> delete from t_member_address where cmemberemail  in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>" })
	int deleteByEmail(List<String> email);

	@Select("select * from t_member_address where cmemberemail = #{0} and  iaddressid = 2")
	List<MemberAddress> getOrderAddressByEmail(String email);

	@Select("select * from t_member_address where cmemberemail = #{0} and  iaddressid = 2 and bdefault = true limit 1")
	MemberAddress getDefaultOrderAddress(String email);

	@Select("select count(iid) from t_member_address where cmemberemail = #{0} and  iaddressid = 1 ")
	Integer getShippingAddressCountByEmail(String email);

	@Select("select count(iid) from t_member_address where cmemberemail = #{0} and  iaddressid = 2 and ishipaddressid=#{1} ")
	Integer getBillAddressCountByEmailAndShipAddressId(String email, Integer ishipAddressId );

	@Select("select * from t_member_address where cmemberemail = #{0} and  iaddressid = 2 and ishipaddressid=#{1} limit 1")
	MemberAddress getBillAddressByEmailAndShipAddressId(String email,
			Integer ishipAddressId);

}