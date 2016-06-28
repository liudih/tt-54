package extensions.base;

import handlers.base.VisitHandler;

import java.util.Set;

import mapper.base.ShorturlMapper;
import mapper.base.VisitLogMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import play.Application;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.google.inject.Module;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.common.CommonModule;
import extensions.event.IEventExtension;

public class BaseModule extends ModuleSupport implements MyBatisExtension,IEventExtension{

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(CommonModule.class);
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("base", ShorturlMapper.class);
		service.addMapperClass("base", VisitLogMapper.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(VisitHandler.class));
	}

	@Override
	public Module getModule(Application application) {
		
		return null;
	}


}
