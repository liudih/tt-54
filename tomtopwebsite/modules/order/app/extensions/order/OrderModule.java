package extensions.order;

import handlers.order.OrderCancelledHandler;
import handlers.order.PaymentConfirmationHandler;
import handlers.order.ProductStatisticsHandler;
import handlers.order.RankProductHandler;
import handlers.order.RemindPaymentHandler;

import java.util.List;
import java.util.Set;

import mapper.order.BillDetailMapper;
import mapper.order.DetailMapper;
import mapper.order.DropShippingMapMapper;
import mapper.order.DropShippingMapper;
import mapper.order.DropShippingOrderDetailMapper;
import mapper.order.DropShippingOrderMapper;
import mapper.order.OrderAlterHistoryMapper;
import mapper.order.OrderCurrencyRateMapper;
import mapper.order.OrderMapper;
import mapper.order.OrderPackMapper;
import mapper.order.OrderPaymentMapper;
import mapper.order.OrderStatusMapper;
import mapper.order.OrderTagMapper;
import mapper.order.PaymentAccountMapper;
import mapper.order.PaymentCallbackMapper;
import mapper.order.PostEmailOrderMapper;
import mapper.order.PreparatoryDetailMapper;
import mapper.order.PreparatoryOrderMapper;
import mapper.order.ShippingMethodMapper;
import mapper.order.ShippingParameterMapper;
import mapper.order.StatusHistoryMapper;
import mapper.order.TotalOrderMapMapper;
import mapper.order.TotalOrderMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import org.apache.camel.builder.RouteBuilder;

import play.Logger;
import services.cart.CartCompositeEnquiry;
import services.cart.CartCompositeRenderer;
import services.cart.CartLifecycleService;
import services.cart.CartService;
import services.cart.CookieCartBuilderService;
import services.cart.CookieCartService;
import services.cart.CookieLaterService;
import services.cart.ExtraLineService;
import services.cart.ICartBuilderService;
import services.cart.ICartCompositeEnquiry;
import services.cart.ICartCompositeRenderer;
import services.cart.ICartLaterServices;
import services.cart.ICartLifecycleService;
import services.cart.ICartService;
import services.cart.ICartServices;
import services.cart.IExtraLineService;
import services.cart.IHandleCartRefreshEventPlugin;
import services.cart.IValidateCartCampaignService;
import services.cart.ValidateCartCampaignService;
import services.order.BillDetailService;
import services.order.CheckoutService;
import services.order.FreightService;
import services.order.IBillDetailService;
import services.order.ICheckoutService;
import services.order.IFreightService;
import services.order.IOrderAlterHistoryService;
import services.order.IOrderCountService;
import services.order.IOrderCurrencyRateService;
import services.order.IOrderDetailService;
import services.order.IOrderEnquiryService;
import services.order.IOrderFragmentPlugin;
import services.order.IOrderPackService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.IOrderTaggingService;
import services.order.IOrderUpdateService;
import services.order.IPreparatoryDetailService;
import services.order.IPreparatoryOrderService;
import services.order.IProductToOrderService;
import services.order.OrderAlterHistoryService;
import services.order.OrderCountService;
import services.order.OrderCurrencyRateService;
import services.order.OrderDetailService;
import services.order.OrderEnquiryService;
import services.order.OrderPackService;
import services.order.OrderService;
import services.order.OrderStatusService;
import services.order.OrderTaggingService;
import services.order.OrderUpdateService;
import services.order.PreparatoryDetailService;
import services.order.PreparatoryOrderService;
import services.order.ProductToOrderService;
import services.order.SimpleOrderFragmentPlugin;
import services.order.fragment.OrderHistoryFragmentProvider;
import services.order.fragment.OrderHistoryFragmentRender;
import services.order.fragment.pretreatment.AddressPretreatment;
import services.order.fragment.pretreatment.OrderPretreatment;
import services.order.fragment.provider.BillingAddressProvider;
import services.order.fragment.provider.OrderCartProductProvider;
import services.order.fragment.provider.OrderShippingAddressProvider;
import services.order.fragment.provider.OrderShippingMethodProvider;
import services.order.fragment.provider.OrderSummaryProvider;
import services.order.fragment.provider.PreparatoryOrderProvider;
import services.order.fragment.renderer.BillingAddressRenderer;
import services.order.fragment.renderer.OrderCartProductRenderer;
import services.order.fragment.renderer.OrderShippingAddressRenderer;
import services.order.fragment.renderer.OrderShippingMethodRenderer;
import services.order.fragment.renderer.OrderSummaryRenderer;
import services.order.fragment.renderer.PreparatoryOrderRenderer;
import services.order.payment.IPaymentCallbackService;
import services.order.payment.PaymentCallbackService;
import services.payment.IPaymentConfirmationService;
import services.payment.IPaymentService;
import services.payment.IWebPaymentService;
import services.payment.PaymentConfirmationService;
import services.payment.PaymentService;
import services.payment.WebPaymentService;
import services.product.IProductFragmentPlugin;
import services.product.SimpleProductFragmentPlugin;
import services.shipping.FillShippingMethod;
import services.shipping.IFillShippingMethod;
import services.shipping.IShippingMethodService;
import services.shipping.IShippingParameterService;
import services.shipping.PTFreeShipping;
import services.shipping.ProductWeightFilter;
import services.shipping.ShippingCountriesFilter;
import services.shipping.ShippingMethodService;
import services.shipping.ShippingParameterService;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import dao.order.IDropShippingEnquiryDao;
import dao.order.IDropShippingMapEnquiryDao;
import dao.order.IDropShippingMapUpdateDao;
import dao.order.IDropShippingOrderDetailEnquiryDao;
import dao.order.IDropShippingOrderDetailUpdateDao;
import dao.order.IDropShippingOrderEnquiryDao;
import dao.order.IDropShippingOrderUpdateDao;
import dao.order.IDropShippingUpdateDao;
import dao.order.IOrderAlterHistoryEnquiryDao;
import dao.order.IOrderAlterHistoryUpdateDao;
import dao.order.IOrderCurrencyRateEnquiryDao;
import dao.order.IOrderCurrencyRateUpdateDao;
import dao.order.IOrderDetailEnquiryDao;
import dao.order.IPaymentCallbackEnquiryDao;
import dao.order.IPaymentCallbackUpdateDao;
import dao.order.IPreparatoryDetailEnquiryDao;
import dao.order.IPreparatoryDetailUpdateDao;
import dao.order.IPreparatoryOrderEnquiryDao;
import dao.order.IPreparatoryOrderUpdateDao;
import dao.order.IShippingParameterDao;
import dao.order.ITotalOrderDao;
import dao.order.ITotalOrderMapDao;
import dao.order.impl.DropShippingEnquiryDao;
import dao.order.impl.DropShippingMapEnquiryDao;
import dao.order.impl.DropShippingMapUpdateDao;
import dao.order.impl.DropShippingOrderDetailEnquiryDao;
import dao.order.impl.DropShippingOrderDetailUpdateDao;
import dao.order.impl.DropShippingOrderEnquiryDao;
import dao.order.impl.DropShippingOrderUpdateDao;
import dao.order.impl.DropShippingUpdateDao;
import dao.order.impl.OrderAlterHistoryEnquiryDao;
import dao.order.impl.OrderAlterHistoryUpdateDao;
import dao.order.impl.OrderCurrencyRateEnquiryDao;
import dao.order.impl.OrderCurrencyRateUpdateDao;
import dao.order.impl.OrderDetailEnquiryDao;
import dao.order.impl.PaymentCallbackEnquiryDao;
import dao.order.impl.PaymentCallbackUpdateDao;
import dao.order.impl.PreparatoryDetailEnquiryDao;
import dao.order.impl.PreparatoryDetailUpdateDao;
import dao.order.impl.PreparatoryOrderEnquiryDao;
import dao.order.impl.PreparatoryOrderUpdateDao;
import dao.order.impl.ShippingParameterDao;
import dao.order.impl.TotalOrderDao;
import dao.order.impl.TotalOrderMapDao;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.camel.ICamelExtension;
import extensions.cart.CartModule;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.member.MemberModule;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.IMemberAccountMenuProvider;
import extensions.member.account.IMemberQuickMenuProvider;
import extensions.order.camel.TimerTriggerRouteBuilder;
import extensions.order.collect.ICollectExtension;
import extensions.order.collect.ICollectProvider;
import extensions.order.dropshipping.DropShipOrderMenuProvider;
import extensions.order.dropshipping.DropShippingMenuProvider;
import extensions.order.member.MemberOrderMenuProvider;
import extensions.order.member.MemberOrderQuickMenuProvider;
import extensions.order.member.MemberOrderStatistics;
import extensions.order.shipping.IFreightExtension;
import extensions.order.shipping.IFreightPlugin;
import extensions.payment.IPaymentExtension;
import extensions.payment.IPaymentHTMLPlugIn;
import extensions.payment.IPaymentHtmlExtension;
import extensions.payment.IPaymentProvider;
import extensions.product.IProductFragmentExtension;
import extensions.product.ProductModule;
import extensions.runtime.IApplication;

public class OrderModule extends ModuleSupport implements MyBatisExtension,
		IProductFragmentExtension, IOrderFragmentExtension, IEventExtension,
		IMemberAccountExtension, ICamelExtension, IFreightExtension,
		IPaymentHtmlExtension, IOrderDetailExtension, HessianServiceExtension {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(ProductModule.class, MemberModule.class,
				CartModule.class);
	}

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(IOrderAlterHistoryEnquiryDao.class).to(
						OrderAlterHistoryEnquiryDao.class);
				bind(IOrderAlterHistoryUpdateDao.class).to(
						OrderAlterHistoryUpdateDao.class);
				bind(IOrderDetailEnquiryDao.class).to(
						OrderDetailEnquiryDao.class);
				bind(IOrderCurrencyRateUpdateDao.class).to(
						OrderCurrencyRateUpdateDao.class);
				bind(IOrderCurrencyRateEnquiryDao.class).to(
						OrderCurrencyRateEnquiryDao.class);
				bind(IDropShippingOrderDetailUpdateDao.class).to(
						DropShippingOrderDetailUpdateDao.class);
				bind(IDropShippingOrderUpdateDao.class).to(
						DropShippingOrderUpdateDao.class);
				bind(IDropShippingMapUpdateDao.class).to(
						DropShippingMapUpdateDao.class);
				bind(IDropShippingMapEnquiryDao.class).to(
						DropShippingMapEnquiryDao.class);
				bind(IDropShippingOrderEnquiryDao.class).to(
						DropShippingOrderEnquiryDao.class);
				bind(IDropShippingOrderDetailEnquiryDao.class).to(
						DropShippingOrderDetailEnquiryDao.class);
				bind(IDropShippingUpdateDao.class).to(
						DropShippingUpdateDao.class);
				bind(IDropShippingEnquiryDao.class).to(
						DropShippingEnquiryDao.class);
				bind(IPaymentCallbackEnquiryDao.class).to(
						PaymentCallbackEnquiryDao.class);
				bind(IPaymentCallbackUpdateDao.class).to(
						PaymentCallbackUpdateDao.class);
				bind(IShippingParameterDao.class)
						.to(ShippingParameterDao.class);
				bind(IBillDetailService.class).to(BillDetailService.class);
				bind(IExtraLineService.class).to(ExtraLineService.class);
				bind(ICartService.class).to(CartService.class);
				bind(ICartLifecycleService.class)
						.to(CartLifecycleService.class);
				bind(ICartCompositeEnquiry.class)
						.to(CartCompositeEnquiry.class);
				bind(ICartCompositeRenderer.class).to(
						CartCompositeRenderer.class);
				bind(IOrderAlterHistoryService.class).to(
						OrderAlterHistoryService.class);
				bind(IOrderEnquiryService.class).to(OrderEnquiryService.class);
				bind(IOrderStatusService.class).to(OrderStatusService.class);
				bind(IShippingMethodService.class).to(
						ShippingMethodService.class);
				bind(IFreightService.class).to(FreightService.class);
				bind(IOrderCountService.class).to(OrderCountService.class);
				bind(IPaymentConfirmationService.class).to(
						PaymentConfirmationService.class);
				bind(IFillShippingMethod.class).to(FillShippingMethod.class);
				bind(IShippingParameterService.class).to(
						ShippingParameterService.class);
				bind(IOrderService.class).to(OrderService.class);
				bind(IOrderUpdateService.class).to(OrderUpdateService.class);
				bind(IOrderDetailService.class).to(OrderDetailService.class);
				bind(IOrderTaggingService.class).to(OrderTaggingService.class);
				bind(IOrderCurrencyRateService.class).to(
						OrderCurrencyRateService.class);
				bind(IProductToOrderService.class).to(
						ProductToOrderService.class);
				bind(IPreparatoryOrderUpdateDao.class).to(
						PreparatoryOrderUpdateDao.class);
				bind(IPreparatoryOrderEnquiryDao.class).to(
						PreparatoryOrderEnquiryDao.class);
				bind(IPreparatoryDetailUpdateDao.class).to(
						PreparatoryDetailUpdateDao.class);
				bind(IPreparatoryDetailEnquiryDao.class).to(
						PreparatoryDetailEnquiryDao.class);
				bind(IPreparatoryOrderService.class).to(
						PreparatoryOrderService.class);
				bind(IPreparatoryDetailService.class).to(
						PreparatoryDetailService.class);
				bind(IPaymentService.class).to(PaymentService.class);
				bind(IWebPaymentService.class).to(WebPaymentService.class);
				bind(ITotalOrderMapDao.class).to(TotalOrderMapDao.class);
				bind(ITotalOrderDao.class).to(TotalOrderDao.class);
				bind(IOrderPackService.class).to(OrderPackService.class);

				bind(ICartBuilderService.class).to(
						CookieCartBuilderService.class);
				bind(ICartServices.class).to(CookieCartService.class);
				bind(ICartLaterServices.class).to(CookieLaterService.class);
				bind(IPaymentCallbackService.class).to(
						PaymentCallbackService.class);
			}
		};
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("order", ShippingMethodMapper.class);
		service.addMapperClass("order", OrderMapper.class);
		service.addMapperClass("order", DetailMapper.class);
		service.addMapperClass("order", PaymentAccountMapper.class);
		service.addMapperClass("order", BillDetailMapper.class);
		service.addMapperClass("order", OrderStatusMapper.class);
		service.addMapperClass("order", StatusHistoryMapper.class);
		service.addMapperClass("order", OrderPackMapper.class);
		service.addMapperClass("order", OrderPaymentMapper.class);
		service.addMapperClass("order", OrderAlterHistoryMapper.class);
		service.addMapperClass("order", OrderTagMapper.class);
		service.addMapperClass("order", PostEmailOrderMapper.class);
		service.addMapperClass("order", OrderCurrencyRateMapper.class);
		service.addMapperClass("order", DropShippingOrderMapper.class);
		service.addMapperClass("order", DropShippingOrderDetailMapper.class);
		service.addMapperClass("order", DropShippingMapMapper.class);
		service.addMapperClass("order", DropShippingMapper.class);
		service.addMapperClass("order", PaymentCallbackMapper.class);
		service.addMapperClass("order", ShippingParameterMapper.class);
		service.addMapperClass("order", PreparatoryOrderMapper.class);
		service.addMapperClass("order", PreparatoryDetailMapper.class);
		service.addMapperClass("order", TotalOrderMapper.class);
		service.addMapperClass("order", TotalOrderMapMapper.class);
		service.addMapperClass("order", mapper.test.OrderMapper.class);
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {

		final Multibinder<IOrderFragmentPlugin> miProviders = Multibinder
				.newSetBinder(binder, IOrderFragmentPlugin.class);

		for (IOrderFragmentExtension e : filterModules(modules,
				IOrderFragmentExtension.class)) {
			e.registerOrdersFragment(miProviders);
		}

		// payment providers extension
		final Multibinder<IPaymentProvider> paymentProviders = Multibinder
				.newSetBinder(binder, IPaymentProvider.class);

		for (IPaymentExtension e : filterModules(modules,
				IPaymentExtension.class)) {
			e.registerPaymentProvider(paymentProviders);
		}

		// payment html plug-in
		final Multibinder<IPaymentHTMLPlugIn> paymentHtmls = Multibinder
				.newSetBinder(binder, IPaymentHTMLPlugIn.class);

		for (IPaymentHtmlExtension e : filterModules(modules,
				IPaymentHtmlExtension.class)) {
			e.registerPaymentHtmlPlugin(paymentHtmls);
		}

		// order extras providers extension
		final Multibinder<IOrderExtrasProvider> extrasProvider = Multibinder
				.newSetBinder(binder, IOrderExtrasProvider.class);

		for (IOrderExtrasExtension e : filterModules(modules,
				IOrderExtrasExtension.class)) {
			e.registerOrderExtrasProvider(extrasProvider);
		}

		// order extras providers extension
		final Multibinder<IOrderSourceProvider> sourceProvider = Multibinder
				.newSetBinder(binder, IOrderSourceProvider.class);

		for (IOrderSourceExtension e : filterModules(modules,
				IOrderSourceExtension.class)) {
			e.registerOrderSourceProvider(sourceProvider);
		}

		final Multibinder<ICollectProvider> collectProviders = Multibinder
				.newSetBinder(binder, ICollectProvider.class);
		for (ICollectExtension e : filterModules(modules,
				ICollectExtension.class)) {
			e.registerCollectProvider(collectProviders);
		}

		// freight plugin extension
		final Multibinder<IFreightPlugin> freightPlugins = Multibinder
				.newSetBinder(binder, IFreightPlugin.class);

		for (IFreightExtension e : filterModules(modules,
				IFreightExtension.class)) {
			e.registerFreightPlugin(freightPlugins);
		}

		// add by lijun
		Multibinder.newSetBinder(binder, IHandleCartRefreshEventPlugin.class);

		// order detail porvider extension
		final Multibinder<IOrderDetailProvider> detailProviders = Multibinder
				.newSetBinder(binder, IOrderDetailProvider.class);

		for (IOrderDetailExtension e : filterModules(modules,
				IOrderDetailExtension.class)) {
			e.registerOrderDetailProvider(detailProviders);
		}

		binder.bind(ICheckoutService.class).to(CheckoutService.class);
	}

	@Override
	public void registerProductFragment(
			Multibinder<IProductFragmentPlugin> plugins) {
		// 以下只是演示用，部分产品页面的内容来自订单，应在此注册
		Logger.debug("Registering 'also-bought' fragment");
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("also-bought",
						OrderHistoryFragmentProvider.class,
						OrderHistoryFragmentRender.class));
	}

	@Override
	public void registerOrdersFragment(Multibinder<IOrderFragmentPlugin> plugins) {
		// 在checkout/onepage（确认及创建订单页面）添加片段案例
		// AddressPretreatment 为对 OrderContext的预处理，可以省略
		plugins.addBinding().toInstance(
				new SimpleOrderFragmentPlugin("shipping-method",
						OrderShippingMethodProvider.class,
						OrderShippingMethodRenderer.class,
						AddressPretreatment.class));

		plugins.addBinding().toInstance(
				new SimpleOrderFragmentPlugin("cart-product",
						OrderCartProductProvider.class,
						OrderCartProductRenderer.class));

		plugins.addBinding()
				.toInstance(
						new SimpleOrderFragmentPlugin("summary",
								OrderSummaryProvider.class,
								OrderSummaryRenderer.class));

		plugins.addBinding().toInstance(
				new SimpleOrderFragmentPlugin("shipping-address",
						OrderShippingAddressProvider.class,
						OrderShippingAddressRenderer.class,
						AddressPretreatment.class));

		plugins.addBinding()
				.toInstance(
						new SimpleOrderFragmentPlugin("billing-address",
								BillingAddressProvider.class,
								BillingAddressRenderer.class,
								AddressPretreatment.class));

		plugins.addBinding()
				.toInstance(
						new SimpleOrderFragmentPlugin("preparatory-order",
								PreparatoryOrderProvider.class,
								PreparatoryOrderRenderer.class,
								OrderPretreatment.class));

		// plugins.addBinding().to(OrderPaymentFragment.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(RankProductHandler.class));
		eventBus.register(injector.getInstance(ProductStatisticsHandler.class));
		eventBus.register(injector
				.getInstance(PaymentConfirmationHandler.class));
		eventBus.register(injector.getInstance(RemindPaymentHandler.class));
		eventBus.register(injector.getInstance(OrderCancelledHandler.class));
		eventBus.register(injector.getInstance(handles.payment.PaymentStatusHandler.class));
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountMenuProvider> menuProviders,
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders,
			Multibinder<IMemberQuickMenuProvider> quickMenuProvider) {
		menuProviders.addBinding().to(MemberOrderMenuProvider.class);
		fragmentProviders.addBinding().to(MemberOrderStatistics.class);
		quickMenuProvider.addBinding().to(MemberOrderQuickMenuProvider.class);
		menuProviders.addBinding().to(DropShippingMenuProvider.class);
		menuProviders.addBinding().to(DropShipOrderMenuProvider.class);
	}

	@Override
	public List<RouteBuilder> getRouteBuilders() {
		return Lists.newArrayList(new TimerTriggerRouteBuilder());
	}

	@Override
	public void registerFreightPlugin(Multibinder<IFreightPlugin> plugin) {
		plugin.addBinding().to(PTFreeShipping.class);
		plugin.addBinding().to(ShippingCountriesFilter.class);
		plugin.addBinding().to(ProductWeightFilter.class);
	}

	@Override
	public void registerPaymentHtmlPlugin(
			Multibinder<IPaymentHTMLPlugIn> providers) {
		providers.addBinding().to(OrderInfoJSPlugIn.class);
	}

	@Override
	public void registerOrderDetailProvider(
			Multibinder<IOrderDetailProvider> detailProvider) {
		detailProvider.addBinding().to(DefaultOrderDetailProvider.class);
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("cartLife", ICartLifecycleService.class,
				CartLifecycleService.class);
		reg.publishService("cartService", ICartService.class, CartService.class);
		reg.publishService("extraLineService", IExtraLineService.class,
				ExtraLineService.class);
		reg.publishService("freightService", IFreightService.class,
				FreightService.class);
		reg.publishService("shippingMethod", IShippingMethodService.class,
				ShippingMethodService.class);
		reg.publishService("orderService", IOrderService.class,
				OrderService.class);
		reg.publishService("orderStatusService", IOrderStatusService.class,
				OrderStatusService.class);
		reg.publishService("paymentService", IWebPaymentService.class,
				WebPaymentService.class);

		reg.publishService("order_OrderCountService", IOrderCountService.class,
				OrderCountService.class);
		reg.publishService("orderEnquiryService", IOrderEnquiryService.class,
				OrderEnquiryService.class);
		reg.publishService("orderPackService", IOrderPackService.class,
				OrderPackService.class);
		reg.publishService("validateCartCampaignService",
				IValidateCartCampaignService.class,
				ValidateCartCampaignService.class);
		reg.publishService("billDetailService", IBillDetailService.class,
				BillDetailService.class);
		reg.publishService("orderAlterHistoryService",
				IOrderAlterHistoryService.class, OrderAlterHistoryService.class);
		reg.publishService("orderDetailService", IOrderDetailService.class,
				OrderDetailService.class);
		reg.publishService("cart_check", ICheckoutService.class,
				CheckoutService.class);
	}
}
