package extensions.cart;

import handlers.order.OrderConfirmationHandler;

import java.util.List;

import mapper.cart.CartBaseMapper;
import mapper.cart.CartItemHistoryMapper;
import mapper.cart.CartItemListAdditionMapper;
import mapper.cart.CartItemListMapper;
import mapper.cart.CartItemMapper;
import mapper.cart.ExtraMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import play.Logger;
import services.base.template.ITemplateFragmentProvider;
import services.cart.ICartFragmentPlugin;
import services.cart.IValidateCartCampaignService;
import services.cart.SimpleCartFragmentPlugin;
import services.cart.ValidateCartCampaignService;
import services.cart.fragment.AddCartResultFragmentFragmentProvider;
import services.cart.fragment.CartAddFragmentFragmentProvider;
import services.cart.fragment.CartDropFragmentProvider;
import services.cart.fragment.ProductBundleSaleFragmentProvider;
import services.cart.fragment.ProductStorageMapFragmentProvider;
import services.cart.fragment.renderer.AddCartResultFragmentRenderer;
import services.cart.fragment.renderer.CartAddFragmentRender;
import services.cart.fragment.renderer.ProductBundleSaleFragmentRenderer;
import services.cart.fragment.renderer.ProductFloatTopFragmentRenderer;
import services.cart.fragment.renderer.ProductStorageMapFragmentRenderer;
import services.product.IProductFragmentPlugin;
import services.product.SimpleProductFragmentPlugin;
import valueobjects.order_api.cart.IExtraLineRule;

import com.google.common.eventbus.EventBus;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.template.ITemplateExtension;
import extensions.cart.member.MergeCartProcess;
import extensions.event.IEventExtension;
import extensions.member.login.ILoginProcess;
import extensions.member.login.ILoginProcessExtension;
import extensions.product.IProductFragmentExtension;
import extensions.runtime.IApplication;

public class CartModule extends ModuleSupport implements MyBatisExtension,
		IEventExtension, IProductFragmentExtension, ITemplateExtension,
		ICartFragmentExtension, ILoginProcessExtension {

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		final Multibinder<ICartFragmentPlugin> tfplugin = Multibinder
				.newSetBinder(binder, ICartFragmentPlugin.class);

		for (ICartFragmentExtension e : filterModules(modules,
				ICartFragmentExtension.class)) {
			e.registerCartFragment(tfplugin);
		}
		// add by lijun
		Multibinder.newSetBinder(binder, IExtraLineRule.class);
		binder.bind(IValidateCartCampaignService.class).to(
				ValidateCartCampaignService.class);
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(CartDropFragmentProvider.class);
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("cart", CartBaseMapper.class);
		service.addMapperClass("cart", CartItemMapper.class);
		service.addMapperClass("cart", CartItemListMapper.class);
		service.addMapperClass("cart", CartItemHistoryMapper.class);
		service.addMapperClass("cart", ExtraMapper.class);
		service.addMapperClass("cart", CartItemListAdditionMapper.class);
	}

	@Override
	public void registerProductFragment(
			Multibinder<IProductFragmentPlugin> plugins) {
		Logger.debug("Registering 'cart' product fragment");
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("cart",
						CartAddFragmentFragmentProvider.class,
						CartAddFragmentRender.class));

		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-top-float", null,
						ProductFloatTopFragmentRenderer.class));

		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("bundle-sale",
						ProductBundleSaleFragmentProvider.class,
						ProductBundleSaleFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-storage",
						ProductStorageMapFragmentProvider.class,
						ProductStorageMapFragmentRenderer.class));
	}

	@Override
	public void registerCartFragment(Multibinder<ICartFragmentPlugin> plugins) {
		Logger.debug("Registering 'add-cart-result' cart fragment");
		plugins.addBinding().toInstance(
				new SimpleCartFragmentPlugin("add-cart-result",
						AddCartResultFragmentFragmentProvider.class,
						AddCartResultFragmentRenderer.class));
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(OrderConfirmationHandler.class));
	}

	@Override
	public void registerLoginProcess(Multibinder<ILoginProcess> binder) {
		binder.addBinding().to(MergeCartProcess.class);
	}

}
