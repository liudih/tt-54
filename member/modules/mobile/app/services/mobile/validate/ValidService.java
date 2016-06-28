package services.mobile.validate;

import java.util.List;

import javax.inject.Inject;

import mapper.cart.CartBaseMapper;
import mapper.loyalty.IntegralUseRuleMapper;
import mapper.loyalty.MemberIntegralHistoryMapper;
import mapper.member.MemberBaseMapper;
import play.Logger;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.StringUtils;
import services.base.utils.Utils;
import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignExecutionService;
import services.campaign.ICampaignInstance;
import services.cart.ICartLifecycleService;
import services.loyalty.coupon.ICartCouponService;
import services.loyalty.coupon.ICouponService;
import services.mobile.MobileService;
import services.mobile.order.CartInfoService;
import utils.DateUtils;

import com.google.api.client.util.Lists;
import com.google.common.collect.FluentIterable;

import dto.Currency;
import dto.cart.CartBase;
import dto.member.MemberBase;
import dto.mobile.CouponsInfo;
import entity.loyalty.CartCoupon;
import entity.loyalty.Coupon;
import entity.loyalty.IntegralUseRule;
import enums.loyalty.coupon.manager.CouponRuleBack;
import extensions.InjectorInstance;
import extensions.loyalty.campaign.coupon.CouponUseAction;
import extensions.loyalty.campaign.coupon.CouponUseEvent;
import extensions.loyalty.campaign.rule.memberactive.ConponActionRule;
import extensions.loyalty.orderxtras.CouponExtrasProvider;
import extensions.loyalty.orderxtras.PointsExtrasProvider;
import facades.cart.Cart;

public class ValidService {

	@Inject
	MemberBaseMapper memberBaseMapper;
	@Inject
	IntegralUseRuleMapper ruleMapper;
	@Inject
	CurrencyService currencyService;
	@Inject
	MemberIntegralHistoryMapper historyMapper;
	@Inject
	ICartLifecycleService cartLifecycle;
//	@Inject
//	ICouponService couponService;
	@Inject
	CampaignContextFactory ctxFactory;
	@Inject
	CartBaseMapper cartBaseMapper;
	@Inject
	PointsExtrasProvider pointsExtrasProvider;
	@Inject
	CampaignExecutionService campaignExec;
	@Inject
	ICartCouponService ccService;
	@Inject
	CouponExtrasProvider couponExtrasProvider;
	@Inject
	FoundationService foundationService;
	@Inject
	CartInfoService cartInfoService;
	@Inject
	MobileService mobileService;

	/**
	 * 以email和当前站点获取规则，在将points转为当前货币相应金额
	 *
	 * @param points
	 * @param email
	 *            需确保传入参数有效，该方法中不做处理
	 * @param siteId
	 * @param curr
	 *            币种 需要转换的币种类型
	 * @param IntegralUseRule
	 *            用户使用权限类
	 * @return 保留两位有效数字
	 */
	public Double getPonintsMoney(Integer points, String email, Integer siteId,
			String curr, IntegralUseRule rule) {

		double money = points * (rule.getFmoney() / rule.getIintegral()) * -1;
		money = currencyService.exchange(money, rule.getCcurrency(), curr);

		return new DoubleCalculateUtils(money).doubleValue();

	}

	/**
	 * 验证用户是否有可用积分
	 *
	 * @param email
	 * @param points
	 * @param siteID
	 * @param IntegralUseRule
	 *            用户使用权限类
	 * @return
	 */
	public boolean validPoints(String email, int points, Integer siteId,
			IntegralUseRule rule) {

		if (points <= getUsefulPoints(email, siteId)
				&& points <= rule.getImaxuse()) {
			return true;
		}

		return false;
	}

	/**
	 * 获取用户权限类
	 *
	 * @param email
	 * @param siteID
	 * @return IntegralUseRule
	 */

	public IntegralUseRule getRule(String email, Integer siteId) {
		MemberBase mbb = memberBaseMapper.getUserByEmail(email, siteId);
		IntegralUseRule rule = getIntegralUseRule(mbb.getIgroupid(), siteId);
		return rule;
	}

	private IntegralUseRule getIntegralUseRule(Integer groupId, Integer siteId) {

		IntegralUseRule rule = ruleMapper
				.getBySiteIdAndGroupId(siteId, groupId);
		if (null == rule) {
			rule = new IntegralUseRule();
			rule.setCcurrency("USD");
			rule.setFmoney(0.0);
			rule.setImaxuse(0);
			rule.setIwebsiteid(siteId);
			rule.setImembergroupid(groupId);
			rule.setIintegral(0);
		}
		return rule;
	}

	/**
	 * 根据用户email获取用户可用积分
	 *
	 * @param email
	 * @param siteID
	 * @return
	 */
	public int getUsefulPoints(String email, int siteID) {
		Integer points = historyMapper.getSumByEmail(email, siteID);
		return points != null ? points : 0;
	}

	/**
	 * 获取可用优惠劵Available Coupon
	 *
	 * @param cartId
	 * @param email
	 * @param userCurrency
	 * @param siteID
	 * @param languageID
	 * @return List<CouponUser>
	 */
	public List<CouponsInfo> getUsefulCoupon(String cartId, String email,
			String userCurrency, int siteID, int languageID) {

		List<CouponsInfo> cuList = Lists.newArrayList();
		Cart cart = cartInfoService.getCurrentCart(mobileService.getUUID(), true);
		if (null == cart) {
			Logger.error("cart is null");
			return null;
		}
//		List<Coupon> coupons = couponService.selectMyCouponUnused(email);
		List<Integer> codeIds = Lists.newArrayList();
//		FluentIterable.from(coupons).forEach(c -> codeIds.add(c.getCodeId()));// 把优惠劵ID添加到codeIds
//																				// List<Integer>中

		List<ConponActionRule> actionRules = null;
//		couponService.getActionRule(codeIds);

		CampaignContext context = ctxFactory.createContext(null, null);
		context.setActionOn(cart);
		FluentIterable<ConponActionRule> usableRule = FluentIterable.from(
				actionRules).filter(c -> c.match(context, null));
		Currency currency = currencyService.getCurrencyByCode(userCurrency);
		usableRule
				.forEach(c -> {
					CouponsInfo cu = new CouponsInfo();
					Double amount = c.getParameter().getFcouponamount(); // 如果是现金券
																			// 转换币种为当前币种
					String vdate = "";
					if (CouponRuleBack.CouponType.CASH == c.getParameter()
							.getType()) {
						if (!StringUtils.isEmpty(userCurrency)) {
							try {
								int currentCurrencyId = c.getParameter()
										.getCcurrency();
								Currency currentCurrency = currencyService
										.getCurrencyById(currentCurrencyId);
								if (!userCurrency.equals(currentCurrency
										.getCcode())) {
									amount = currencyService.exchange(amount,
											currentCurrency.getCcode(),
											userCurrency);
								}
							} catch (Exception e) {
								Logger.error("exchange currency failed",
										e.toString());
							}
						}
						cu.setDis(amount);
						cu.setDescr(c.getParameter().getType().getDescribeEN()
								+ " " + currency.getCsymbol()
								+ Utils.money(amount));
					} else {
						double discount = c.getParameter().getFdiscount();
						cu.setDescr(c.getParameter().getType().getDescribeEN()
								+ " " + Utils.money(discount) + "% OFF");
						cu.setDis(amount);
					}

					cu.setCode(c.getParameter().getCode());
					cu.setFlag(c.getParameter().getType().getCode());
					cu.setMinAmt(c.getParameter().getForderamountlimit());
					try {
						vdate = DateUtils.addOndDay(c.getParameter()
								.getDcreatedate(), c.getParameter()
								.getIvalidity());
					} catch (Exception e) {
						Logger.error("format vdate error" + e.toString());
					}
					cu.setVdate(Long.parseLong(vdate));

					cuList.add(cu);
				});

		return cuList;
	}

	/**
	 * 应用优惠劵Available Coupon
	 *
	 * @param cartId
	 * @param email
	 * @param code
	 * @param userCurrency
	 * @param siteID
	 * @param languageID
	 * @return
	 */
	public boolean applyCoupon(String cartId, String email, String code,
			String userCurrency, int siteID, int languageID) {

		Cart cart = cartInfoService.getCurrentCart(mobileService.getUUID(), true);
		if (null == cart) {
			Logger.error("cart is null");
			return false;
		}
		CouponUseEvent event = new CouponUseEvent(cart, code, email, siteID);
		boolean b = true;
		try {

			List<ICampaignInstance> execd = campaignExec.execute(event);
			Logger.debug(
					"Campaign executed: {}",
					com.google.common.collect.Lists.transform(execd,
							ci -> ci.getInstanceId()));

		} catch (Exception e) {
			b = false;
			Logger.error(" coupon execution", e.toString());
		}

		return b;

	}

	/**
	 * 删除订单优惠劵
	 *
	 * @param cartId
	 * @param email
	 * @param userCurrency
	 * @param siteID
	 * @param languageID
	 * @return
	 */
	public boolean deleteCoupon(String cartId, String email, String code,
			String userCurrency, int siteID, int languageID) {
		boolean b = true;

		Cart cart = cartInfoService.getCurrentCart(mobileService.getUUID(), true);
		if (null == cart) {
			Logger.error("cart is null");
			return false;
		}
		CartCoupon cc = new CartCoupon();
		cc.setCcode(code);
		cc.setIstatus(0);
		cc.setCcartid(cart.getId());
		b = ccService.update(cc);

		cart.delExtraLine(CouponUseAction.ID);
		Logger.debug("购物车{}里面的优惠券{}移除成功", cartId, code);

		return b;

	}

	/**
	 * 删除订单使用积分
	 *
	 * @param email
	 * @param ltc
	 * @param userCurrency
	 * @param siteID
	 * @param languageID
	 * @return
	 */
	public boolean delPoints(String email, String ltc, String userCurrency,
			int siteID, int languageID) {
		Cart cart = cartInfoService.getCurrentCart(mobileService.getUUID(), true);
		if (cart!=null && cart.delExtraLine(pointsExtrasProvider.getId())) {
			return true;
		}
		return false;
	}

	/**
	 * 删除使用的推广码(未实现controllers)
	 *
	 * @param email
	 * @param ltc
	 * @param userCurrency
	 * @param siteID
	 * @param languageID
	 * @return
	 */
	public boolean delSpcode(String email, String ltc, String userCurrency,
			int siteID, int languageID) {
		Cart cart = cartInfoService.getCurrentCart(mobileService.getUUID(), true);
		if (cart!=null && cart.delExtraLine(couponExtrasProvider.getId())) {
			return true;
		}
		return false;
	}

}
