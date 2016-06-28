package extensions.messaging;

import handlers.messaging.JoinDropShipMessagingHandler;
import handlers.messaging.JoinWholeSaleMessagingHandler;

import java.util.List;

import mapper.messaging.BroadcastMapper;
import mapper.messaging.MessageMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import services.messaging.IBroadcastService;
import services.messaging.IMessageService;
import services.messaging.impl.BroadcastService;
import services.messaging.impl.MessageService;

import com.google.common.eventbus.EventBus;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import dao.messaging.IBroadcastDao;
import dao.messaging.IMessageDao;
import dao.messaging.impl.BroadcastDao;
import dao.messaging.impl.MessageDao;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.member.IMyMessageProvider;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.IMemberAccountMenuProvider;
import extensions.member.account.IMemberQuickMenuProvider;
import extensions.messaging.member.MemberMyMessageProvider;
import extensions.runtime.IApplication;

public class MessagingModule extends ModuleSupport implements MyBatisExtension,
		IMemberAccountExtension, IEventExtension, HessianServiceExtension {

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("messaging", BroadcastMapper.class);
		service.addMapperClass("messaging", MessageMapper.class);
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountMenuProvider> menuProviders,
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders,
			Multibinder<IMemberQuickMenuProvider> quickMenuProvider) {
		menuProviders.addBinding().to(MemberMyMessageProvider.class);

	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		binder.bind(IBroadcastDao.class).to(BroadcastDao.class);
		binder.bind(IBroadcastService.class).to(BroadcastService.class);
		binder.bind(IMessageDao.class).to(MessageDao.class);
		binder.bind(IMessageService.class).to(MessageService.class);
		// binder.bind(IMyMessageProvider.class).to(MemberMyMessageProvider.class);
		Multibinder<IMyMessageProvider> multibinder = Multibinder.newSetBinder(
				binder, IMyMessageProvider.class);
		multibinder.addBinding().to(MemberMyMessageProvider.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector
				.getInstance(JoinWholeSaleMessagingHandler.class));
		eventBus.register(injector
				.getInstance(JoinDropShipMessagingHandler.class));
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("messageService", IMessageService.class,
				MessageService.class);
		reg.publishService("broadcastService", IBroadcastService.class,
				BroadcastService.class);

	}

}
