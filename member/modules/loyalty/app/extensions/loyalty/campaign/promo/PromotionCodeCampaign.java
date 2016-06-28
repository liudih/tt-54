package extensions.loyalty.campaign.promo;

import java.util.List;

import javax.inject.Inject;

import mapper.loyalty.promo.PromotionCodeMapper;
import play.Logger;
import play.libs.F.Tuple4;
import services.campaign.ActionRepository;
import services.campaign.ActionRuleRepository;
import services.campaign.ActionRules;
import services.campaign.ActionRulesParameter;
import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignSupport;
import services.campaign.IAction;
import services.campaign.IActionParameter;
import services.campaign.IActionRule;
import services.campaign.ICampaignInstance;
import services.campaign.MultiRules.Match;
import services.loyalty.coupon.ICouponMainService;
import services.loyalty.coupon.IPromoCodeService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import entity.loyalty.promo.PromotionCode;
import extensions.InjectorInstance;
import extensions.loyalty.campaign.action.discount.DiscountType;
import extensions.loyalty.campaign.action.discount.DiscountUnit;
import extensions.loyalty.campaign.action.discount.SimpleDiscountAction;
import extensions.loyalty.campaign.action.discount.SimpleDiscountActionParameter;
import extensions.loyalty.campaign.coupon.PromoCodeUseAction;
import extensions.loyalty.campaign.rule.memberactive.ConponActionRule;
import extensions.loyalty.campaign.rule.totalexceed.TotalExceedActionRule;
import extensions.loyalty.campaign.rule.totalexceed.TotalExceedActionRuleParameter;

public class PromotionCodeCampaign extends CampaignSupport {

	@Inject
	CampaignContextFactory ctxFactory;

	@Inject
	ActionRuleRepository ruleRepo;

	@Inject
	ActionRepository actionRepo;

	@Inject
	PromotionCodeMapper promoCodeMapper;

	@Inject
	IPromoCodeService promoCodeService;
	
	@Inject
	ICouponMainService couponService;
	
	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String getId() {
		return "promo-code";
	}

	@Override
	public Class<?> getPayloadClass() {
		return PromotionCodeUsage.class;
	}

	@Override
	public CampaignContext createCampaignContext(Object payload,
			ICampaignInstance instance) {
		PromotionCodeUsage promo = (PromotionCodeUsage) payload;
		CampaignContext ctx = ctxFactory.createContext(payload, instance);
		ctx.setWebContext(promo.getWebContext());
		ctx.setActionOn(promo.getTarget());
		return ctx;
	}

	@Override
	public ICampaignInstance createCampaignInstance() {
		return new PromotionCodeCampaignInstance();
	}

	@Override
	public List<ICampaignInstance> getActiveInstances(Object payload) {
		PromotionCodeUsage promo = (PromotionCodeUsage) payload;
		// PromotionCode entity = promoCodeMapper.findPromotionCode(promo
		// .getCode());
		// Logger.debug("Entity: {}", entity);
		// if (entity != null) {
		// try {
		// // return Lists.newArrayList(mockInstance());
		// return Lists.newArrayList(convert(entity));
		// } catch (Exception e) {
		// Logger.error("Exception converting entity", e);
		// }
		// }
		// modify by lijun
		
			ConponActionRule actionRule = this.couponService.getActionRuleForPormoCode(promo
					.getCode());
			
			if (actionRule != null) {
				PromotionCodeCampaignInstance ci = new PromotionCodeCampaignInstance();
				ci.setCampaign(this);
				ci.setInstanceId(this.getId() + ":" + promo
						.getCode());

				PromoCodeUseAction action = InjectorInstance.getInjector()
						.getInstance(PromoCodeUseAction.class);

				List<IActionRule> arl = Lists.newArrayList(actionRule);
				ActionRules ars = new ActionRules(arl,
						services.campaign.MultiRules.Match.MATCH_ALL);
				ci.setActionRules(ars);
				// 设置action参数
				action.setPara(actionRule.getParameter());

				ci.setActions(Lists.newArrayList(action));

				return Lists.newArrayList(ci);
			}
		return Lists.newArrayList();
	}

	@Override
	public Optional<ICampaignInstance> getInstance(String instanceId) {
		return Optional.of(mockInstance());
	}

	private ICampaignInstance convert(PromotionCode entity) throws Exception {
		PromotionCodeCampaignInstance ci = new PromotionCodeCampaignInstance();
		ci.setCampaign(this);
		ci.setInstanceId(this.getId() + ":" + entity.getCcode());
		Tuple4<ActionRules, ActionRulesParameter, List<IAction>, List<IActionParameter>> parsedResult = convert(
				entity.getCrules(), entity.getCruleparams(),
				entity.getCactions(), Match.MATCH_ALL,
				entity.getCactionparams());
		ci.setActionRules(parsedResult._1);
		ci.setActionRuleParams(parsedResult._2 != null ? parsedResult._2
				.getParameters() : null);
		ci.setActions(parsedResult._3);
		ci.setActionParams(parsedResult._4);
		return ci;
	}

	protected List<String> getPossibleActionRuleIDs() {
		return Lists.newArrayList(TotalExceedActionRule.ID);
	}

	protected List<String> getPossibleActionIDs() {
		return Lists.newArrayList(SimpleDiscountAction.ID);
	}

	private ICampaignInstance mockInstance() {
		// XXX hard code
		PromotionCodeCampaignInstance ci = new PromotionCodeCampaignInstance();
		ci.setCampaign(this);
		ci.setInstanceId("hard-code-instance-id");

		IActionRule rule = ruleRepo.findRule("total-exceed");
		TotalExceedActionRuleParameter rp = new TotalExceedActionRuleParameter();
		rp.setCurrency("USD");
		rp.setAmount(50);

		ci.setActionRules(new ActionRules(Lists.newArrayList(rule),
				Match.MATCH_ALL));
		ci.setActionRuleParams(Lists.newArrayList(rp));

		IAction action = actionRepo.findAction("simple-discount");
		ci.getActions().add(action);
		SimpleDiscountActionParameter p = new SimpleDiscountActionParameter();
		p.setType(DiscountType.SKU_INCLUSIVE);
		p.setUnit(DiscountUnit.PERCENT);
		p.setIncludeSku(Lists.newArrayList("F1028GR", "HP001"));
		p.setValue(0.50);
		ci.getActionParams().add(p);

		JsonNode r = rule.getParameterCodec().encode(rp);
		Logger.debug("JSON rule encoded: {}", r);
		Logger.debug("JSON rule decoded: {}", rule.getParameterCodec()
				.decode(r));

		JsonNode n = action.getParameterCodec().encode(p);
		Logger.debug("JSON encoded: {}", n);
		Logger.debug("JSON decoded: {}", action.getParameterCodec().decode(n));

		JsonNode rs = ci.getActionRules().getParameterCodec()
				.encode(new ActionRulesParameter(Lists.newArrayList(rp)));
		Logger.debug("Rules encoded: {}", rs);
		Logger.debug("Rules decoded: {}", ci.getActionRules()
				.getParameterCodec().decode(rs));

		return ci;
	}
}
