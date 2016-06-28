package services.loyalty;

import java.util.List;
import javax.inject.Inject;
import entity.loyalty.CouponMember;
import mapper.loyalty.CouponMemberMapper;

/**
 * 会员优惠券服务类对象
 * 
 * @author guozy
 *
 */
public class CouponMemberService {

	@Inject
	private CouponMemberMapper couponMemberMapper;

	/**
	 * 获取会员优惠券的集合
	 * 
	 * @return
	 */
	public List<CouponMember> getCouponMembers(CouponMember couponMember) {
		return couponMemberMapper.getCouponMembers(couponMember.getIruleid(),
				couponMember.getIstatus(), 
				couponMember.getIcodeid(),
				couponMember.getCemail());		
	};

}
