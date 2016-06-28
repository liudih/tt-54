package extensions.product;

import services.product.IProductBadgePartProvider;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IProductBadgeFragmentExtension extends IExtensionPoint {

	public void registerProductBadgePartProvider(
			Multibinder<IProductBadgePartProvider> provider);

}
