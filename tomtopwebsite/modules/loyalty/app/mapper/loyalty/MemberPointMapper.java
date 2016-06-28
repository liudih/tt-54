package mapper.loyalty;

import org.apache.ibatis.annotations.Select;

import entity.loyalty.MemberSign;

public interface MemberPointMapper {
	@Select("select * from t_member_sign where cemail=#{0} and iwebsiteid = #{1} limit 1")
	MemberSign getMemberSign(String cmemberemail, Integer siteId);
}
