package extensions.loyalty.campaign.coupon;

import java.util.List;

import javax.inject.Inject;

import play.Logger;
import services.campaign.ActionRules;
import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignSupport;
import services.campaign.IAction;
import services.campaign.IActionParameter;
import services.campaign.IActionRule;
import services.campaign.ICampaignInstance;
import services.loyalty.coupon.ICouponMainService;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import extensions.InjectorInstance;
import extensions.loyalty.campaign.rule.memberactive.ConponActionRule;
import facades.cart.Cart;

/**
 * 优惠券使用活动
 * 
 * @author lijun
 *
 */
public class CouponUseCampaign extends CampaignSupport {
	private static final String ID = CouponUseAction.ID;

	@Inject
	CampaignContextFactory ctxFactory;

	@Inject
	ICouponMainService couponService;

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public Class<?> getPayloadClass() {
		return CouponUseEvent.class;
	}

	@Override
	public CampaignContext createCampaignContext(Object payload,
			ICampaignInstance instance) {
		CouponUseEvent event = (CouponUseEvent) payload;
		Cart cart = event.getCart();
		CampaignContext context = ctxFactory.createContext(payload, instance);
		context.setActionOn(cart);
		context.setWebContext(event.getWebContext());
		return context;
	}

	@Override
	public ICampaignInstance createCampaignInstance() {
		return null;
	}

	@Override
	public List<ICampaignInstance> getActiveInstances(Object payload) {
		CouponUseEvent event = (CouponUseEvent) payload;
		String code = event.getCode();
		// 把CouponRule转换成ConponActionRule
		ConponActionRule actionRule = this.couponService.getActionRule(code);
		if (actionRule == null) {
			Logger.error("获取不到优惠券,所以不能使用该优惠券:{}", code);
			return null;
		}

		CouponUseCampaignInstance instance = InjectorInstance.getInjector()
				.getInstance(CouponUseCampaignInstance.class);

		List<String> actionIds = instance.getActionIDs();
		List<IAction> actions = super.getActions(actionIds);
		instance.setActions(actions);
		instance.setCampaign(this);

		List<IActionRule> arl = Lists.newArrayList(actionRule);
		ActionRules ars = new ActionRules(arl,
				services.campaign.MultiRules.Match.MATCH_ALL);
		instance.setActionRules(ars);

		List<IActionParameter> actionParams = Lists.newArrayList(actionRule
				.getParameter());
		instance.setActionParams(actionParams);

		return Lists.newArrayList(instance);
	}

	@Override
	public Optional<ICampaignInstance> getInstance(String instanceId) {
		return null;
	}

	@Override
	protected List<String> getPossibleActionRuleIDs() {
		return null;
	}

	@Override
	protected List<String> getPossibleActionIDs() {
		return null;
	}

}
