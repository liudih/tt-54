package extensions.payment;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IPaymentExtension extends IExtensionPoint {

	public void registerPaymentProvider(Multibinder<IPaymentProvider> providers);

}
