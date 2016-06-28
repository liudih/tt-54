package extensions.email;

import handler.email.DeleteEmailLogHandler;
import handler.email.EmailLogHandler;
import handler.email.EmailResendHandler;

import java.util.List;
import java.util.Set;

import org.apache.camel.builder.RouteBuilder;

import service.email.EmailSendService;
import service.email.send.IEmailSendService;
import mapper.email.EmailLogMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.camel.ICamelExtension;
import extensions.common.CommonModule;
import extensions.email.camel.DeleteEmailLogTimerTriggerRouteBuilder;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.runtime.IApplication;

public class EmailModule extends ModuleSupport implements MyBatisExtension,HessianServiceExtension,IEventExtension,ICamelExtension
		 {
	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(CommonModule.class);
	}

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(IEmailSendService.class).to(
						EmailSendService.class);
			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("email", EmailLogMapper.class);
		
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(EmailLogHandler.class));
		eventBus.register(injector.getInstance(EmailResendHandler.class));
		eventBus.register(injector.getInstance(DeleteEmailLogHandler.class));
	}

	@Override
	public List<RouteBuilder> getRouteBuilders() {
		return Lists.newArrayList(
				new DeleteEmailLogTimerTriggerRouteBuilder());
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("email_send", IEmailSendService.class,
				EmailSendService.class);
		
	}


}
