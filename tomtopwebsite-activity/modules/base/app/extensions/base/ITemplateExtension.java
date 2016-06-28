package extensions.base;

import services.base.activity.template.ITemplateFragmentProvider;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ITemplateExtension extends IExtensionPoint {

	void registerTemplateProviders(Multibinder<ITemplateFragmentProvider> binder);
}
