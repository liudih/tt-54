package extensions.common;

import java.util.Set;

import mybatis.MyBatisInitModule;
import play.Application;

import com.google.common.collect.Sets;
import com.google.inject.Module;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.event.EventBusModule;

public class CommonModule extends ModuleSupport {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(MyBatisInitModule.class,EventBusModule.class);
	}

	@Override
	public Module getModule(Application application) {
		return null;
	}

}
