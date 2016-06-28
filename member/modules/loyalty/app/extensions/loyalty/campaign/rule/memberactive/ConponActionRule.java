package extensions.loyalty.campaign.rule.memberactive;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.FluentIterable;

import play.Logger;
import scala.Tuple2;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import services.campaign.CampaignContext;
import services.campaign.IActionRule;
import services.campaign.IActionRuleParameter;
import services.product.ProductEnquiryService;
import services.product.ProductSalePriceService;
import valueobjects.order_api.cart.CartItem;
import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import context.WebContext;
import dto.Currency;
import dto.product.ProductCategoryMapper;
import dto.product.ProductLabel;
import extensions.loyalty.campaign.coupon.PromoCodeUseAction;
import extensions.order.OrderExtrasProviderSupport;
import facades.cart.Cart;

/**
 * 
 * @author lijun
 *
 */
public class ConponActionRule implements IActionRule {
	public static final String ID = "conpon-rule-";
	// 数据库id
	private int ruleId;
	// 参数
	CouponRuleActionParameter parameter;

	@Inject
	ProductEnquiryService pService;

	@Inject
	CurrencyService currencyService;

	@Inject
	FoundationService fservice;

	@Inject
	ProductSalePriceService pspService;

	private Class<? extends OrderExtrasProviderSupport> orderExtrasProvider;

	public ConponActionRule() {

	}

	public void setOrderExtrasProvider(
			Class<? extends OrderExtrasProviderSupport> orderExtrasProvider) {
		this.orderExtrasProvider = orderExtrasProvider;
	}

	public ConponActionRule(int ruleId) {
		this.ruleId = ruleId;
	}

	public CouponRuleActionParameter getParameter() {
		return parameter;
	}

	public void setParameter(CouponRuleActionParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public String getId() {
		return ID + this.ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	@Override
	public boolean match(CampaignContext context, IActionRuleParameter param) {
		
		if (param != null) {
			this.parameter = (CouponRuleActionParameter) param;
		}
		// 终端类型做判断
		WebContext terminalWebcontext = context.getWebContext();
		if (null == terminalWebcontext) {
			Logger.error("Unable to get the terminal type from context!");
			return false;
		}
		String terminal = fservice.getDevice(terminalWebcontext);
		if (services.base.utils.StringUtils.isEmpty(terminal)) {
			Logger.error("Unable to get the terminal type!");
			return false;
		}
		if (!(parameter.getUseTerminal() != null && parameter.getUseTerminal()
				.size() > 0)) {
			Logger.error("Coupons available terminal type is empty!");
			return false;
		}
		if (!(parameter.getUseTerminal().contains(terminal))) {
			Logger.error("Coupons terminal type does not match!");
			return false;
		}
		Cart cart = (Cart) context.getActionOn();
		// 总金额金额
		double totalAmount = 0;

		Date now = new Date();
		// 最低消费金额
		Double limitAmount = parameter.getForderamountlimit();
		// 把最低消费金额转换成用户所在国家的金额
		if (parameter.getTimeType() != null) {

			switch (parameter.getTimeType()) {
			case VALIDITY:
				// 优惠券生成日期=有效日期的开始时间
				Date start = parameter.getDcreatedate();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(start);
				Integer validity = parameter.getIvalidity();
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
				Date startDate = parameter.getStartdate();
				Date endDate = parameter.getEnddate();
				if (endDate.getTime() < now.getTime()
						|| now.getTime() < startDate.getTime()) {
					Logger.error("有效期不满足规则");
					return false;
				}
				break;
			}
		}
		// 查看购物车里的商品是否在排除商品类型里
		List<CartItem> cartItems = cart.getAllItems();
		List<CartItem> validProduct = getValidItem(cartItems, this.parameter);
		if (validProduct.size() == 0) {
			Logger.error("购物车内没有满足规则的商品了,所以不能使用该优惠了");
			return false;
		} else {
			// 计算最低消费金额
			totalAmount = this.getTotalPrice(validProduct);
			// limitAmount不为null则代表有最小消费金额限制
			if (limitAmount != null) {
				// 把面值转换成用户所在国家币值
				String userCurrency = fservice.getCurrency();
				if (StringUtils.isEmpty(userCurrency)) {
					Logger.error("获取的用户币种为空,优惠券不能使用");
					return false;
				}
				try {
					int currentCurrencyId = parameter.getCcurrency();
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

	@Override
	public ICodec<IActionRuleParameter, JsonNode> getParameterCodec() {
		return null;
	}

}
