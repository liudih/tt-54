package extensions.vizury;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.BaseModule;
import extensions.filter.IFilter;
import extensions.filter.IFilterExtension;
import extensions.runtime.IApplication;

public class VizuryModule extends ModuleSupport implements IFilterExtension {

	@Override
	public void registerFilter(Multibinder<IFilter> filters) {
		filters.addBinding().to(VizuryScriptAddition.class);
	}

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(BaseModule.class);
	}

}
