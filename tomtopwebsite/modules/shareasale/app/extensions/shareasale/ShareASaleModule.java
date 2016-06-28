package extensions.shareasale;

import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.ModuleSupport;
import extensions.payment.IPaymentHTMLPlugIn;
import extensions.payment.IPaymentHtmlExtension;
import extensions.runtime.IApplication;

public class ShareASaleModule extends ModuleSupport implements
		IPaymentHtmlExtension {

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@Override
	public void registerPaymentHtmlPlugin(
			Multibinder<IPaymentHTMLPlugIn> providers) {
		//providers.addBinding().to(ShareASaleTrackingPlugin.class);
	}

}
