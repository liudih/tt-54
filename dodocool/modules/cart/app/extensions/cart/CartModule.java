package extensions.cart;

import java.util.List;

import services.dodocool.base.template.ITemplateFragmentProvider;
import services.dodocool.cart.fragment.AddToCartFragmentProvider;
import services.dodocool.cart.fragmentRenderer.AddToCartFramentRenderer;
import services.dodocool.product.IProductFragmentPlugin;
import services.dodocool.product.SimpleProductFragmentPlugin;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.template.TemplateFragmentExtension;
import extensions.cart.template.CartHeaderTemplate;
import extensions.product.IProductFragmentExtension;
import extensions.runtime.IApplication;

public class CartModule extends ModuleSupport implements
		TemplateFragmentExtension, IProductFragmentExtension {

	@Override
	public Module getModule(IApplication arg0) {
		return new AbstractModule() {
			@Override
			protected void configure() {

			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(CartHeaderTemplate.class);
	}

	@Override
	public void registerProductFragment(
			Multibinder<IProductFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("add-to-cart-menu",
						AddToCartFragmentProvider.class,
						AddToCartFramentRenderer.class));
	}

}
