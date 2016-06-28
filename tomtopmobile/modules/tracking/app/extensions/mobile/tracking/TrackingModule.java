package extensions.mobile.tracking;

import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.ModuleSupport;
import extensions.filter.IFilter;
import extensions.filter.IFilterExtension;
import extensions.runtime.IApplication;
import filter.tracking.MarketingUserFilter;
import filter.tracking.VisitLogFilter;

public class TrackingModule extends ModuleSupport implements IFilterExtension{

	@Override
	public Module getModule(IApplication arg0) {
		return null;
	}

	@Override
	public void registerFilter(Multibinder<IFilter> paramMultibinder) {
		paramMultibinder.addBinding().to(VisitLogFilter.class);
		paramMultibinder.addBinding().to(MarketingUserFilter.class);
		
	}
}
