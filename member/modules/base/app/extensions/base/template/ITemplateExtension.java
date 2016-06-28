package extensions.base.template;

import services.base.template.ITemplateFragmentProvider;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ITemplateExtension extends IExtensionPoint {

	/**
	 * Implementors need to register their own concrete
	 * ITemplateFragmentProvider to the Multibinder provided.
	 * 
	 * @param binder
	 */
	void registerTemplateProviders(Multibinder<ITemplateFragmentProvider> binder);
}
