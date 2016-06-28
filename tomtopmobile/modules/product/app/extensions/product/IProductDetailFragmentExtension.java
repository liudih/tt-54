package extensions.product;

import services.product.IProductDetailPartProvider;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IProductDetailFragmentExtension extends IExtensionPoint {

	public void registerProductDetailPartProvider(
			Multibinder<IProductDetailPartProvider> provider);

}
