package extensions.filter;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.runtime.IApplication;

public class FilterModule extends ModuleSupport {

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		final Multibinder<IFilter> filters = Multibinder.newSetBinder(binder,
				IFilter.class);

		for (IFilterExtension e : filterModules(modules, IFilterExtension.class)) {
			e.registerFilter(filters);
		}
	}

}
