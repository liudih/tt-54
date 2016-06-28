package mapper.loyalty;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import entity.loyalty.Coupon;
import entity.loyalty.CouponMember;
import entity.loyalty.CouponValue;

/**
 * Coupon 数据库映射类
 * 
 * @author lijun
 *
 */
public interface CouponMapper {

	/**
	 * 分页查询
	 * 
	 * @param paras
	 * @return
	 */
	public List<CouponValue> selectForPage(Map paras);

	/**
	 * 查询数据总条数
	 * 
	 * @param paras
	 * @return
	 */
	public int getTotal(Map paras);

	/**
	 * 新增操作
	 * 
	 * @param c
	 * @return
	 */
	public int add(Coupon c);

	/**
	 * 删除操作
	 * 
	 * @param id
	 * @return
	 */
	public int delete(Map paras);

	/**
	 * 跟新操作
	 * 
	 * @param paras
	 * @return
	 */
	public int update(Coupon c);

	/**
	 * 查询操作
	 * 
	 * @param paras
	 * @return
	 */
	public List<Coupon> select(Map paras);

	/**
	 * 获取我的优惠券信息
	 * 
	 * @param paras
	 * @return
	 */
	public List<Coupon> selectMyCouponForPage(Map paras);

	/**
	 * 判断是否存在某记录
	 * 
	 * @param paras
	 * @return
	 */
	public int isExisted(Map paras);

	/**
	 * 获取我的优惠券总数
	 * 
	 * @param paras
	 * @return
	 */
	public int getTotalMyCoupon(Map paras);

	/**
	 * 判断我的优惠券是否可用
	 * 
	 * @param userEmail
	 * @param code
	 * @param siteId
	 * @return
	 */
	public int myCouponUseable(Map paras);

	/**
	 * 根据赠送的规则查询用户邮箱
	 */
	@Select("select distinct cemail from t_member_coupon where itype=#{0}")
	List<String> getMemberEmailByType(int type);
	
	/**
	 * 判断某人是否拥有这张coupon,不管之前是否已经使用过
	 * @param paras
	 * @return
	 */
	public int isOwnCoupon(Map paras);

}
