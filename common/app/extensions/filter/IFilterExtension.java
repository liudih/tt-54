package extensions.filter;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IFilterExtension extends IExtensionPoint {

	void registerFilter(Multibinder<IFilter> filters);

}
