package extensions.mobile.google;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.filter.IFilter;
import extensions.filter.IFilterExtension;
import extensions.google_api.login.AbstractGoogleLoginProvider;
import extensions.member.login.ILoginExtension;
import extensions.member.login.ILoginProvider;
import extensions.runtime.IApplication;

/**
 * 
 * @author lijun
 *
 */
public class GoogleModule extends ModuleSupport implements ILoginExtension, IFilterExtension {

	@Override
	public Module getModule(IApplication arg0) {
		return null;
	}

	@Override
	public void registerLoginProvider(Multibinder<ILoginProvider> binder) {
		binder.addBinding().to(GoogleLoginProvider.class);
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		binder.bind(AbstractGoogleLoginProvider.class).to(
				GoogleLoginProvider.class);
	}

	@Override
	public void registerFilter(Multibinder<IFilter> filters) {
		filters.addBinding().to(GoogleTagManagerScriptAddition.class);
	}

}
