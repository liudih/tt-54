package extensions.product;

import services.price.IDiscountProvider;
import services.price.IPriceCalculationContextProvider;
import services.price.IPriceProvider;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IPriceExtension extends IExtensionPoint {

	void registerPriceDiscountProviders(
			Multibinder<IPriceProvider> priceProviders,
			Multibinder<IDiscountProvider> discountProviders,
			Multibinder<IPriceCalculationContextProvider> contextProviders);
}
