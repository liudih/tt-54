package extensions.product;

import services.product.IProductAttrIconProvider;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IProductAttrIconFragmentExtension extends IExtensionPoint {

	public void registerProductAttrIconProvider(
			Multibinder<IProductAttrIconProvider> provider);

}
