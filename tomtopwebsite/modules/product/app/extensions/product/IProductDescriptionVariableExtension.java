package extensions.product;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

/**
 * Implement this in your module if you want to provide product description
 * variables. This will be called during module startup.
 * 
 * @see IProductDescriptionVariableProvider
 * @author kmtong
 *
 */
public interface IProductDescriptionVariableExtension extends IExtensionPoint {

	void registerVariableProvider(
			Multibinder<IProductDescriptionVariableProvider> binder);

}
