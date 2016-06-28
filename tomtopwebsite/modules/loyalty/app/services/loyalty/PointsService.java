package services.loyalty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.loyalty.IntegralBehaviorMapper;
import mapper.loyalty.IntegralUseRuleMapper;
import mapper.loyalty.MemberIntegralHistoryMapper;
import mapper.loyalty.MemberPointMapper;
import mapper.loyalty.OrderPointsMapper;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;
import play.mvc.Http.Context;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import services.cart.ICartLifecycleService;
import services.cart.ICartService;
import services.member.IMemberEnquiryService;
import services.member.login.LoginService;
import services.order.ICheckoutService;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.loyalty.UsedPoint;
import valueobjects.order_api.OrderConstants;
import valueobjects.order_api.cart.CartOwner;
import valueobjects.order_api.cart.ExtraLine;
import valueobjects.base.LoginContext;
import valueobjects.base.Page;
import valueobjects.cart.CartItem;

import com.google.api.client.util.Maps;
import com.google.common.collect.Lists;

import context.ContextUtils;
import context.WebContext;
import entity.loyalty.IntegralBehavior;
import entity.loyalty.IntegralUseRule;
import dto.member.MemberBase;
import dto.order.Order;
import entity.loyalty.MemberSignRule;
import entity.loyalty.MemberIntegralHistory;
import entity.loyalty.OrderPoints;
import extensions.loyalty.orderxtras.PointsExtrasProvider;
import facades.cart.Cart;
import forms.member.memberSearch.MemberSearchForm;

/**
 *
 * @ClassName: PointService
 * @Description: 积分有关的操作
 * @author luojiaheng
 * @date 2015年1月14日 下午4:52:29
 *
 */
public class PointsService implements IPointsService {

	@Inject
	IntegralBehaviorMapper behaviorMapper;

	@Inject
	MemberIntegralHistoryMapper historyMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	CurrencyService currencyService;

	@Inject
	IntegralUseRuleMapper ruleMapper;

	@Inject
	LoginService loginService;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	MemberPointMapper pointMapper;

	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	PointsExtrasProvider pointsExtrasProvider;

	@Inject
	ICartService cartService;

	@Inject
	OrderPointsMapper orderPointsMapper;

	@Inject
	ICheckoutService checkoutService;

	@Inject
	LoyaltyOrderCall loyaltyOrderCall;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#grantPoints(java.lang.String, int,
	 * double, java.lang.String, java.lang.String, int, java.lang.String)
	 */
	public boolean grantPoints(String email, int siteID, double points,
			String type, String remark, int status, String source) {
		
		if (LOCK_TYPE.equals(type)) {
			throw new RuntimeException("Type " + type + " is not allowed");
		}
		
		if("order-payment".equalsIgnoreCase(type)){
			// 在此先添加一个判断，如果此订单已经送了积分，则不再送积分，后续再定位为什么会送两次积分 xcf
			boolean flag = this.validateIsSendPoint(siteID, email, type, remark);
			if (flag) {
				return true;
			}
		}else if("sign-in".equalsIgnoreCase((type))){
			// 在此先添加一个判断，如果此订单已经送了积分，则不再送积分，后续再定位为什么会送两次积分 xcf
			boolean flag = this.validateSignInSendPoint(siteID, email, type, remark);
			if (flag) {
				return true;
			}
		}
		MemberIntegralHistory mhistory = new MemberIntegralHistory();
		mhistory.setCemail(email);
		mhistory.setCremark(remark);
		mhistory.setCdotype(type);
		mhistory.setIintegral(points);
		mhistory.setIwebsiteid(siteID);
		mhistory.setIstatus(status);
		mhistory.setCsource(source);
		
		return insertIntegralHistory(mhistory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#lockPoints(java.lang.String, int,
	 * int, java.lang.String, context.WebContext)
	 */
	public Integer lockPoints(String email, int siteID, int points,
			String remark, WebContext context) {
		if (validPoints(email, points, context)) {
			MemberIntegralHistory history = new MemberIntegralHistory();
			history.setIwebsiteid(siteID);
			history.setCemail(email);
			history.setCdotype(LOCK_TYPE);
			history.setIintegral(Double.valueOf(points) * -1);
			history.setCremark(remark);
			history.setCsource("cost");
			if (insertIntegralHistory(history)) {
				return history.getIid();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#unlockPointsWithType(java.lang.String,
	 * java.lang.Integer)
	 */
	public boolean unlockPointsWithType(String status, Integer pointsId) {
		int i = historyMapper.updateStatus(status, pointsId);
		if (i == 1) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#updateRemarkById(java.lang.String,
	 * java.lang.Integer)
	 */
	public boolean updateRemarkById(String remark, Integer id) {
		int i = historyMapper.updateRemark(remark, id);
		if (i == 1) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#getMemberPoints(java.lang.String,
	 * int)
	 */
	public int getMemberPoints(String email, int siteID) {
		Integer points = getUsefulPoints(email, siteID)
				- getLockedPoints(email, siteID);
		return points != null ? points : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#getUsefulPoints(java.lang.String,
	 * int)
	 */
	public int getUsefulPoints(String email, int siteID) {
		Integer points = historyMapper.getSumByEmail(email, siteID);
		return points != null ? points : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#getLockedPoints(java.lang.String,
	 * int)
	 */
	public int getLockedPoints(String email, int siteID) {
		Integer points = historyMapper.getByEmailAndBehaviorName(email,
				LOCK_TYPE, siteID);
		return points != null ? points : 0;
	}

	// -------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#getById(java.lang.Integer)
	 */
	public Integer getById(Integer id) {
		return historyMapper.getById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getIntegralBehavior(java.lang.Integer,
	 * java.lang.String)
	 */
	public IntegralBehavior getIntegralBehavior(Integer iwebsite, String cname) {
		return behaviorMapper.getIntegralBehavior(iwebsite, cname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#insertIntegralHistory(entity.loyalty.
	 * MemberIntegralHistory)
	 */
	public boolean insertIntegralHistory(
			MemberIntegralHistory memberIntegralHistory) {
		
		int i = historyMapper.insert(memberIntegralHistory);
		return i > 0;
	}

	public boolean validateIsSendPoint(Integer siteId, String cmemberemail,
			String doType, String orderNumber) {
		int i = historyMapper.validateIsSendPoint(siteId, cmemberemail, doType,
				orderNumber);
		return i > 0;
	}

	public boolean validateSignInSendPoint(Integer siteId, String cmemberemail,
			String doType, String remark) {
		int i = historyMapper.validateSignInSendPoint(siteId, cmemberemail,
				doType, remark);
		return i > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#validPoints(java.lang.String, int,
	 * context.WebContext)
	 */
	public boolean validPoints(String email, int points, WebContext context) {
		IntegralUseRule rule = getIntegralUseRule(email, context);
		if (points <= getUsefulPoints(email, foundationService.getSiteID())
				&& points <= rule.getImaxuse()) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#getIntegralUseRule(java.lang.String,
	 * context.WebContext)
	 */
	public IntegralUseRule getIntegralUseRule(String email, WebContext context) {
		MemberBase member = memberEnquiryService.getMemberByMemberEmail(email,
				context);
		return getIntegralUseRule(member.getIgroupid());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getIntegralUseRule(java.lang.Integer)
	 */
	public IntegralUseRule getIntegralUseRule(Integer groupId) {
		IntegralUseRule rule = ruleMapper.getBySiteIdAndGroupId(
				foundationService.getSiteID(), groupId);
		if (null == rule) {
			rule = new IntegralUseRule();
			rule.setCcurrency("USD");
			rule.setFmoney(0.0);
			rule.setImaxuse(0);
			rule.setIwebsiteid(foundationService.getSiteID());
			rule.setImembergroupid(groupId);
			rule.setIintegral(0);
		}
		return rule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#getMoney(java.lang.Integer,
	 * entity.loyalty.IntegralUseRule)
	 */
	public Double getMoney(Integer points, IntegralUseRule rule) {
		if (null == rule) {
			return null;
		}
		double money = points * (rule.getFmoney() / rule.getIintegral()) * -1;
		money = currencyService.exchange(money, rule.getCcurrency(),
				foundationService.getCurrency());
		return new DoubleCalculateUtils(money).doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#getMoney(java.lang.Integer,
	 * java.lang.String, context.WebContext)
	 */
	public Double getMoney(Integer points, String email, WebContext context) {
		IntegralUseRule rule = getIntegralUseRule(email, context);
		return getMoney(points, rule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#Save(com.website.dto.member.MemberPoint
	 * [])
	 */
	public String Save(com.website.dto.member.MemberPoint[] points) {
		if (points == null || points.length == 0) {
			return "";
		}
		try {
			List<Integer> saveids = Lists.transform(Arrays.asList(points),
					obj -> obj.getId());
			List<Integer> existsIds = historyMapper.getIds(saveids);
			List<MemberIntegralHistory> list = Lists
					.transform(
							Arrays.asList(points),
							obj -> {
								if (existsIds.contains(obj.getId())) {
									return null;
								}
								MemberIntegralHistory point = new MemberIntegralHistory();
								point.setIid(obj.getId());
								point.setCdotype(obj.getDotype());
								point.setCemail(obj.getEmail());
								point.setCremark(obj.getRemark());
								point.setIintegral(obj.getIntegral());
								point.setIwebsiteid(obj.getWebsiteid());
								return point;
							});
			historyMapper.batchInsert(list);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getMemberIntegralHistoryList(forms.member
	 * .memberSearch.MemberSearchForm)
	 */
	public List<MemberIntegralHistory> getMemberIntegralHistoryList(
			MemberSearchForm form) {
		return historyMapper.getMemberIntegralHistoryList(form.getEmail(),
				form.getSiteId(), form.getCdotype(), form.getCremark(),
				form.getStatus(), form.getPageSize(), form.getPageNum());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getMemberIntegralHistoryCount(forms.member
	 * .memberSearch.MemberSearchForm)
	 */
	public Integer getMemberIntegralHistoryCount(MemberSearchForm form) {
		return historyMapper.getMemberIntegralHistoryCount(form.getEmail(),
				form.getSiteId(), form.getCdotype(), form.getStatus(),
				form.getPageSize(), form.getPageNum());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#updateMemberIntegralHistory(entity.loyalty
	 * .MemberIntegralHistory)
	 */
	public boolean updateMemberIntegralHistory(
			MemberIntegralHistory memberIntegralHistory) {
		return historyMapper.updateMemberIntegralHistory(
				memberIntegralHistory.getIid(),
				memberIntegralHistory.getCdotype(),
				memberIntegralHistory.getIintegral(),
				memberIntegralHistory.getCremark(),
				memberIntegralHistory.getIstatus()) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getIntegralHistoriesByEmail(java.lang
	 * .Integer, java.lang.String, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public Page<MemberIntegralHistory> getIntegralHistoriesByEmail(
			Integer websiteId, String email, Integer status, Integer pageIndex,
			Integer pageSize, Integer dateType, Integer totalCount) {
		// List<MemberIntegralHistory> memberIntegralHistories =
		// historyMapper.getIntegralHistoriesByEmail(websiteId, email, status,
		// page, limit);

		List<MemberIntegralHistory> memberIntegralHistories = Lists
				.newArrayList();
		Date start = null;
		Date end = null;
		if (dateType != 0) {
			end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (dateType * (-1)));
			start = calendar.getTime();
		}
		Logger.debug("pageIndex: " + pageIndex + "  pageSize: " + pageSize);
		String doType = "cost";
		memberIntegralHistories = historyMapper.getIntegralHistoriesByEmail(
				websiteId, email, status, pageSize, pageIndex, doType);
		;
		return new Page<MemberIntegralHistory>(memberIntegralHistories,
				totalCount, pageIndex, pageSize);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getUsedPointsByEmail(java.lang.Integer,
	 * java.lang.String, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, java.lang.Integer)
	 */
	public Page<MemberIntegralHistory> getUsedPointsByEmail(Integer websiteId,
			String email, Integer pageIndex, Integer pageSize,
			Integer dateType, Integer totalCount) {
		List<MemberIntegralHistory> usedPoints = Lists.newArrayList();
		Date start = null;
		Date end = null;
		if (dateType != 0) {
			end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (dateType * (-1)));
			start = calendar.getTime();
		}
		Logger.debug("pageIndex: " + pageIndex + "  pageSize: " + pageSize);
		String doType = LOCK_TYPE;
		usedPoints = historyMapper.getUsedPointHistory(websiteId, email,
				doType, pageSize, pageIndex);
		return new Page<MemberIntegralHistory>(usedPoints, totalCount,
				pageIndex, pageSize);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getOrderPointsByEmail(java.lang.Integer,
	 * java.lang.String, java.lang.Integer)
	 */
	public List<UsedPoint> getOrderPointsByEmail(Integer websiteId,
			String email, Integer status) {
		List<UsedPoint> orderPoints = historyMapper.getOrderPointsByEmail(
				websiteId, email, status);
		Logger.debug("++++++siez: " + orderPoints.size());
		if (!CollectionUtils.isEmpty(orderPoints)) {
			return orderPoints;
		}

		return new ArrayList<UsedPoint>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getTotalIntegralCountByEmail(java.lang
	 * .Integer, java.lang.String)
	 */
	public Integer getTotalIntegralCountByEmail(Integer siteId, String email) {
		return historyMapper.getTotalIntegralCountByEmail(siteId, email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getTotalUsedCountByEmail(java.lang.Integer
	 * , java.lang.String, java.lang.String)
	 */
	public Integer getTotalUsedCountByEmail(Integer siteId, String email,
			String doType) {
		return historyMapper.getTotalUsedCountByEmail(siteId, email, doType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getTotalUsePointByEmail(java.lang.Integer
	 * , java.lang.String)
	 */
	public Integer getTotalUsePointByEmail(Integer siteId, String email) {
		return historyMapper.getTotalUsePointByEmail(siteId, email,
				IPointsService.LOCK_TYPE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#saveMemberIntegralHistory(java.lang.String
	 * , java.lang.Integer, java.lang.Integer, java.lang.String,
	 * java.lang.String)
	 */
	public void saveMemberIntegralHistory(String email, Integer siteId,
			Integer integral, String type, String source) {
		MemberIntegralHistory memberIntegralHistory = new MemberIntegralHistory();
		memberIntegralHistory.setCemail(email);
		memberIntegralHistory.setIintegral(integral.doubleValue());
		memberIntegralHistory.setIstatus(1);
		memberIntegralHistory.setIwebsiteid(siteId);
		memberIntegralHistory.setCdotype(type);
		memberIntegralHistory.setCremark(type);
		memberIntegralHistory.setCsource(source);
		historyMapper.insert(memberIntegralHistory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#getIntegralBySignDayCheck(java.lang.Integer
	 * )
	 */
	public Integer getIntegralBySignDayCheck(Integer day) {
		if (day >= 6) {
			return 10;
		}
		List<MemberSignRule> memberSignRules = historyMapper
				.getMemberSignRule();
		Map<Integer, Integer> signRuleMap = Maps.newHashMap();
		for (MemberSignRule memberSignRule : memberSignRules) {
			signRuleMap.put(memberSignRule.getIcount(),
					memberSignRule.getIntegral());
		}
		// Logger.debug("-------signRuleMap: " +
		// JsonFormatUtils.beanToJson(signRuleMap));
		return signRuleMap.get(day);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.loyalty.IPointsService#integralForType(java.lang.String)
	 */
	public Integer integralForType(String type) {
		Integer integral = 10;
		if (VIDEO.equals(type)) {
			integral = 40;
		} else if (PHOTO.equals(type) || SUBSCRIBER.equals(type)) {
			integral = 20;
		}

		return integral;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.loyalty.IPointsService#checkSubsciberPoint(java.lang.Integer,
	 * java.lang.String, java.lang.String)
	 */
	public boolean checkSubsciberPoint(Integer siteId, String email, String type) {
		MemberIntegralHistory memberIntegralHistory = historyMapper
				.checkSubscriberPoint(siteId, email, type);
		if (null != memberIntegralHistory) {
			return true;
		}
		return false;
	}

	@Override
	public Page<MemberIntegralHistory> getValidPointsByEmail(Integer websiteId,
			String email, Integer pageIndex, Integer pageSize) {
		if (websiteId == null) {
			throw new NullPointerException("websiteId is null");
		}
		if (email == null || email.length() == 0) {
			throw new NullPointerException("email is null");
		}
		if (pageIndex == null || pageIndex <= 0) {
			throw new NullPointerException("pageIndex is null or pageIndex = 0");
		}
		if (pageSize == null || pageSize <= 0) {
			throw new NullPointerException("pageSize is null or pageSize = 0");
		}
		Map<String, Object> paras = Maps.newHashMap();
		paras.put("siteId", websiteId);
		paras.put("email", email);
		paras.put("pageSize", pageSize);
		paras.put("pageNum", pageIndex);
		paras.put("status", 1);

		List<MemberIntegralHistory> list = this.historyMapper
				.getValidPointsByEmail(paras);
		int total = this.historyMapper.getTotal(paras);

		Page<MemberIntegralHistory> page = new Page<MemberIntegralHistory>(
				list, total, pageIndex, pageSize);

		return page;
	}

	@Override
	public boolean usePoint(int costPoints, String cartId) {
		LoginContext loginCtx = foundationService.getLoginContext();
		int site = foundationService.getSiteID();
		return this.usePoint(costPoints, cartId, loginCtx, site);
	}

	/**
	 * 使用积分到购物车
	 * 
	 * @author lijun
	 * @param costPoints
	 *            要使用的积分
	 * @param cartId
	 *            购物车id
	 * @param email
	 *            用户email
	 * @param siteId
	 *            站点id
	 * @return true 使用优惠券成功
	 */
	private boolean usePoint(int costPoints, String cartId,
			LoginContext loginCtx, int siteId) {
		if (cartId == null || cartId.length() == 0) {
			throw new NullPointerException("cartId is null");
		}
		// 用户是否登录
		if (loginCtx == null || !loginCtx.isLogin()) {
			Logger.debug("用户未登录不能使用积分{}到购物车：{}", costPoints, cartId);
			return false;
		}

		String email = loginCtx.getMemberID();
		// 验证参数cartId是否是当前用户的购物车
		CartOwner cartOwner = cartService.getCartOwner(cartId);
		if (cartOwner == null) {
			Logger.debug("系统中不存在购物车：{}", cartId);
			return false;
		}
		if (cartOwner.getEmail() == null || !email.equals(cartOwner.getEmail())) {
			Logger.debug("当前用户{}不存在购物车{}", email, cartId);
			return false;
		}
		try {

			// 购物车是否已经使用过优惠券
			Cart cart = cartLifecycle.getCart(cartId);
			if (!cart.validExtraLine(pointsExtrasProvider.getId())) {
				Logger.debug("购物车：{}已经使用过积分,不能再次使用积分", cartId);
				return false;
			}
			// 验证当前用户是否有可使用的积分
			// 目前用户可用积分
			int usefullPoint = this.getUsefulPoints(email, siteId);
			if (costPoints > usefullPoint) {
				Logger.debug("用户:{}目前可用积分:{},用户当前想使用的积分{}大于可用积分", email,
						usefullPoint, costPoints);
				return false;
			}
			// 检查积分使用规则
			int group = loginCtx.getGroupID();
			IntegralUseRule rule = this.getIntegralUseRule(group);
			Integer max = rule.getImaxuse();
			if (max != null && costPoints > max) {
				Logger.debug(
						"用户:{}所属group:{}积分使用规则中最大可用积分为:{},用户当前想使用的积分{}大于规则中最大可用积分",
						email, group, max, costPoints);
				return false;
			}

			ExtraLine extra = new ExtraLine();
			extra.setPayload(String.valueOf(costPoints));
			extra.setPluginId(pointsExtrasProvider.getId());
			cart.addExtraLine(extra);
			return true;
		} catch (Exception e) {
			Logger.debug("购物车：{}使用积分出现异常", cartId, e);
			return false;
		}
	}

	@Override
	public boolean usePoint(int costPoints, String cartId, WebContext webCtx) {
		if (webCtx == null) {
			throw new NullPointerException("WebContext is null");
		}

		LoginContext loginCtx = foundationService.getLoginContext(webCtx);
		int site = foundationService.getSiteID(webCtx);
		return this.usePoint(costPoints, cartId, loginCtx, site);
	}

	@Override
	public boolean cancelUsedPoint(String cartId) {
		LoginContext loginCtx = foundationService.getLoginContext();
		return this.cancelUsedPoint(cartId, loginCtx);
	}

	private boolean cancelUsedPoint(String cartId, LoginContext loginCtx) {
		if (cartId == null || cartId.length() == 0) {
			throw new NullPointerException("cartId is null");
		}
		// 用户是否登录
		if (loginCtx == null || !loginCtx.isLogin()) {
			Logger.debug("用户未登录取消购物车：{}积分失败", cartId);
			return false;
		}

		String email = loginCtx.getMemberID();
		// 验证参数cartId是否是当前用户的购物车
		CartOwner cartOwner = cartService.getCartOwner(cartId);
		if (cartOwner == null) {
			Logger.debug("系统中不存在购物车：{}", cartId);
			return false;
		}
		if (cartOwner.getEmail() == null || !email.equals(cartOwner.getEmail())) {
			Logger.debug("当前用户{}不存在购物车{}", email, cartId);
			return false;
		}

		try {
			Cart cart = cartLifecycle.getCart(cartId);

			cart.delExtraLine(OrderConstants.POINTS);
		} catch (Exception e) {
			Logger.debug("删除购物车{}积分失败", cartId, e);
			return false;
		}

		return true;
	}

	@Override
	public boolean cancelUsedPoint(String cartId, WebContext webCtx) {
		LoginContext loginCtx = foundationService.getLoginContext(webCtx);
		return this.cancelUsedPoint(cartId, loginCtx);
	}

	@Override
	public LoyaltyPrefer applyPoints(String email, List<CartItem> cartItems,
			Integer costPoints, WebContext webContext) {
		LoyaltyPrefer loyaltyPrefer = new LoyaltyPrefer();
		if (null == email || costPoints == null) {
			return loyaltyPrefer;
		}

		Integer memberPoints = this.getUsefulPoints(email,
				foundationService.getSiteID());
		Integer rullMaxUsePoints = this.getIntegralUseRule(email, webContext)
				.getImaxuse();
		if ((memberPoints <= 0) || (costPoints > memberPoints)) {
			return loyaltyPrefer;
		}
		costPoints = costPoints < rullMaxUsePoints ? costPoints
				: rullMaxUsePoints;

		Double money = this.getMoneyByContex(costPoints, email, webContext);
		loyaltyPrefer.setIsSuccess(true);
		loyaltyPrefer.setPreferType("point");
		loyaltyPrefer.setValue(money);
		loyaltyPrefer.setCode(String.valueOf(costPoints));
		return loyaltyPrefer;
	}

	/**
	 * 根据传递的上下文计算
	 * 
	 * @param points
	 * @param email
	 * @param context
	 * @return
	 */
	public Double getMoneyByContex(Integer points, String email,
			WebContext context) {
		IntegralUseRule rule = getIntegralUseRule(email, context);
		// return getMoney(points, rule);
		if (null == rule) {
			return null;
		}
		double money = points * (rule.getFmoney() / rule.getIintegral()) * -1;
		money = currencyService.exchange(money, rule.getCcurrency(),
				foundationService.getCurrency(context));
		return new DoubleCalculateUtils(money).doubleValue();
	}

	@Override
	public boolean saveOrderPrefer(String email, LoyaltyPrefer loyaltyPrefer,
			WebContext webCtx) {
		Integer cost = 0;
		try {
			cost = Integer.parseInt(loyaltyPrefer.getCode());
		} catch (Exception e) {
			Logger.error("Save the points type conversion failure", e);
			return false;
		}
		// 订单记录的Id
		Integer id = this.lockPoints(email,
				foundationService.getSiteID(webCtx), cost, "Pay for order.",
				ContextUtils.getWebContext(Context.current()));
		Order order = loyaltyPrefer.getOrder();
		if (null == order) {
			return false;
		}
		OrderPoints points = new OrderPoints();
		points.setCemail(order.getCmemberemail());
		points.setFparvalue(loyaltyPrefer.getValue());
		points.setIorderid(order.getIid());
		points.setIpointsid(id);
		points.setIstatus(1);
		String remark = "Pay for order." + "No." + order.getCordernumber();
		if (1 == orderPointsMapper.insert(points)
				&& this.updateRemarkById(remark, id)) {
			return true;
		}
		return false;
	}

}
