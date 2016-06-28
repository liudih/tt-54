package extensions.order;

import java.util.List;
import java.util.Set;

import plugins.mobile.order.IOrderDetailPlugin;
import plugins.mobile.order.IOrderFragmentPlugin;
import plugins.mobile.order.OrderPayentMethodPlugin;
import plugins.mobile.order.OrderProductDetailPlugin;
import plugins.mobile.order.SimpleOrderFragmentPlugin;
import plugins.mobile.order.provider.BillingAddressProvider;
import plugins.mobile.order.provider.OrderCartProductProvider;
import plugins.mobile.order.provider.OrderShippingAddressProvider;
import plugins.mobile.order.provider.OrderShippingMethodProvider;
import plugins.mobile.order.provider.OrderSummaryProvider;
import plugins.mobile.order.renderer.BillingAddressRenderer;
import plugins.mobile.order.renderer.OrderCartProductRenderer;
import plugins.mobile.order.renderer.OrderShippingAddressRenderer;
import plugins.mobile.order.renderer.OrderShippingMethodRenderer;
import plugins.mobile.order.renderer.OrderSummaryRenderer;
import services.cart.CookieCartBuilderService;
import services.cart.CookieCartService;
import services.cart.CookieLaterService;
import services.cart.ICartBuilderService;
import services.cart.ICartLaterServices;
import services.cart.ICartServices;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.cart.CartModule;
import extensions.event.IEventExtension;
import extensions.member.MemberModule;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.order.collect.ICollectExtension;
import extensions.order.collect.ICollectProvider;
import extensions.order.member.AddressMenuProvider;
import extensions.order.member.BillingAddressMenuProvider;
import extensions.order.member.OrderStatisticsProvider;
import extensions.product.ProductModule;
import extensions.runtime.IApplication;

public class OrderModule extends ModuleSupport implements IEventExtension,
		IOrderFragmentExtension, IMemberAccountExtension {

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
				bind(ICartBuilderService.class).to(
						CookieCartBuilderService.class);
				bind(ICartServices.class).to(CookieCartService.class);
				bind(ICartLaterServices.class).to(CookieLaterService.class);
			}
		};
	}

	@Override
	public void registerListener(EventBus arg0, Injector arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerOrdersFragment(Multibinder<IOrderFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleOrderFragmentPlugin("shipping-method",
						OrderShippingMethodProvider.class,
						OrderShippingMethodRenderer.class));

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
						OrderShippingAddressRenderer.class));

		plugins.addBinding().toInstance(
				new SimpleOrderFragmentPlugin("billing-address",
						BillingAddressProvider.class,
						BillingAddressRenderer.class));

	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		final Multibinder<IOrderFragmentPlugin> ofpb = Multibinder
				.newSetBinder(binder, IOrderFragmentPlugin.class);
		// registerOrdersFragment
		FluentIterable.from(modules).forEach(m -> {
			if (IOrderFragmentExtension.class.isInstance(m)) {
				((IOrderFragmentExtension) m).registerOrdersFragment(ofpb);
			}
		});

		final Multibinder<IOrderDetailPlugin> odp = Multibinder.newSetBinder(
				binder, IOrderDetailPlugin.class);
		odp.addBinding().to(OrderProductDetailPlugin.class);
		odp.addBinding().to(OrderPayentMethodPlugin.class);

		final Multibinder<ICollectProvider> collectProviders = Multibinder
				.newSetBinder(binder, ICollectProvider.class);
		for (ICollectExtension e : filterModules(modules,
				ICollectExtension.class)) {
			e.registerCollectProvider(collectProviders);
		}
		
		final Multibinder<IOrderSourceProvider> sourceProvider = Multibinder
				.newSetBinder(binder, IOrderSourceProvider.class);

		for (IOrderSourceExtension e : filterModules(modules,
				IOrderSourceExtension.class)) {
			e.registerOrderSourceProvider(sourceProvider);
		}

		final Multibinder<CampaignUiProvider> cp = Multibinder.newSetBinder(
				binder, CampaignUiProvider.class);


		cp.addBinding().to(PointCampaignUiProvider.class);
		
		binder.bind(HttpRequestFactory.class).toInstance(new NetHttpTransport().createRequestFactory());
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders) {
		fragmentProviders.addBinding().to(AddressMenuProvider.class);
		fragmentProviders.addBinding().to(BillingAddressMenuProvider.class);

		fragmentProviders.addBinding().to(OrderStatisticsProvider.class);
	}
}
