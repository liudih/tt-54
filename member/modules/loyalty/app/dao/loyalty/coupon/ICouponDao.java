package dao.loyalty.coupon;

import java.util.List;
import java.util.Map;

import entity.loyalty.Coupon;
import entity.loyalty.CouponMember;

/**
 * Coupon 数据库映射类
 * 
 * @author lijun
 *
 */
public interface ICouponDao {

	/**
	 * 分页查询
	 * 
	 * @param paras
	 * @return
	 */
	public List<Coupon> selectForPage(Map paras);

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
	 * @return
	 */
	public int add(Coupon c);

	/**
	 * 删除操作
	 * 
	 * @param id
	 * @return
	 */
	public int delete(int id);

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
	public boolean myCouponUseable(Map paras);


}
