package services.loyalty;

import java.util.List;

import valueobjects.base.Page;
import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.loyalty.UsedPoint;
import context.WebContext;
import entity.loyalty.IntegralBehavior;
import entity.loyalty.IntegralUseRule;
import entity.loyalty.MemberIntegralHistory;
import forms.member.memberSearch.MemberSearchForm;

public interface IPointsService {

	public static final String LOCK_TYPE = "lock";
	public static final Integer PER_DATE = 86400;
	public final static String SUBSCRIBER = "subscriber";
	public final static String VIDEO = "video";
	public final static String COMMENT = "comment";
	public final static String PHOTO = "photo";
	public final static String REGISTER = "register";

	/**
	 * 给积分到会员账上
	 * 
	 * @param email
	 * @param siteID
	 * @param points
	 * @param type
	 * @param remark
	 * @return
	 */
	public boolean grantPoints(String email, int siteID, double points,
			String type, String remark, int status, String source);

	/**
	 * 锁定用户积分
	 *
	 * @param email
	 * @param siteID
	 * @param points
	 *            传入的points为你需要锁定的分数，需要传入正数，存入时会自动转为负数
	 * @param remark
	 *            备注
	 * @return 如果锁定失败，则返回null，成功则返回id
	 * @author luojiaheng
	 */
	public Integer lockPoints(String email, int siteID, int points,
			String remark, WebContext context);

	/**
	 * 解锁并设定为某个类型的积分
	 * 
	 * @param status
	 * @param pointsId
	 *            <code>lockPoints</code>的返回结果
	 * @return
	 * @see #lockPoints(String, int, int, String)
	 */
	public boolean unlockPointsWithType(String status, Integer pointsId);

	public boolean updateRemarkById(String remark, Integer id);

	/**
	 * 根据用户email获取用户当前积分
	 *
	 * @param email
	 * @param siteID
	 * @return
	 * @author luojiaheng
	 */
	public int getMemberPoints(String email, int siteID);

	/**
	 * 根据用户email获取用户可用积分
	 *
	 * @param email
	 * @param siteID
	 * @return
	 * @author luojiaheng
	 */
	public int getUsefulPoints(String email, int siteID);

	/**
	 * 根据用户email获取用户被锁定积分
	 *
	 * @param email
	 * @param siteID
	 * @return
	 * @author luojiaheng
	 */
	public int getLockedPoints(String email, int siteID);

	// -------------------------------------------------------------------
	public Integer getById(Integer id);

	/**
	 * 根据name获得积分行为
	 *
	 * @param cname
	 * @return
	 * @author luojiaheng
	 */
	public IntegralBehavior getIntegralBehavior(Integer iwebsite, String cname);

	/**
	 * insert IntegralHistory，添加积分记录
	 *
	 * @param memberIntegralHistory
	 * @return
	 * @author luojiaheng
	 */
	public boolean insertIntegralHistory(
			MemberIntegralHistory memberIntegralHistory);

	public boolean validPoints(String email, int points, WebContext context);

	/**
	 * 默认以当前站点获取规则，以email获取用户组
	 *
	 * @param email
	 * @return
	 * @author luojiaheng
	 */
	public IntegralUseRule getIntegralUseRule(String email, WebContext context);

	public IntegralUseRule getIntegralUseRule(Integer groupId);

	public Double getMoney(Integer points, IntegralUseRule rule);

	/**
	 * 以email和当前站点获取规则，在将points转为当前货币相应金额
	 *
	 * @param points
	 * @param email
	 *            需确保传入参数有效，该方法中不做处理
	 * @return 保留两位有效数字
	 * @author luojiaheng
	 */
	public Double getMoney(Integer points, String email, WebContext context);

	public String Save(com.website.dto.member.MemberPoint[] points);

	public List<MemberIntegralHistory> getMemberIntegralHistoryList(
			MemberSearchForm form);

	public Integer getMemberIntegralHistoryCount(MemberSearchForm form);

	public boolean updateMemberIntegralHistory(
			MemberIntegralHistory memberIntegralHistory);

	/**
	 * 获取可用积分记录
	 */
	public Page<MemberIntegralHistory> getIntegralHistoriesByEmail(
			Integer websiteId, String email, Integer status, Integer pageIndex,
			Integer pageSize, Integer dateType, Integer totalCount);

	/**
	 * 获取已用积分记录
	 */
	public Page<MemberIntegralHistory> getUsedPointsByEmail(Integer websiteId,
			String email, Integer pageIndex, Integer pageSize,
			Integer dateType, Integer totalCount);

	/**
	 * 获取订单使用积分记录
	 */
	public List<UsedPoint> getOrderPointsByEmail(Integer websiteId,
			String email, Integer status);

	public Integer getTotalIntegralCountByEmail(Integer siteId, String email);

	public Integer getTotalUsedCountByEmail(Integer siteId, String email,
			String doType);

	public Integer getTotalUsePointByEmail(Integer siteId, String email);

	public void saveMemberIntegralHistory(String email, Integer siteId,
			Integer integral, String type, String source);

	/**
	 * 
	 * 连续签到天数奖励积分规则
	 */
	public Integer getIntegralBySignDayCheck(Integer day);

	/**
	 * 上传视频 40； 写产品评论 10； 分享图片/视频 20;
	 */
	public Integer integralForType(String type);

	/**
	 * 验证邮件订阅只能送一次积分
	 */
	public boolean checkSubsciberPoint(Integer siteId, String email, String type);

	/**
	 * 获取用户所有合法积分(使用过 + 未使用过)
	 * 
	 * @author lijun
	 * @param websiteId
	 * @param email
	 * @param pageIndex
	 *            获取第几页数据
	 * @param pageSize
	 *            每页数据大小
	 * @return
	 */
	public Page<MemberIntegralHistory> getValidPointsByEmail(Integer websiteId,
			String email, Integer pageIndex, Integer pageSize);

	/**
	 * 使用积分到购物车
	 * 
	 * @author lijun
	 * @param costPoints
	 *            要使用的积分
	 * @param cartId
	 *            购物车id
	 * @return true : 积分成功绑定到购物车
	 */
	public boolean usePoint(int costPoints, String cartId);

	/**
	 * 使用积分到购物车
	 * 
	 * @author lijun
	 * @param costPoints
	 *            要使用的积分
	 * @param cartId
	 *            购物车id
	 * @param webCtx
	 * @return true : 积分成功绑定到购物车
	 */
	public boolean usePoint(int costPoints, String cartId, WebContext webCtx);

	/**
	 * 取消购物车已经使用的积分
	 * 
	 * @author lijun
	 * @param cartId
	 *            购物车id
	 * @return true 取消成功
	 */
	public boolean cancelUsedPoint(String cartId);

	/**
	 * 取消购物车已经使用的积分
	 * 
	 * @author lijun
	 * @param cartId
	 *            购物车id
	 * @param webCtx
	 *            WebContext
	 * @return true 取消成功
	 */
	public boolean cancelUsedPoint(String cartId, WebContext webCtx);
	
	/**
	 * 应用积分操作,给远程调用
	 * @param email
	 * @param cartItems
	 * @param costPoints
	 * @param webContext
	 * @return
	 */
	public LoyaltyPrefer applyPoints(String email,
			List<CartItem> cartItems, Integer costPoints, WebContext webContext);
	
	/**
	 * 订单付款成功后,将积分设置为已用
	 * @param email
	 * @param loyaltyPrefer
	 * @param webCtx
	 * @return
	 */
	public boolean saveOrderPrefer(String email,
			LoyaltyPrefer loyaltyPrefer, WebContext webCtx);
	
	/**
	 * 验证订单完成后是否已送积分
	 * @param siteId
	 * @param cmemberemail
	 * @param doType
	 * @param orderNumber
	 * @return
	 */
	public boolean validateIsSendPoint(Integer siteId, String cmemberemail,
			String doType, String orderNumber);
}