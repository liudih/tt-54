package extensions.mobile.paypal.login;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.member.login.ILoginExtension;
import extensions.member.login.ILoginProvider;
import extensions.paypal_api.login.AbstractPayPalLoginProvider;
import extensions.runtime.IApplication;

/**
 * 
 * @author lijun
 *
 */
public class PaypalModule extends ModuleSupport implements ILoginExtension {

	@Override
	public Module getModule(IApplication arg0) {
		return null;
	}

	@Override
	public void registerLoginProvider(Multibinder<ILoginProvider> binder) {
		binder.addBinding().to(PaypalLoginProvider.class);
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		binder.bind(AbstractPayPalLoginProvider.class).to(
				PaypalLoginProvider.class);
	}

}
