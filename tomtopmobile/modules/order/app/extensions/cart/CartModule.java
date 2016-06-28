package extensions.cart;

import java.util.List;

import services.base.template.ITemplateFragmentProvider;
import services.cart.ICartFragmentPlugin;
import services.mobile.cart.fragment.CartBarProvider;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.ITemplateExtension;
import extensions.runtime.IApplication;

public class CartModule extends ModuleSupport implements ITemplateExtension {

	@Override
	public Module getModule(IApplication application) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		final Multibinder<ICartFragmentPlugin> tfplugin = Multibinder
				.newSetBinder(binder, ICartFragmentPlugin.class);

	}
	
	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(CartBarProvider.class);
	}

}
