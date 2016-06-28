package extensions.product;

import services.product.IProductAttrPartProvider;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IProductAttrPartFragmentExtension extends IExtensionPoint {

	public void registerProductAttrPartProvider(
			Multibinder<IProductAttrPartProvider> provider);

}
