package mapper.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.loyalty.CouponMember;

/**
 * CouponMember 数据库映射类
 * 
 * @author lijun
 *
 */
public interface CouponMemberMapper {

	@Select("<script>" + "SELECT * FROM t_member_coupon t where 1=1 "
			+ "<if test=\"iruleid != null  \">and iruleid=#{iruleid} </if>"
			+ "<if test=\"istatus !=null \">and istatus=#{istatus} </if>"
			+ "<if test=\"icodeid != null  \">and icodeid=#{icodeid} </if>"
			+ "<if test=\"cemail !=null \">and cemail=#{cemail} </if>  ORDER BY Iid"
			+ "</script>")
	List<CouponMember> getCouponMembers(@Param("iruleid") Integer iruleid,
			@Param("istatus") Integer istatus,
			@Param("icodeid") Integer icodeid,
			@Param("cemail") String cemail);
}
