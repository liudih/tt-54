package services.loyalty.coupon.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapper.loyalty.OrderCouponMapper;

import org.elasticsearch.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.Http.Context;
import scala.Tuple2;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.StringUtils;
import services.base.utils.Utils;
import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignExecutionService;
import services.campaign.ICampaignInstance;
import services.cart.ICartLifecycleService;
import services.cart.IHandleCartRefreshEventPlugin;
import services.loyalty.IPointsService;
import services.loyalty.coupon.CouponCodeService;
import services.loyalty.coupon.CouponRuleService;
import services.loyalty.coupon.ICartCouponService;
import services.loyalty.coupon.ICouponMainService;
import services.loyalty.coupon.IPromoCodeService;
import services.order.ICheckoutService;
import services.product.ProductEnquiryService;
import services.product.ProductSalePriceService;
import valueobjects.base.Page;
import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyCoupon;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.order_api.cart.ExtraLine;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;

import context.WebContext;
import dao.loyalty.coupon.ICouponDao;
import dto.Currency;
import dto.product.ProductCategoryMapper;
import dto.product.ProductLabel;
import entity.loyalty.CartCoupon;
import entity.loyalty.Coupon;
import entity.loyalty.CouponCode;
import entity.loyalty.CouponRule;
import entity.loyalty.OrderCoupon;
import entity.loyalty.PromoCode;
import entity.loyalty.business.CouponCodeBo;
import enums.loyalty.coupon.manager.CouponRuleBack;
import enums.loyalty.coupon.manager.CouponRuleBack.CouponType;
import enums.loyalty.coupon.manager.CouponRuleSelect;
import enums.loyalty.coupon.manager.Status;
import enums.loyalty.coupon.manager.Type;
import extensions.InjectorInstance;
import extensions.loyalty.campaign.coupon.CouponUseAction;
import extensions.loyalty.campaign.coupon.CouponUseEvent;
import extensions.loyalty.campaign.rule.memberactive.ConponActionRule;
import extensions.loyalty.campaign.rule.memberactive.CouponRuleActionParameter;
import facades.cart.Cart;

/**
 * 
 * @author lijun
 *
 */
public class CouponService implements ICouponMainService,
		IHandleCartRefreshEventPlugin {
	@Inject
	ICouponDao dao;

	@Inject
	CouponCodeService codeService;

	@Inject
	SystemParameterService sysParasService;

	@Inject
	CouponRuleService ruleService;

	@Inject
	CampaignContextFactory ctxFactory;

	@Inject
	ICartCouponService ccService;

	@Inject
	FoundationService fservice;

	@Inject
	IPromoCodeService promoCodeService;

	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	CurrencyService currencyService;

	@Inject
	CampaignExecutionService campaignExec;

	@Inject
	ProductSalePriceService pspService;

	@Inject
	ProductEnquiryService pService;

	@Inject
	IPointsService pointsService;

	@Inject
	OrderCouponMapper orderCouponMapper;

	@Inject
	ICheckoutService checkoutService;

	@Inject
	CouponCodeService couponCodeService;

	@Override
	public Page<Coupon> selectForPage(int page, int pageSize, String email,
			String code) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("page", page);
		paras.put("pageSize", pageSize);
		if (!StringUtils.isEmpty(email)) {
			paras.put("email", email);
		}
		if (!StringUtils.isEmpty(code)) {
			paras.put("code", code);
		}
		List<Coupon> list = this.dao.selectForPage(paras);
		int total = this.dao.getTotal(paras);
		return new Page<Coupon>(list, total, page, pageSize);
	}

	@Override
	public int getTotal() {
		return this.dao.getTotal(null);
	}

	@Override
	public int add(Coupon c) {
		return this.dao.add(c);
	}

	@Override
	public int delete(int id) {
		return this.dao.delete(id);
	}

	@Override
	public int updateStatus(int id, int code) {
		Coupon c = new Coupon();
		c.setId(id);
		c.setStatus(code);
		return this.dao.update(c);
	}

	@Override
	public Coupon selectById(int id) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("id", id);
		List<Coupon> list = this.dao.select(paras);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@Override
	public int update(Coupon c) {
		return this.dao.update(c);
	}

	/**
	 * 为新注册用户赠送优惠券
	 */
	public List<Coupon> giftCouponForSignin(String userEmail, int siteId) {
		// 优惠券codes,返回给调用者
		List<Coupon> codes = Lists.newLinkedList();
		if (StringUtils.isEmpty(userEmail)) {
			throw new NullPointerException("userEmail is null");
		}

		// 先判断用户是否已经获取过
		boolean existed = this.isExistedForSignin(userEmail);
		if (existed) {
			Logger.debug("{}已经送过激活优惠券", userEmail);
			return codes;
		}

		// 获取发送优惠券张数配置参数
		// 默认是0张
		int nums = 0;
		String numsStr = sysParasService.getSystemParameter(siteId, 0,
				"numsCouponForSignin");

		if (!StringUtils.isEmpty(numsStr)) {
			try {
				nums = Integer.parseInt(numsStr);
			} catch (Exception e) {
				if (Logger.isInfoEnabled()) {
					Logger.info("<<<<<<<<<<<<<<<<<<<<<<numsCouponForSignin in table t_system_parameter is invalid");
				}
			}

		}
		Logger.info("<<<<<<<<<<<<<<<<<<<<<<numsCouponForSignin={}", nums);
		Logger.info("<<<<<<<<<<<<<<<<<<<<<<siteId={}", siteId);
		for (int i = 0; i < nums; i++) {
			int codeId = codeService.getCodeIdByLogin(siteId);
			// code == 0 代表获取优惠券code失败
			if (0 != codeId) {
				Coupon c = new Coupon();

				c.setWebsiteId(siteId);
				c.setEmail(userEmail);
				c.setCodeId(codeId);
				c.setStatus(Status.SEND.getCode());
				c.setType(Type.REGISTER_MEMBER.getCode());
				c.setCreator(0);
				this.add(c);
				codes.add(c);
			} else {
				if (Logger.isDebugEnabled()) {
					Logger.debug(
							"<<<<<<<<<<<<<<<<<<<<<<get {}th signin coupon code is failed for {}",
							i, userEmail);
				}
			}
		}
		return codes;
	}

	/**
	 * 为新注册用户补发优惠券
	 */
	@Override
	public List<Coupon> giftCouponForSigninRessiue(String userEmail, int siteId) {
		// 优惠券codes,返回给调用者
		List<Coupon> codes = Lists.newLinkedList();
		if (StringUtils.isEmpty(userEmail)) {
			throw new NullPointerException("userEmail is null");
		}

		// 先判断用户是否已经获取过
		boolean existed = this.isExistedForSignin(userEmail);
		if (existed) {
			Logger.debug("{}已经送过激活优惠券", userEmail);
			return codes;
		}

		// 获取发送优惠券张数配置参数
		// 默认是0张
		int nums = 0;
		String numsStr = sysParasService.getSystemParameter(siteId, 0,
				"numsCouponForSignin");

		if (!StringUtils.isEmpty(numsStr)) {
			try {
				nums = Integer.parseInt(numsStr);
			} catch (Exception e) {
				if (Logger.isInfoEnabled()) {
					Logger.info("<<<<<<<<<<<<<<<<<<<<<<numsCouponForSignin in table t_system_parameter is invalid");
				}
			}

		}
		Logger.info("<<<<<<<<<<<<<<<<<<<<<<numsCouponForSignin={}", nums);
		Logger.info("<<<<<<<<<<<<<<<<<<<<<<siteId={}", siteId);
		for (int i = 0; i < nums; i++) {
			int codeId = codeService.getCodeIdByLogin(siteId);
			// code == 0 代表获取优惠券code失败
			if (0 != codeId) {
				Coupon c = new Coupon();

				c.setWebsiteId(siteId);
				c.setEmail(userEmail);
				c.setCodeId(codeId);
				c.setStatus(Status.SEND.getCode());
				c.setType(Type.REGISTER_MEMBER.getCode());
				// 补发优惠券特殊标示
				c.setCreator(5959);
				this.add(c);
				codes.add(c);
			} else {
				if (Logger.isDebugEnabled()) {
					Logger.debug(
							"<<<<<<<<<<<<<<<<<<<<<<get {}th signin coupon code is failed for {}",
							i, userEmail);
				}
			}
		}
		return codes;
	}

	@Override
	public List<Coupon> giftCouponForRSS(String userEmail, int siteId) {
		if (StringUtils.isEmpty(userEmail)) {
			throw new NullPointerException("userEmail is null");
		}
		List<Coupon> codes = Lists.newLinkedList();
		// 先判断用户是否已经获取过
		boolean existed = this.isExistedForRSS(userEmail);
		if (existed) {
			return codes;
		}
		// 获取发送优惠券张数配置参数
		// int siteId = fService.getSiteID();
		// 默认是0张
		int nums = 0;
		String numsStr = sysParasService.getSystemParameter(siteId, 1,
				"numsCouponForRss");
		if (!StringUtils.isEmpty(numsStr)) {
			try {
				nums = Integer.parseInt(numsStr);
			} catch (Exception e) {
				if (Logger.isInfoEnabled()) {
					Logger.info("<<<<<<<<<<<<<<<<<<<<<<numsCouponForRss in table t_system_parameter is invalid");
				}
			}

		}
		for (int i = 0; i < nums; i++) {
			int codeId = codeService.getCodeIdBySubscribe(siteId);
			// code == 0 代表获取优惠券code失败
			if (0 != codeId) {
				Coupon c = new Coupon();

				c.setWebsiteId(siteId);
				c.setEmail(userEmail);
				c.setCodeId(codeId);
				c.setStatus(Status.SEND.getCode());
				c.setType(Type.RSS.getCode());
				c.setCreator(0);
				this.add(c);
				codes.add(c);
			} else {
				if (Logger.isDebugEnabled()) {
					Logger.debug(
							"<<<<<<<<<<<<<<<<<<<<<<get {}th RSS coupon code is failed for {}",
							i, userEmail);
				}
			}
		}
		return codes;
	}

	@Override
	public List<Coupon> giftCouponForRSSRessiue(String userEmail, int siteId) {
		if (StringUtils.isEmpty(userEmail)) {
			throw new NullPointerException("userEmail is null");
		}
		List<Coupon> codes = Lists.newLinkedList();
		// 先判断用户是否已经获取过
		boolean existed = this.isExistedForRSS(userEmail);
		if (existed) {
			return codes;
		}
		// 获取发送优惠券张数配置参数
		// int siteId = fService.getSiteID();
		// 默认是0张
		int nums = 0;
		String numsStr = sysParasService.getSystemParameter(siteId, 1,
				"numsCouponForRss");
		if (!StringUtils.isEmpty(numsStr)) {
			try {
				nums = Integer.parseInt(numsStr);
			} catch (Exception e) {
				if (Logger.isInfoEnabled()) {
					Logger.info("<<<<<<<<<<<<<<<<<<<<<<numsCouponForRss in table t_system_parameter is invalid");
				}
			}

		}
		for (int i = 0; i < nums; i++) {
			int codeId = codeService.getCodeIdBySubscribe(siteId);
			// code == 0 代表获取优惠券code失败
			if (0 != codeId) {
				Coupon c = new Coupon();

				c.setWebsiteId(siteId);
				c.setEmail(userEmail);
				c.setCodeId(codeId);
				c.setStatus(Status.SEND.getCode());
				c.setType(Type.RSS.getCode());
				// 补发优惠券特殊标示
				c.setCreator(5959);
				this.add(c);
				codes.add(c);
			} else {
				if (Logger.isDebugEnabled()) {
					Logger.debug(
							"<<<<<<<<<<<<<<<<<<<<<<get {}th RSS coupon code is failed for {}",
							i, userEmail);
				}
			}
		}
		return codes;
	}

	@Override
	public Page<Coupon> selectMyCouponForPage(int page, int pageSize,
			String userEmail) {
		if (StringUtils.isEmpty(userEmail)) {
			throw new NullPointerException();
		}
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("page", page);
		paras.put("pageSize", pageSize);
		paras.put("userEmail", userEmail);
		List<Coupon> list = this.dao.selectMyCouponForPage(paras);
		int total = this.dao.getTotal(paras);
		Page<Coupon> messages = new Page<Coupon>(list, total, page, pageSize);
		return messages;
	}

	@Override
	public boolean isExistedForRSS(String userEmail) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("userEmail", userEmail);
		paras.put("type", Type.RSS.getCode());
		int result = this.dao.isExisted(paras);
		return result == 0 ? false : true;
	}

	@Override
	public int getTotalMyCouponUnused(String userEmail) {
		if (StringUtils.isEmpty(userEmail)) {
			return 0;
		}
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("userEmail", userEmail);
		paras.put("unused", "true");
		return this.dao.getTotalMyCoupon(paras);
	}

	@Override
	public Page<Coupon> selectMyCouponUnusedForPage(int page, int pageSize,
			String userEmail) {
		if (StringUtils.isEmpty(userEmail)) {
			throw new NullPointerException();
		}
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("page", page);
		paras.put("pageSize", pageSize);
		paras.put("userEmail", userEmail);
		paras.put("unused", "true");
		List<Coupon> list = this.dao.selectMyCouponForPage(paras);
		int total = this.dao.getTotalMyCoupon(paras);
		Page<Coupon> messages = new Page<Coupon>(list, total, page, pageSize);
		return messages;
	}

	@Override
	public Page<Coupon> selectMyCouponUsedForPage(int page, int pageSize,
			String userEmail) {
		if (StringUtils.isEmpty(userEmail)) {
			throw new NullPointerException();
		}
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("page", page);
		paras.put("pageSize", pageSize);
		paras.put("userEmail", userEmail);
		paras.put("used", "true");
		List<Coupon> list = this.dao.selectMyCouponForPage(paras);
		int total = this.dao.getTotalMyCoupon(paras);
		Page<Coupon> messages = new Page<Coupon>(list, total, page, pageSize);
		return messages;
	}

	@Override
	public boolean isExistedForSignin(String userEmail) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("userEmail", userEmail);
		paras.put("type", Type.REGISTER_MEMBER.getCode());
		int result = this.dao.isExisted(paras);
		return result == 0 ? false : true;
	}

	@Override
	public List<Coupon> selectMyCouponUnused(String userEmail) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("userEmail", userEmail);
		paras.put("unused", "true");
		List<Coupon> list = this.dao.selectMyCouponForPage(paras);
		return list;
	}

	@Override
	public boolean myCouponUseable(String userEmail, String code) {
		if (StringUtils.isEmpty(userEmail)) {
			return false;
		}
		if (StringUtils.isEmpty(code)) {
			return false;
		}

		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("userEmail", userEmail);
		paras.put("code", code);
		return this.dao.myCouponUseable(paras);
	}

	@Override
	public ConponActionRule getActionRule(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		// 去获取配置的rule
		CouponRule rule = ruleService.getCouponRuleByCode(code);
		// 获取创建时间
		CouponCode cc = codeService.getCouponCodeByCode(code);
		// 把CouponRule转换成ConponActionRule
		if (rule != null) {
			ConponActionRule actionRule = this.getConponActionRule(rule);
			actionRule.getParameter().setDcreatedate(cc.getDcreatdate());
			return actionRule;
		}
		return null;
	}

	/**
	 * 处理购物车刷新事件,此方法只给pc端调用
	 * 
	 * @param cart
	 */
	public void handleCartRefreshEvent(Cart cart) {
		Map<String, ExtraLine> el = cart.getExtraLines();
		ExtraLine couponLine = el.get(CouponUseAction.ID);
		if (couponLine != null) {
			String payload = couponLine.getPayload();
			JsonNode payloadJson = Json.parse(payload);
			JsonNode codeNode = payloadJson.get("code");
			if (codeNode != null) {
				String code = codeNode.asText();
				ConponActionRule rule = this.getActionRule(code);
				CampaignContext context = ctxFactory.createContext(null, null);
				// 把pc端的上下文设置进去
				WebContext webContext = fservice.getWebContext();
				context.setWebContext(webContext);
				context.setActionOn(cart);
				boolean isMatch = rule.match(context, null);
				// 如果不匹配rule了则需要把该购物车的优惠券移除
				if (!isMatch) {
					Logger.debug(
							"^^^^^^^^^^^^^^^^^^^^购物车：{} 里面的购物券{} 不再符合规则,需移除该张优惠券",
							cart.getId(), code);
					CartCoupon cc = new CartCoupon();
					cc.setCcode(code);
					cc.setIstatus(0);
					cc.setCcartid(cart.getId());
					ccService.update(cc);
					// 清除额外行
					cart.delExtraLine(CouponUseAction.ID);
					Logger.debug("^^^^^^^^^^^^^^^^^^^^移除成功");
				}
			}
		}
	}

	@Override
	public List<ConponActionRule> getActionRule(List<Integer> codeIds) {
		// 去获取配置的rule
		List<CouponCodeBo> codes = codeService.getCouponCodesByCodeIds(codeIds);
		// 找出当前符合rule的可用coupon
		List<ConponActionRule> actionRules = new ArrayList<ConponActionRule>(
				codes.size());
		FluentIterable.from(codes).forEach(c -> {
			if (c.getDcreatdate() == null) {
				Logger.error("获取不到优惠券{}的创建时间,所以跳过该优惠券", c.getCcode());
			}
			CouponRule r = c.getCouponRule();
			if (r != null && c.getDcreatdate() != null) {
				ConponActionRule actionRule = this.getConponActionRule(r);
				actionRule.getParameter().setCode(c.getCcode());
				actionRule.getParameter().setDcreatedate(c.getDcreatdate());
				actionRules.add(actionRule);
			}
		});
		return actionRules;
	}

	/**
	 * 通过CouponRule对象来构建ConponActionRule对象
	 * 
	 * @param cr
	 * @return
	 */
	private ConponActionRule getConponActionRule(CouponRule cr) {
		ConponActionRule actionRule = InjectorInstance.getInjector()
				.getInstance(ConponActionRule.class);
		actionRule.setRuleId(cr.getIid());
		CouponRuleActionParameter paras = new CouponRuleActionParameter(
				cr.getIid());
		BeanUtils.copyProperties(cr, paras);
		// 设置优惠券的创建日期
		Logger.debug("规则{}创建时间{}", cr.getIid(), cr.getDcreatedate());
		// 设置允许使用sku
		String skus = cr.getCsku();
		String[] arraySku = org.apache.commons.lang3.StringUtils.split(skus,
				",");
		if (null != arraySku && arraySku.length > 0) {
			List<String> rulesku = Arrays.asList(arraySku);
			paras.setSkus(rulesku);
		}
		// 设置允许使用终端
		String terminals = cr.getCuseterminal();
		String[] arrayTerminal = org.apache.commons.lang3.StringUtils.split(
				terminals, ",");
		List<String> list = new ArrayList<String>(5);
		if (null != arrayTerminal && arrayTerminal.length > 0) {
			for (int i = 0; i < arrayTerminal.length; i++) {
				CouponRuleBack.UseTerminal useTerminal = CouponRuleBack.UseTerminal
						.getUseTerminal(arrayTerminal[i]);
				list.add(useTerminal.getTerminalEN());
			}
			paras.setUseTerminal(list);
		}
		actionRule.setParameter(paras);
		return actionRule;
	}

	@Override
	public ConponActionRule getActionRule(int ruleId) {
		CouponRule rule = ruleService.getCouponRuleByRuleId(ruleId);
		ConponActionRule ar = this.getConponActionRule(rule);
		return ar;
	}

	@Override
	public ConponActionRule getActionRuleForPormoCode(String promoCode) {
		if (promoCode == null || promoCode.length() == 0) {
			return null;
		}
		PromoCode pc = promoCodeService.selectPromoCodeByCode(promoCode);
		if (pc == null || pc.getCreateDate() == null) {
			Logger.error("数据库不存在该推广码或获取不到推广码{}的创建时间,所以不能使用该推广码", promoCode);
			return null;
		}

		int ruleId = pc.getRuleId();
		ConponActionRule ar = this.getActionRule(ruleId);
		if (ar != null) {
			ar.getParameter().setDcreatedate(pc.getCreateDate());
			return ar;
		}
		return null;
	}

	@Override
	public List<valueobjects.loyalty.Coupon> getMyUsableCoupon(String email,
			String cartId, WebContext webCtx) {
		List<Coupon> coupons = this.selectMyCouponUnused(email);
		List<Integer> codeIds = new ArrayList<Integer>();
		Cart cart = cartLifecycle.getCart(cartId);
		FluentIterable.from(coupons).forEach(c -> codeIds.add(c.getCodeId()));

		List<ConponActionRule> actionRules = this.getActionRule(codeIds);

		// 开始过滤满足rule的优惠券
		CampaignContext context = ctxFactory.createContext(null, null);
		context.setActionOn(cart);
		context.setWebContext(webCtx);

		// 获取当前币种
		String userCurrency = (webCtx == null ? fservice.getCurrency()
				: fservice.getCurrency(webCtx));
		Currency currency = currencyService.getCurrencyByCode(userCurrency);
		// 过滤出满足条件的优惠券
		FluentIterable<ConponActionRule> usableRule = FluentIterable.from(
				actionRules).filter(c -> c.match(context, null));
		List<valueobjects.loyalty.Coupon> result = Lists.newLinkedList();

		FluentIterable
				.from(usableRule.toList())
				.forEach(
						c -> {
							valueobjects.loyalty.Coupon coupon = new valueobjects.loyalty.Coupon();
							Double amount = c.getParameter().getFcouponamount();
							coupon.setCode(c.getParameter().getCode());
							if (CouponRuleBack.CouponType.CASH == c
									.getParameter().getType()) {
								coupon.setCash(true);

								if (!StringUtils.isEmpty(userCurrency)) {
									try {
										int currentCurrencyId = c
												.getParameter().getCcurrency();
										Currency currentCurrency = currencyService
												.getCurrencyById(currentCurrencyId);
										if (!userCurrency
												.equals(currentCurrency
														.getCcode())) {
											amount = currencyService.exchange(
													amount,
													currentCurrency.getCcode(),
													userCurrency);
										}
									} catch (Exception e) {
										Logger.error(
												"exchange currency failed", e);
									}
								}
								coupon.setAmount(amount);
								coupon.setCurrency(currency);
							} else {
								double discount = c.getParameter()
										.getFdiscount();
								coupon.setCash(false);
								coupon.setPercent(discount);
							}
							result.add(coupon);
						});
		return result;
	}

	@Override
	public boolean applyCoupon(String email, String cartId, String code,
			WebContext webContext) {
		boolean result = false;
		if (!StringUtils.isEmpty(email) && !StringUtils.isEmpty(cartId)
				&& !StringUtils.isEmpty(code)) {
			int siteId = fservice.getSiteID();
			Cart cart = cartLifecycle.getCart(cartId);

			CouponUseEvent event = new CouponUseEvent(cart, code, email, siteId);
			// 传递使用终端设备
			event.setWebContext(webContext);
			try {
				// coupon execution
				List<ICampaignInstance> execd = campaignExec.execute(event);
				result = true;
				Logger.debug("Campaign executed: {}",
						Lists.transform(execd, ci -> ci.getInstanceId()));
				return result;
			} catch (Exception e) {
				result = false;
			}
		}
		return result;
	}

	@Override
	public boolean delCartCoupon(String cartId, String code) {
		boolean result = false;
		if (!StringUtils.isEmpty(cartId) && !StringUtils.isEmpty(code)) {

			Cart cart = cartLifecycle.getCart(cartId);

			CartCoupon cc = new CartCoupon();
			cc.setCcode(code);
			cc.setIstatus(0);
			cc.setCcartid(cart.getId());
			ccService.update(cc);
			// 清除额外行

			result = cart.delExtraLine(CouponUseAction.ID);
			Logger.debug("购物车{}里面的优惠券{}移除成功", cartId, code);
		}
		return result;
	}

	@Override
	public void convert(List<Coupon> coupons) {
		String userCurrency = fservice.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(userCurrency);
		// 转换币种
		if (coupons != null) {
			FluentIterable
					.from(coupons)
					.forEach(c -> {
						// 如果是现金券,那么要把优惠券转换成当前币种

							if (!StringUtils.isEmpty(userCurrency)) {
								try {
									int currentCurrencyId = c.getCurrency();
									Currency currentCurrency = currencyService
											.getCurrencyById(currentCurrencyId);
									c.setCo(currency);
									if (currentCurrencyId != currency.getIid()) {
										if (c.isCash()) {
											double amount = c.getPar();
											amount = currencyService.exchange(
													amount,
													currentCurrency.getCcode(),
													userCurrency);
											c.setValue(Utils.money(amount) + "");
										}
										// 最低消费金额
										double minAmount = c.getMinAmount();
										minAmount = currencyService.exchange(
												minAmount,
												currentCurrency.getCcode(),
												userCurrency);
										c.setMinAmount(minAmount);
									} else {
										if (CouponRuleBack.CouponType.CASH
												.getCode() == c.getValueType()) {
											c.setValue(Utils.money(c.getPar())
													+ "");
										}
									}
								} catch (Exception e) {
									Logger.error("exchange currency failed", e);
									c.setValue(Utils.money(c.getPar()) + "");
								}
							}

							if (!c.isCash()) {
								// 折扣券,在值的后面加上%
								Double discount = c.getDiscount();
								if (discount != null) {
									c.setValue(discount + "% OFF");
								}
							}
							// 计算有效期
							if (CouponRuleSelect.TimeType.VALIDITY.getTypeid()
									.equals(c.getTimeType())) {
								try {
									Integer validDays = c.getValidDays();
									Date createDate = c.getCreateDate();
									if (validDays != null) {
										Calendar calendar = Calendar
												.getInstance();
										calendar.setTime(createDate);
										calendar.add(calendar.DATE, validDays);
										c.setValidStartDate(createDate);
										c.setValidEndDate(calendar.getTime());
									}
								} catch (Exception e) {
									Logger.error("calculate valid date failed",
											e);
								}
							}
						});
		}

	}

	@Override
	public LoyaltyPrefer applyCoupon(String email, List<CartItem> cartItems,
			String code, WebContext webContext) {

		LoyaltyPrefer loyaltyPrefer = prepareApplyCoupon(email, cartItems,
				code, webContext);
		if (null != loyaltyPrefer && loyaltyPrefer.isSuccess()) {
			ConponActionRule actionRule = this.getActionRule(code);

			if (actionRule == null) {
				Logger.error("获取不到优惠券,所以不能使用该优惠券:{}", code);
				LoyaltyPrefer fail = new LoyaltyPrefer();
				fail.setErrorMessage(play.i18n.Messages
						.get("loyalty.errorMessage.noRule"));
				return fail;
			}
			CouponRuleActionParameter rule = actionRule.getParameter();
			if (null != rule) {
				List<CartItem> validCartItems = getValidItem(cartItems, rule);
				if (null != validCartItems && validCartItems.size() > 0) {
					Double value = calculatePrefer(validCartItems, rule,
							webContext);

					loyaltyPrefer.setIsSuccess(true);
					loyaltyPrefer.setCode(code);
					loyaltyPrefer.setValue(value);
					loyaltyPrefer.setPreferType(LOYALTY_TYPE_COUPON);
					if (rule.getType() == CouponType.CASH) {
						loyaltyPrefer.setExtra("cash");
					} else {
						loyaltyPrefer.setExtra("discount");
					}
					return loyaltyPrefer;
				}
			}
		}
		return loyaltyPrefer;
	}

	private LoyaltyPrefer prepareApplyCoupon(String email,
			List<CartItem> cartItems, String code, WebContext webContext) {
		boolean useable = this.myCouponUseable(email, code);
		LoyaltyPrefer fail = new LoyaltyPrefer();
		if (!useable) {
			Logger.error("{}该用户无此张优惠券:{}", email, code);
			fail.setErrorMessage(play.i18n.Messages
					.get("loyalty.errorMessage.noRule"));
			return fail;
		}
		ConponActionRule actionRule = this.getActionRule(code);

		if (actionRule == null) {
			Logger.error("获取不到优惠券,所以不能使用该优惠券:{}", code);
			fail.setErrorMessage(play.i18n.Messages
					.get("loyalty.errorMessage.noRule"));
			return fail;
		}
		CouponRuleActionParameter rule = actionRule.getParameter();
		return this.matchRule(cartItems, rule, webContext);
	}

	/**
	 * 针对code规则判断是否匹配
	 * 
	 * @param cartItems
	 * @param rule
	 * @param webContext
	 * @return
	 */
	private boolean match(List<CartItem> cartItems,
			CouponRuleActionParameter rule, WebContext webContext) {

		if (rule == null) {
			return false;
		}
		// 终端类型做判断
		WebContext terminalWebcontext = webContext;
		if (null == terminalWebcontext) {
			Logger.error("Unable to get the terminal type from context!");
			return false;
		}
		String terminal = fservice.getDevice(terminalWebcontext);
		if (services.base.utils.StringUtils.isEmpty(terminal)) {
			Logger.error("Unable to get the terminal type!");
			return false;
		}
		if (!(rule.getUseTerminal() != null && rule.getUseTerminal().size() > 0)) {
			Logger.error("Coupons available terminal type is empty!");
			return false;
		}
		if (!(rule.getUseTerminal().contains(terminal))) {
			Logger.error("Coupons terminal type does not match!");
			return false;
		}
		// Cart cart = (Cart) context.getActionOn();
		// 总金额金额
		double totalAmount = 0;

		Date now = new Date();
		// 最低消费金额
		Double limitAmount = rule.getForderamountlimit();
		// 把最低消费金额转换成用户所在国家的金额
		if (rule.getTimeType() != null) {

			switch (rule.getTimeType()) {
			case VALIDITY:
				// 优惠券生成日期=有效日期的开始时间
				Date start = rule.getDcreatedate();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(start);
				Integer validity = rule.getIvalidity();
				calendar.add(calendar.DATE, validity);
				// 结束时间
				Date end = calendar.getTime();
				if (end.getTime() < now.getTime()) {
					Logger.error("有效期不满足规则");
					return false;
				}
				break;

			case DATE:
				// 开始时间
				Date startDate = rule.getStartdate();
				Date endDate = rule.getEnddate();
				if (endDate.getTime() < now.getTime()
						|| now.getTime() < startDate.getTime()) {
					Logger.error("有效期不满足规则");
					return false;
				}
				break;
			}
		}
		// 查看购物车里的商品是否在排除商品类型里
		// List<CartItem> cartItems = cart.getAllItems();
		List<CartItem> validProduct = getValidItem(cartItems, rule);
		if (validProduct.size() == 0) {
			Logger.error("购物车内没有满足规则的商品了,所以不能使用该优惠了");
			return false;
		} else {
			// 计算最低消费金额
			totalAmount = checkoutService.subToatl(validProduct);
			// limitAmount不为null则代表有最小消费金额限制
			if (limitAmount != null) {
				// 把面值转换成用户所在国家币值
				String userCurrency = fservice.getCurrency(webContext);
				if (StringUtils.isEmpty(userCurrency)) {
					Logger.error("获取的用户币种为空,优惠券不能使用");
					return false;
				}
				try {
					int currentCurrencyId = rule.getCcurrency();
					Currency currentCurrency = currencyService
							.getCurrencyById(currentCurrencyId);
					limitAmount = currencyService.exchange(limitAmount,
							currentCurrency.getCcode(), userCurrency);
				} catch (Exception e) {
					Logger.error("exchange currency failed", e);
					return false;
				}

				if (limitAmount > totalAmount) {
					Logger.error("最低消费金额不满足规则,limitAmount:{} totalAmount:{}",
							limitAmount, totalAmount);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 针对code规则判断是否匹配
	 * 
	 * @param cartItems
	 * @param rule
	 * @param webContext
	 * @return
	 */
	private LoyaltyPrefer matchRule(List<CartItem> cartItems,
			CouponRuleActionParameter rule, WebContext webContext) {

		if (rule == null) {
			LoyaltyPrefer failResult = new LoyaltyPrefer();
			failResult.setErrorMessage(play.i18n.Messages
					.get("loyalty.errorMessage.noRule"));
			return failResult;
		}
		// 终端类型做判断
		WebContext terminalWebcontext = webContext;
		if (null == terminalWebcontext) {
			Logger.error("Unable to get the terminal type from context!");
			LoyaltyPrefer failResult = new LoyaltyPrefer();
			failResult.setErrorMessage(play.i18n.Messages
					.get("loyalty.errorMessage.noRule"));
			return failResult;
		}
		String terminal = fservice.getDevice(terminalWebcontext);
		if (services.base.utils.StringUtils.isEmpty(terminal)) {
			Logger.error("Unable to get the terminal type!");
			LoyaltyPrefer failResult = new LoyaltyPrefer();
			failResult.setErrorMessage(play.i18n.Messages
					.get("loyalty.errorMessage.noRule"));
			return failResult;
		}
		if (!(rule.getUseTerminal() != null && rule.getUseTerminal().size() > 0)) {
			Logger.error("Coupons available terminal type is empty!");
			LoyaltyPrefer failResult = new LoyaltyPrefer();
			failResult.setErrorMessage(play.i18n.Messages
					.get("loyalty.errorMessage.noRule"));
			return failResult;
		}
		if (!(rule.getUseTerminal().contains(terminal))) {
			Logger.error("Coupons terminal type does not match!");
			LoyaltyPrefer failResult = new LoyaltyPrefer();
			String warn = play.i18n.Messages
					.get("loyalty.errorMessage.platform");
			warn = warn.replace("xxx", rule.getUseTerminal().toString());
			failResult.setErrorMessage(warn);
			return failResult;
		}
		// Cart cart = (Cart) context.getActionOn();
		// 总金额金额
		double totalAmount = 0;

		Date now = new Date();
		// 最低消费金额
		Double limitAmount = rule.getForderamountlimit();
		// 把最低消费金额转换成用户所在国家的金额
		if (rule.getTimeType() != null) {

			switch (rule.getTimeType()) {
			case VALIDITY:
				// 优惠券生成日期=有效日期的开始时间
				Date start = rule.getDcreatedate();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(start);
				Integer validity = rule.getIvalidity();
				calendar.add(calendar.DATE, validity);
				// 结束时间
				Date end = calendar.getTime();
				if (end.getTime() < now.getTime()) {
					Logger.error("有效期不满足规则");
					LoyaltyPrefer failResult = new LoyaltyPrefer();
					failResult.setErrorMessage(play.i18n.Messages
							.get("loyalty.errorMessage.date"));
					return failResult;
				}
				break;

			case DATE:
				// 开始时间
				Date startDate = rule.getStartdate();
				Date endDate = rule.getEnddate();
				if (endDate.getTime() < now.getTime()
						|| now.getTime() < startDate.getTime()) {
					Logger.error("有效期不满足规则");
					LoyaltyPrefer failResult = new LoyaltyPrefer();
					failResult.setErrorMessage(play.i18n.Messages
							.get("loyalty.errorMessage.date"));
					return failResult;
				}
				break;
			}
		}
		// 查看购物车里的商品是否在排除商品类型里
		// List<CartItem> cartItems = cart.getAllItems();
		List<CartItem> validProduct = getValidItem(cartItems, rule);
		if (validProduct.size() == 0) {
			Logger.error("购物车内没有满足规则的商品了,所以不能使用该优惠了");
			LoyaltyPrefer failResult = new LoyaltyPrefer();
			failResult.setErrorMessage(play.i18n.Messages
					.get("loyalty.errorMessage.category"));
			return failResult;
		} else {
			// 计算最低消费金额
			totalAmount = checkoutService.subToatl(validProduct);
			// limitAmount不为null则代表有最小消费金额限制
			if (limitAmount != null) {
				// 把面值转换成用户所在国家币值
				String userCurrency = fservice.getCurrency(webContext);
				if (StringUtils.isEmpty(userCurrency)) {
					Logger.error("获取的用户币种为空,优惠券不能使用");
					LoyaltyPrefer failResult = new LoyaltyPrefer();
					failResult.setErrorMessage("currency is null!");
					return failResult;
				}
				Currency currentCurrency = null;
				try {
					int currentCurrencyId = rule.getCcurrency();
					currentCurrency = currencyService
							.getCurrencyById(currentCurrencyId);
					limitAmount = currencyService.exchange(limitAmount,
							currentCurrency.getCcode(), userCurrency);
				} catch (Exception e) {
					Logger.error("exchange currency failed", e);
					LoyaltyPrefer failResult = new LoyaltyPrefer();
					failResult.setErrorMessage("exchange currency failed!");
					return failResult;
				}

				if (limitAmount > totalAmount) {
					Logger.error("最低消费金额不满足规则,limitAmount:{} totalAmount:{}",
							limitAmount, totalAmount);
					LoyaltyPrefer failResult = new LoyaltyPrefer();
					String warn = play.i18n.Messages
							.get("loyalty.errorMessage.lessMoney");
					warn = warn.replaceAll("xxx", userCurrency
							+ Utils.money(limitAmount, userCurrency));
					failResult.setErrorMessage(warn);
					return failResult;
				}
			}
		}
		LoyaltyPrefer success = new LoyaltyPrefer();
		success.setIsSuccess(true);
		return success;
	}

	/**
	 * 获取符合规则的商品,最小消费金额,优惠折扣 是以排除不符合规则后的商品算的
	 * 
	 * @param cartItems
	 * @param parameter
	 * @return
	 */
	public List<CartItem> getValidItem(List<CartItem> cartItems,
			CouponRuleActionParameter parameter) {
		final List<Integer> excludeCategoryIds = parameter
				.getExcludeCategoryIds();
		final List<String> excludeLabelType = parameter.getExcludeProductIds();
		Logger.debug("购物车里有{}件商品", cartItems.size());
		// 满足规则的产品
		List<CartItem> validProduct = FluentIterable
				.from(cartItems)
				.filter(c -> {
					String listingid = c.getClistingid();
					Tuple2<List<ProductCategoryMapper>, List<ProductLabel>> lc = pService
							.getLabelAndCategory(listingid);
					// 检查该商品是否是折扣商品
					boolean isOffPrice = pspService.isOffPrice(listingid);
					if (isOffPrice) {
						Logger.error("商品{}是折扣商品", listingid);
						ProductLabel OffPrice = new ProductLabel();
						OffPrice.setCtype("OffPrice");
						OffPrice.setClistingid(listingid);
						List<ProductLabel> productLabels = lc._2;
						productLabels.add(OffPrice);
					}
					// 判断sku是否满足,当sku存在时,所有的排除商品品类以及排除商品标签都失效
					List<String> skus = parameter.getSkus();
					if (null != skus && skus.size() > 0) {
						if (skus.contains(c.getSku())) {
							return true;
						} else {
							Logger.error("sku does not meet the rules,sku=={}",
									c.getSku());
							return false;
						}
					}

					if (lc != null) {
						// 判断产品目录是否满足规则
						List<ProductCategoryMapper> category = lc._1;
						// 获取最小级别目录
						Logger.debug("category != null:{}", category != null);
						Logger.debug("excludeCategoryIds != null:{}",
								excludeCategoryIds != null);
						Logger.debug("{}", excludeCategoryIds);
						if (category != null && excludeCategoryIds != null) {
							Logger.debug("==========开始判断商品Category===========");
							List<ProductCategoryMapper> invalidCategory = FluentIterable
									.from(category)
									.filter(l -> {
										Integer cid = l.getIcategory();
										Logger.debug("商品{}所属Category:{}",
												listingid, cid);
										if (excludeCategoryIds.contains(cid)) {
											Logger.debug("商品{}属于排除Category{}",
													listingid, cid);
											return true;
										}
										return false;
									}).toList();
							if (invalidCategory.size() > 0) {
								Logger.debug(
										"产品{}属于规则排除Category内,所以该产品不参与最低消费金额的计算",
										listingid);
								return false;
							}
						}
						// 判断产品类别是否满足规则
						List<ProductLabel> labels = lc._2;
						if (labels != null && excludeLabelType != null) {
							Logger.debug("==========开始判断商品标签===========");
							List<ProductLabel> invalidLabel = FluentIterable
									.from(labels)
									.filter(l -> {
										String type = l.getCtype();
										Logger.debug("商品{}标签:{}", listingid,
												type);
										if (excludeLabelType.contains(type)) {
											Logger.debug("商品{}属于排除标签{}",
													listingid, type);
											return true;
										}
										return false;
									}).toList();
							Logger.debug("==========结束判断商品标签===========");
							if (invalidLabel.size() > 0) {
								Logger.debug("产品{}属于规则排除标签内,所以该产品不参与最低消费金额的计算",
										listingid);
								return false;
							}
						}

					}

					return true;
				}).toList();
		return validProduct;
	}

	/**
	 * 计算商品的总价
	 * 
	 * @return
	 */
	public double getTotalPrice(List<CartItem> items) {
		Logger.debug("开始计算{}商品总价", items.size());
		DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0);
		double total = 0;
		for (CartItem c : items) {
			total = total + c.getPrice().getPrice();
		}
		Logger.debug("================total:{}", total);
		duti = duti.add(total);
		return duti.doubleValue();
	}

	/**
	 * 计算符合规则条件的商品总价
	 * 
	 * @param validCartItems
	 * @param rule
	 * @return
	 */
	private Double calculatePrefer(List<CartItem> validCartItems,
			CouponRuleActionParameter rule, WebContext webContext) {
		String code = null;
		try {
			// code = dataJson.get("code").asText();
			Double amount = rule.getFcouponamount();
			Integer currency = rule.getCcurrency();

			double value = 0;
			if (rule.getType() != null && (rule.getType() == CouponType.CASH)
					&& amount != null && currency != null) {
				// 把面值转换成用户所在国家币值
				String userCurrency = fservice.getCurrency(webContext);
				if (StringUtils.isEmpty(userCurrency)) {
					Logger.error("获取的用户币种为空,优惠券不能使用");
					return 0D;
				}
				try {
					int currentCurrencyId = currency;
					Currency currentCurrency = currencyService
							.getCurrencyById(currentCurrencyId);
					value = currencyService.exchange(amount,
							currentCurrency.getCcode(), userCurrency);
					// 针对一些货币精度做处理
					String couponValue = Utils.money(value, userCurrency)
							.replaceAll(",", "");
					value = Double.valueOf(couponValue);
				} catch (Exception e) {
					Logger.error("exchange currency failed", e);
					return 0D;
				}
			} else {
				Float rate = rule.getFdiscount();
				if (rate != null) {
					// ConponActionRule actionRule = this
					// .getActionRule(code);
					// //List<CartItem> cartItems = cart.getAllItems();
					// CouponRuleActionParameter parameter = actionRule
					// .getParameter();
					// // 排除不满足规则的商品,用于计算折扣
					// List<CartItem> validItems = actionRule.getValidItem(
					// cartItems, parameter);

					if (validCartItems != null && validCartItems.size() > 0) {
						double total = checkoutService.subToatl(validCartItems);
						double rd = rate / 100;
						if (rd >= 0.0 && rd <= 1.0) {
							value = total * rd;
						}
					} else {
						Logger.debug("计算prepareOrderInstance折扣时该购物车里面没有符合规则的商品");
					}
				}
			}
			if (value > 0) {
				return -value;
			}
		} catch (Exception e) {
			Logger.error("coupon:{} use failed", code, e);
		}
		return null;
	}

	@Override
	public LoyaltyPrefer applyPromo(String email, List<CartItem> cartItems,
			String code, WebContext webContext) {
		LoyaltyPrefer loyaltyPrefer = new LoyaltyPrefer();
		ConponActionRule actionRule = this.getActionRuleForPormoCode(code);
		if (null != actionRule) {
			CouponRuleActionParameter rule = actionRule.getParameter();
			if (null != rule) {
				LoyaltyPrefer ismatch = this.matchRule(cartItems, rule,
						webContext);
				if (null != ismatch && ismatch.isSuccess()) {
					List<CartItem> validCartItems = getValidItem(cartItems,
							rule);
					if (null != validCartItems && validCartItems.size() > 0) {
						Double value = calculatePrefer(validCartItems, rule,
								webContext);
						loyaltyPrefer.setIsSuccess(true);
						loyaltyPrefer.setCode(code);
						loyaltyPrefer.setValue(value);
						loyaltyPrefer.setPreferType(LOYALTY_TYPE_PROMO);
						return loyaltyPrefer;
					}
				} else {
					loyaltyPrefer.setCode(code);
					loyaltyPrefer.setErrorMessage(ismatch.getErrorMessage());
					return loyaltyPrefer;
				}
			}
		} else {
			loyaltyPrefer.setCode(code);
			loyaltyPrefer.setErrorMessage(play.i18n.Messages
					.get("loyalty.errorMessage.noRule"));
			return loyaltyPrefer;
		}
		return loyaltyPrefer;
	}

	@Override
	public LoyaltyPrefer applyCouponToDB(String email,
			List<CartItem> cartItems, String code, WebContext webContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoyaltyPrefer applyPromoToDB(String email, List<CartItem> cartItems,
			String code, WebContext webContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoyaltyPrefer getCurrentPrefer(String email,
			List<CartItem> cartItems, WebContext webContext) {
		LoyaltyPrefer loyaltyPrefer = new LoyaltyPrefer();
		Context ctx = Context.current();
		if (ctx.request().cookie(LOYALTY_PREFER) != null) {

			String loyaltycookie = ctx.request().cookie(LOYALTY_PREFER).value();
			String[] loyaltyArray = org.apache.commons.lang3.StringUtils.split(
					loyaltycookie, ":");
			if (loyaltyArray.length != 2) {
				return loyaltyPrefer;
			} else {
				String loyaltyType = loyaltyArray[0];
				String loyaltyCode = loyaltyArray[1];
				if (loyaltyType == LOYALTY_TYPE_PROMO) {
					return this.applyCoupon(email, cartItems, loyaltyCode,
							webContext);
				} else if (loyaltyType == LOYALTY_TYPE_COUPON) {
					return this.applyPromo(email, cartItems, loyaltyCode,
							webContext);
				}
			}

		} else {
			// 从数据库中获取优惠信息,稍后完善
		}
		return loyaltyPrefer;
	}

	@Override
	public List<LoyaltyCoupon> getMyUsableCoupon(String email,
			List<CartItem> cartItems, WebContext webCtx) {
		// List<Coupon> coupons =
		List<Coupon> coupons = this.selectMyCouponUnused(email);
		List<Integer> codeIds = new ArrayList<Integer>();
		// Cart cart = cartLifecycle.getCart(cartId);
		FluentIterable.from(coupons).forEach(c -> codeIds.add(c.getCodeId()));

		List<ConponActionRule> actionRules = this.getActionRule(codeIds);

		// 开始过滤满足rule的优惠券
		// CampaignContext context = ctxFactory.createContext(null, null);
		// context.setActionOn(cart);
		// context.setWebContext(webCtx);

		// 获取当前上下文币种
		String userCurrency = (webCtx == null ? fservice.getCurrency()
				: fservice.getCurrency(webCtx));
		Currency currency = currencyService.getCurrencyByCode(userCurrency);
		// 过滤出满足条件的优惠券
		FluentIterable<ConponActionRule> usableRule = FluentIterable.from(
				actionRules).filter(
				c -> this.match(cartItems, c.getParameter(), webCtx));
		List<LoyaltyCoupon> result = Lists.newLinkedList();

		FluentIterable.from(usableRule.toList()).forEach(
				c -> {
					LoyaltyCoupon coupon = new LoyaltyCoupon();
					Double amount = c.getParameter().getFcouponamount();
					coupon.setCode(c.getParameter().getCode());
					// 设置开始结束日期
					if (c.getParameter().getTimeType() != null) {
						switch (c.getParameter().getTimeType()) {
						case VALIDITY:
							// 优惠券生成日期=有效日期的开始时间
							Date start = c.getParameter().getDcreatedate();
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(start);
							Integer validity = c.getParameter().getIvalidity();
							calendar.add(calendar.DATE, validity);
							// 结束时间
							Date end = calendar.getTime();
							coupon.setStartDate((start.getYear()+1900)+"."+(start.getMonth()+1)+"."+start.getDate());
							coupon.setEndDate(calendar.get(Calendar.YEAR)+"."+(calendar.get(Calendar.MONTH)+1)+"."+calendar.get(Calendar.DATE));
							break;

						case DATE:
							// 开始时间
							Date startDate = c.getParameter().getStartdate();
							Date endDate = c.getParameter().getEnddate();
							coupon.setStartDate((startDate.getYear()+1900)+"."+(startDate.getMonth()+1)+"."+startDate.getDate());
							coupon.setEndDate((endDate.getYear()+1900)+"."+(endDate.getMonth()+1)+"."+endDate.getDate());
							break;
						}
					}
					
					
					if (CouponRuleBack.CouponType.CASH == c.getParameter()
							.getType()) {
						coupon.setCouponType("cash");

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
								// 对特殊货币做处理
								String couponValue = Utils.money(amount,
										userCurrency).replaceAll(",", "");
								amount = Double.valueOf(couponValue);
							} catch (Exception e) {
								Logger.error("exchange currency failed", e);
							}
						}
						coupon.setValue(amount);
						coupon.setUnit(userCurrency);
						coupon.setSpendLimitValue(c.getParameter()
								.getForderamountlimit());
						Integer currencyId = c.getParameter().getCcurrency();
						if (null != currencyId) {
							coupon.setSpendLimitCurrency(currencyService
									.getCurrencyById(currencyId).getCsymbol());
						}
					} else {
						double discount = c.getParameter().getFdiscount();
						coupon.setCouponType("discount");
						coupon.setValue(discount * 0.01);
						coupon.setUnit("OFF");
						coupon.setSpendLimitValue(c.getParameter()
								.getForderamountlimit());
						Integer currencyId = c.getParameter().getCcurrency();
						if (null != currencyId) {
							coupon.setSpendLimitCurrency(currencyService
									.getCurrencyById(currencyId).getCsymbol());
						}
					}
					result.add(coupon);
				});
		return result;
	}

	@Override
	public boolean saveCouponOrderPrefer(String email,
			LoyaltyPrefer loyaltyPrefer, WebContext webCtx) {
		boolean result = false;
		String code = loyaltyPrefer.getCode();
		if (code != null) {
			CartCoupon cartCoupon = new CartCoupon();
			// int orderId = order.getIid();
			String orderNumber = loyaltyPrefer.getOrder().getCordernumber();
			int orderId = loyaltyPrefer.getOrder().getIid();
			cartCoupon.setCcode(code);
			if (!StringUtils.isEmpty(orderNumber)) {
				cartCoupon.setOrderNumber(orderNumber);
			}
			cartCoupon.setOrderId(orderId);
			// 数字2代表已使用,新流程取消从前的锁定状态,调用此方法后coupon直接设置为已使用状态
			cartCoupon.setIstatus(2);
			cartCoupon.setCemail(email);
			result = ccService.addOrderCoupon(cartCoupon);
		}
		return result;
	}

	@Override
	public boolean savePromoOrderPrefer(String email,
			LoyaltyPrefer loyaltyPrefer, WebContext webCtx) {
		boolean result = false;

		// 向t_member_coupon表保存一条使用记录
		Coupon coupon = new Coupon();
		String code = loyaltyPrefer.getCode();
		PromoCode promo = promoCodeService.selectPromoCodeByCode(code);
		coupon.setWebsiteId(promo.getWebsiteId());
		coupon.setEmail(email);
		coupon.setRuleId(promo.getRuleId());
		coupon.setCodeId(promo.getId());
		coupon.setType(enums.loyalty.coupon.manager.Type.PROMO_CODE.getCode());
		coupon.setStatus(enums.loyalty.coupon.manager.Status.USED.getCode());
		int couponResult = this.add(coupon);

		OrderCoupon promoOrder = new OrderCoupon();
		int orderId = loyaltyPrefer.getOrder().getIid();
		String orderNumber = loyaltyPrefer.getOrder().getCordernumber();
		promoOrder.setCcode(code);
		promoOrder.setIorderid(orderId);
		promoOrder.setCordernumber(orderNumber);
		promoOrder.setCemail(email);
		promoOrder.setIstatus(1);

		if (orderCouponMapper.insert(promoOrder) == 1 && (couponResult == 1)) {
			result = true;
			return result;
		}

		return result;
	}

	@Override
	public Coupon createCouponByRule(String email, Integer ruleId,
			WebContext webCtx) {
		if (StringUtils.isEmpty(email) || null == ruleId) {
			throw new NullPointerException("email or ruleId is empty");
		}
		Coupon coupon = new Coupon();
		coupon.setCreator(0);
		coupon.setEmail(email);
		coupon.setRuleId(ruleId);
		int siteid = fservice.getSiteID(webCtx);
		coupon.setWebsiteId(siteid);
		coupon.setStatus(enums.loyalty.coupon.manager.Status.SEND.getCode());
		int codeId = codeService.getCodeIdByRuleId(coupon.getRuleId(), false,
				fservice.getSiteID(webCtx), 0);
		coupon.setCodeId(codeId);
		if (codeId == 0) {
			throw new NullPointerException(
					"This rule no longer exists or no publish");
		}
		String code = couponCodeService.getCodeById(codeId);
		coupon.setCode(code);

		int result = this.add(coupon);
		return result > 0 ? coupon : null;
	}

}
