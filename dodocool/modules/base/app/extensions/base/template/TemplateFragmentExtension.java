package extensions.base.template;

import services.dodocool.base.template.ITemplateFragmentProvider;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface TemplateFragmentExtension extends IExtensionPoint {
	
	public void registerTemplateProviders(Multibinder<ITemplateFragmentProvider> binder);
		

}
