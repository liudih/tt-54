package extensions.loyalty;

import handlers.loyalty.CampaignEventHandler;
import handlers.loyalty.EmailActivateHandler;
import handlers.loyalty.GiftCouponHandler;
import handlers.loyalty.OrderExtrasHandle;
import handlers.loyalty.SubscribeHandler;

import java.util.List;

import javax.inject.Singleton;

import mapper.loyalty.ActivityMapper;
import mapper.loyalty.BulkMapper;
import mapper.loyalty.BulkRateMapper;
import mapper.loyalty.CartCouponMapper;
import mapper.loyalty.CollectingCustomerShareMapper;
import mapper.loyalty.CouponBaseMapper;
import mapper.loyalty.CouponCodeMapper;
import mapper.loyalty.CouponMapper;
import mapper.loyalty.CouponMemberMapper;
import mapper.loyalty.CouponRuleMapper;
import mapper.loyalty.CouponTypeMapper;
import mapper.loyalty.IntegralBehaviorMapper;
import mapper.loyalty.IntegralUseRuleMapper;
import mapper.loyalty.MemberEdmMapper;
import mapper.loyalty.MemberIntegralHistoryMapper;
import mapper.loyalty.MemberPointMapper;
import mapper.loyalty.OrderCouponMapper;
import mapper.loyalty.OrderPointsMapper;
import mapper.loyalty.ThemeCssMapper;
import mapper.loyalty.ThemeGroupMapper;
import mapper.loyalty.ThemeMapper;
import mapper.loyalty.ThemeGroupNameMapper;
import mapper.loyalty.ThemeSkuRelationMapper;
import mapper.loyalty.ThemeTitleMapper;
import mapper.loyalty.promo.PromotionCodeMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import services.base.template.ITemplateFragmentProvider;
import services.campaign.IAction;
import services.campaign.IActionRule;
import services.campaign.ICampaign;
import services.cart.ICartFragmentPlugin;
import services.cart.IHandleCartRefreshEventPlugin;
import services.cart.SimpleCartFragmentPlugin;
import services.loyalty.IPointsService;
import services.loyalty.IPreferService;
import services.loyalty.LoyaltyForProviderService;
import services.loyalty.LoyaltyOrderCall;
import services.loyalty.PointsService;
import services.loyalty.award.CouponAwardProvider;
import services.loyalty.award.IAwardProvider;
import services.loyalty.award.PointsAwardProvider;
import services.loyalty.coupon.ICartCouponService;
import services.loyalty.coupon.ICouponMainService;
import services.loyalty.coupon.ICouponService;
import services.loyalty.coupon.IPromoCodeService;
import services.loyalty.coupon.impl.CartCouponService;
import services.loyalty.coupon.impl.CouponService;
import services.loyalty.coupon.impl.PromoCodeService;
import services.loyalty.fragment.Subscribe2Fragment;
import services.loyalty.fragment.SubscribeFragment;
import services.loyalty.fragment.provider.ThemeGroupFragmentProvider;
import services.loyalty.fragment.renderer.ThemeGroupFragmentRenderer;
import services.loyalty.price.BulkDiscountProvider;
import services.loyalty.price.MemberGroupPriceContextProvider;
import services.loyalty.theme.IThemeCssService;
import services.loyalty.theme.IThemeGroupNameService;
import services.loyalty.theme.IThemeGroupService;
import services.loyalty.theme.IThemeService;
import services.loyalty.theme.IThemeSkuRelationService;
import services.loyalty.theme.IThemeTitleService;
import services.loyalty.theme.impl.ThemeCssService;
import services.loyalty.theme.impl.ThemeGroupNameService;
import services.loyalty.theme.impl.ThemeGroupService;
import services.loyalty.theme.impl.ThemeServiceImpl;
import services.loyalty.theme.impl.ThemeSkuRelationServiceImpl;
import services.loyalty.theme.impl.ThemeTitleServiceImpl;
import services.loyalty.theme.IThemeFragmentPlugin;
import services.loyalty.theme.SimpleThemeFragmentPlugin;
import services.price.IDiscountProvider;
import services.price.IPriceCalculationContextProvider;
import services.price.IPriceProvider;
import valueobjects.order_api.cart.IExtraLineRule;

import com.google.common.eventbus.EventBus;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import dao.loyalty.coupon.ICartCouponDao;
import dao.loyalty.coupon.ICouponDao;
import dao.loyalty.coupon.IPromoCodeDao;
import dao.loyalty.impl.coupon.CartCouponDao;
import dao.loyalty.impl.coupon.CouponDao;
import dao.loyalty.impl.coupon.PromoCodeDao;
import dao.loyalty.theme.IThemeCssDao;
import dao.loyalty.theme.IThemeDao;
import dao.loyalty.theme.IThemeGroupNameDao;
import dao.loyalty.theme.impl.ThemeCssDao;
import dao.loyalty.theme.impl.ThemeDaoImpl;
import dao.loyalty.theme.impl.ThemeGroupDao;
import dao.loyalty.theme.impl.ThemeGroupNameDao;
import dao.loyalty.theme.IThemeGroupDao;
import dao.loyalty.theme.IThemeSkuRelationDao;
import dao.loyalty.theme.IThemeTitleDao;
import dao.loyalty.theme.impl.ThemeSkuRelationDaoImpl;
import dao.loyalty.theme.impl.ThemeTitleDaoImpl;
import extension.point.IExtensionSignin;
import extension.point.ISigninProvider;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.template.ITemplateExtension;
import extensions.cart.ICartFragmentExtension;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.loyalty.campaign.EDMSubscriptionCampaign;
import extensions.loyalty.campaign.action.discount.SimpleDiscountAction;
import extensions.loyalty.campaign.action.point.GrantPointAction;
import extensions.loyalty.campaign.coupon.CouponUseAction;
import extensions.loyalty.campaign.coupon.CouponUseCampaign;
import extensions.loyalty.campaign.coupon.PromoCodeUseAction;
import extensions.loyalty.campaign.login.LoginEverydayCampaign;
import extensions.loyalty.campaign.orderpayment.OrderPaymentCampaign;
import extensions.loyalty.campaign.photo.PhotoCampaign;
import extensions.loyalty.campaign.promo.PromotionCodeCampaign;
import extensions.loyalty.campaign.review.ReviewCampaign;
import extensions.loyalty.campaign.rule.cart.CouponCodeExtraLineRule;
import extensions.loyalty.campaign.rule.cart.CouponUseExtraLineRule;
import extensions.loyalty.campaign.rule.firstloginperday.FirstLoginPerDayActionRule;
import extensions.loyalty.campaign.rule.review.ReviewActionRule;
import extensions.loyalty.campaign.rule.signin.SigninActionRule;
import extensions.loyalty.campaign.rule.totalexceed.TotalExceedActionRule;
import extensions.loyalty.campaign.signin.SigninCampaign;
import extensions.loyalty.campaign.signin.SigninPointProvider;
import extensions.loyalty.campaign.signup.ActivationMemberCampaign;
import extensions.loyalty.campaign.signup.ActivationMemberGiftCouponAction;
import extensions.loyalty.campaign.signup.SignupCampaign;
import extensions.loyalty.campaign.subscription.EDMSubscriptionGiftConponAction;
import extensions.loyalty.campaign.video.VideoCampaign;
import extensions.loyalty.cart.BulkPriceFragmentProvider;
import extensions.loyalty.cart.BulkPriceFragmentRenderer;
import extensions.loyalty.member.MemberCouponStatisticFragment;
import extensions.loyalty.member.MemberLevelProfileFragment;
import extensions.loyalty.member.MemberLoyaltyMenuProvider;
import extensions.loyalty.member.MemberPointsStatisticFragment;
import extensions.loyalty.member.MyCouponMenuProvider;
import extensions.loyalty.menu.PointMenuProvider;
import extensions.loyalty.orderxtras.CouponExtrasProvider;
import extensions.loyalty.orderxtras.CouponUseExtrasProvider;
import extensions.loyalty.orderxtras.PointsExtrasProvider;
import extensions.loyalty.orderxtras.SimpleDiscountExtrasProvider;
import extensions.loyalty.subscribe.SubscribeBoxProvider;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.IMemberAccountMenuProvider;
import extensions.member.account.IMemberQuickMenuProvider;
import extensions.order.IOrderExtrasExtension;
import extensions.order.IOrderExtrasProvider;
import extensions.order.IOrderLoyaltyProvider;
import extensions.order.IPreferProvider;
import extensions.product.IPriceExtension;
import extensions.product.subscribe.ISubscribeExtension;
import extensions.product.subscribe.ISubscribeProvider;
import extensions.runtime.IApplication;

public class LoyaltyModule extends ModuleSupport implements MyBatisExtension,
		IEventExtension, IOrderExtrasExtension, IPriceExtension,IThemeFragmentExtension,
		IMemberAccountExtension, ITemplateExtension, IloyaltyExtension,
		ISubscribeExtension, ICartFragmentExtension, ICampaignExtension,
		IExtensionSignin, HessianServiceExtension {

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("loyalty", IntegralBehaviorMapper.class);
		service.addMapperClass("loyalty", MemberIntegralHistoryMapper.class);
		service.addMapperClass("loyalty", CouponBaseMapper.class);
		service.addMapperClass("loyalty", OrderCouponMapper.class);
		service.addMapperClass("loyalty", OrderPointsMapper.class);
		service.addMapperClass("loyalty", IntegralUseRuleMapper.class);
		service.addMapperClass("loyalty", ActivityMapper.class);
		service.addMapperClass("loyalty", MemberEdmMapper.class);
		service.addMapperClass("loyalty", BulkRateMapper.class);
		service.addMapperClass("loyalty", BulkMapper.class);
		service.addMapperClass("loyalty", PromotionCodeMapper.class);
		service.addMapperClass("loyalty", CouponRuleMapper.class);
		service.addMapperClass("loyalty", CouponMapper.class);
		service.addMapperClass("loyalty", CouponTypeMapper.class);
		service.addMapperClass("loyalty", CouponCodeMapper.class);
		service.addMapperClass("loyalty", CartCouponMapper.class);
		service.addMapperClass("loyalty", MemberPointMapper.class);
		service.addMapperClass("loyalty", CouponMemberMapper.class);
		service.addMapperClass("loyalty", CollectingCustomerShareMapper.class);
		service.addMapperClass("loyalty", ThemeMapper.class);
		service.addMapperClass("loyalty", ThemeGroupMapper.class);
		service.addMapperClass("loyalty", ThemeGroupNameMapper.class);
		service.addMapperClass("loyalty", ThemeTitleMapper.class);
		service.addMapperClass("loyalty", ThemeSkuRelationMapper.class);
		service.addMapperClass("loyalty", ThemeCssMapper.class);		
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(OrderExtrasHandle.class));
		eventBus.register(injector.getInstance(SubscribeHandler.class));
		eventBus.register(injector.getInstance(CampaignEventHandler.class));
		// add by lijun
		eventBus.register(injector.getInstance(EmailActivateHandler.class));
		eventBus.register(injector.getInstance(GiftCouponHandler.class));
	}

	@Override
	public void registerOrderExtrasProvider(
			Multibinder<IOrderExtrasProvider> extrasProvider) {
		extrasProvider.addBinding().to(CouponExtrasProvider.class);
		extrasProvider.addBinding().to(PointsExtrasProvider.class);
		extrasProvider.addBinding().to(SimpleDiscountExtrasProvider.class);
		// add by lijun
		extrasProvider.addBinding().to(CouponUseExtrasProvider.class);
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(SubscribeFragment.class);
		binder.addBinding().to(Subscribe2Fragment.class);
	}

	@Override
	public void registerPriceDiscountProviders(
			Multibinder<IPriceProvider> priceProviders,
			Multibinder<IDiscountProvider> discountProviders,
			Multibinder<IPriceCalculationContextProvider> contextProviders) {
		discountProviders.addBinding().to(BulkDiscountProvider.class)
				.in(Singleton.class);
		contextProviders.addBinding().to(MemberGroupPriceContextProvider.class);
	}

	@Override
	public void registerCartFragment(Multibinder<ICartFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleCartFragmentPlugin("bulk_price_info",
						BulkPriceFragmentProvider.class,
						BulkPriceFragmentRenderer.class));
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {

		final Multibinder<IAwardProvider> miProviders = Multibinder
				.newSetBinder(binder, IAwardProvider.class);
		for (IloyaltyExtension e : filterModules(modules,
				IloyaltyExtension.class)) {
			e.registerAwardFragment(miProviders);
		}

		final Multibinder<ICampaign> campaignBinder = Multibinder.newSetBinder(
				binder, ICampaign.class);
		final Multibinder<IActionRule> ruleBinder = Multibinder.newSetBinder(
				binder, IActionRule.class);
		final Multibinder<IAction> actionBinder = Multibinder.newSetBinder(
				binder, IAction.class);
		for (ICampaignExtension e : filterModules(modules,
				ICampaignExtension.class)) {
			e.registerCampaign(campaignBinder, ruleBinder, actionBinder);
		}
		// add by lijun
		binder.bind(ICouponDao.class).to(CouponDao.class);
		binder.bind(ICouponMainService.class).to(CouponService.class);
		binder.bind(ICartCouponService.class).to(CartCouponService.class);
		binder.bind(ICartCouponDao.class).to(CartCouponDao.class);
		// binder.bind(CouponServiceProvider.class).to(CouponService.class);
		Multibinder.newSetBinder(binder, IHandleCartRefreshEventPlugin.class)
				.addBinding().to(CouponService.class);
		Multibinder<IExtraLineRule> extraLineRules = Multibinder.newSetBinder(
				binder, IExtraLineRule.class);
		extraLineRules.addBinding().to(CouponUseExtraLineRule.class);
		extraLineRules.addBinding().to(CouponCodeExtraLineRule.class);

		binder.bind(IPromoCodeDao.class).to(PromoCodeDao.class);
		binder.bind(IPromoCodeService.class).to(PromoCodeService.class);
		Multibinder.newSetBinder(binder, IHandleCartRefreshEventPlugin.class)
				.addBinding().to(PromoCodeService.class);
		binder.bind(IPointsService.class).to(PointsService.class);
		binder.bind(IThemeService.class).to(ThemeServiceImpl.class);
		binder.bind(IThemeTitleService.class).to(ThemeTitleServiceImpl.class);
		binder.bind(IThemeDao.class).to(ThemeDaoImpl.class);
		binder.bind(IPreferProvider.class).to(LoyaltyOrderCall.class);
		binder.bind(IPreferService.class).to(LoyaltyOrderCall.class);
		// add by Guozy

		// add theme by Guozy
		binder.bind(IThemeTitleDao.class).to(ThemeTitleDaoImpl.class);
		binder.bind(IThemeCssService.class).to(ThemeCssService.class);
		binder.bind(IThemeCssDao.class).to(ThemeCssDao.class);
		binder.bind(IThemeGroupService.class).to(ThemeGroupService.class);
		binder.bind(IThemeGroupDao.class).to(ThemeGroupDao.class);
		binder.bind(IThemeGroupNameService.class).to(ThemeGroupNameService.class);
		binder.bind(IThemeGroupNameDao.class).to(ThemeGroupNameDao.class);

		binder.bind(IThemeSkuRelationService.class).to(ThemeSkuRelationServiceImpl.class);
		binder.bind(IThemeSkuRelationDao.class).to(ThemeSkuRelationDaoImpl.class);
		binder.bind(IOrderLoyaltyProvider.class).to(LoyaltyForProviderService.class);


		
		final Multibinder<IThemeFragmentPlugin> tfp = Multibinder
				.newSetBinder(binder, IThemeFragmentPlugin.class);
		for (IThemeFragmentExtension e : filterModules(modules,
				IThemeFragmentExtension.class)) {
			e.registerThemeFragment(tfp);
		}
	}

	@Override
	public void registerAwardFragment(Multibinder<IAwardProvider> providers) {
		providers.addBinding().to(CouponAwardProvider.class);
		providers.addBinding().to(PointsAwardProvider.class);
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountMenuProvider> menuProviders,
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders,
			Multibinder<IMemberQuickMenuProvider> quickMenuProvider) {
		menuProviders.addBinding().to(MemberLoyaltyMenuProvider.class);
		fragmentProviders.addBinding().to(MemberLevelProfileFragment.class);
		fragmentProviders.addBinding().to(MemberPointsStatisticFragment.class);
		fragmentProviders.addBinding().to(MemberCouponStatisticFragment.class);
		// add by lijun
		menuProviders.addBinding().to(MyCouponMenuProvider.class);
		menuProviders.addBinding().to(PointMenuProvider.class);

	}

	@Override
	public void registerSubscribeProvider(Multibinder<ISubscribeProvider> binder) {
		binder.addBinding().to(SubscribeBoxProvider.class);
	}

	@Override
	public void registerCampaign(Multibinder<ICampaign> binder,
			Multibinder<IActionRule> rules, Multibinder<IAction> actions) {
		// campaigns
		binder.addBinding().to(EDMSubscriptionCampaign.class);
		binder.addBinding().to(PromotionCodeCampaign.class);
		binder.addBinding().to(SignupCampaign.class);
		binder.addBinding().to(OrderPaymentCampaign.class);
		binder.addBinding().to(LoginEverydayCampaign.class);

		// add by lijun for git coupon
		binder.addBinding().to(ActivationMemberCampaign.class);
		binder.addBinding().to(CouponUseCampaign.class);

		binder.addBinding().to(ReviewCampaign.class);
		binder.addBinding().to(VideoCampaign.class);
		binder.addBinding().to(PhotoCampaign.class);
		binder.addBinding().to(SigninCampaign.class);

		// actions
		actions.addBinding().to(SimpleDiscountAction.class);
		actions.addBinding().to(GrantPointAction.class);
		actions.addBinding().to(PromoCodeUseAction.class);
		// add by lijun for git coupon
		actions.addBinding().to(ActivationMemberGiftCouponAction.class);
		actions.addBinding().to(EDMSubscriptionGiftConponAction.class);
		actions.addBinding().to(CouponUseAction.class);

		// rules
		rules.addBinding().to(TotalExceedActionRule.class);
		rules.addBinding().to(FirstLoginPerDayActionRule.class);
		rules.addBinding().to(ReviewActionRule.class);
		rules.addBinding().to(SigninActionRule.class);
	}

	@Override
	public void registerSignin(Multibinder<ISigninProvider> plugins) {
		// TODO Auto-generated method stub
		plugins.addBinding().to(SigninPointProvider.class);
	}

	@Override
	public void registerRemoteService(HessianRegistrar hr) {
		hr.publishService("point", IPointsService.class, PointsService.class);
		hr.publishService("loyalty-couponService", ICouponService.class,
				CouponService.class);
		
		hr.publishService("loyalty-ordercall", IPreferService.class,
				LoyaltyOrderCall.class);
		
		hr.publishService("loyalty_provider", IOrderLoyaltyProvider.class,
				LoyaltyForProviderService.class);
		
	}

	@Override
	public void registerThemeFragment(Multibinder<IThemeFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleThemeFragmentPlugin("theme-group",
						ThemeGroupFragmentProvider.class,
						ThemeGroupFragmentRenderer.class));
	}

}
