package services.loyalty.coupon;

import java.util.List;

import entity.loyalty.Coupon;
import extensions.loyalty.campaign.rule.memberactive.ConponActionRule;

/**
 * 优惠券服务类 该接口只在服务端用
 * 
 * @author lijun
 *
 */
public interface ICouponMainService extends ICouponService {

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
	 * 更新记录的状态
	 * 
	 * @param id
	 * @param code
	 * @return
	 */
	public int updateStatus(int id, int code);

	/**
	 * 更新操作
	 * 
	 * @param c
	 * @return
	 */
	public int update(Coupon c);

	/**
	 * 通过优惠券code来获取rule,该code只能是表t_coupon_code中的code
	 * 
	 * @param code
	 *            优惠券code
	 * @return
	 */
	public ConponActionRule getActionRule(String code);

	/**
	 * 通过优惠券codeId来获取rule ,该codeId只能是表t_coupon_code中的codeId
	 * 
	 * @param codeId
	 *            优惠券codeId
	 * @return
	 */
	public List<ConponActionRule> getActionRule(List<Integer> codeIds);

	/**
	 * 通过优惠券ruleId来获取rule
	 * 
	 * @param ruleId
	 *            优惠券ruleId
	 * @return
	 */
	public ConponActionRule getActionRule(int ruleId);

	/**
	 * 获取推广码的rule
	 * 
	 * @param code
	 * @return
	 */
	public ConponActionRule getActionRuleForPormoCode(String promoCode);

	/**
	 * 内容转换工具方法
	 * 
	 * @author xiaoch
	 */
	public void convert(List<Coupon> coupons);
}
