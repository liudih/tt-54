package extensions.payment;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IPaymentHtmlExtension extends IExtensionPoint {
	public void registerPaymentHtmlPlugin(
			Multibinder<IPaymentHTMLPlugIn> providers);
}
