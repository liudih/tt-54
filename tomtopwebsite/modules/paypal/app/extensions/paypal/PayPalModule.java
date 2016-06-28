package extensions.paypal;

import handlers.paypal.PaymentStatusHandler;
import handlers.paypal.PaypalPaymentLogHandler;

import java.util.List;
import java.util.Set;

import services.paypal.ExpressCheckoutNvpService;
import services.paypal.IExpressCheckoutNvpService;
import valueobjects.paypal_api.IPaypalPaymentStatus;
import mapper.paypal.payment.PaymentMapper;
import mapper.paypal.payment.PaypaiReturnLogMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.member.MemberModule;
import extensions.member.login.ILoginExtension;
import extensions.member.login.ILoginProvider;
import extensions.order.OrderModule;
import extensions.payment.IPaymentExtension;
import extensions.payment.IPaymentProvider;
import extensions.paypal.login.PayPalLoginProvider;
import extensions.paypal.payment.PayPalPaymentProvider;
import extensions.runtime.IApplication;

public class PayPalModule extends ModuleSupport implements MyBatisExtension,
		ILoginExtension, IPaymentExtension, IEventExtension,HessianServiceExtension {

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(MemberModule.class, OrderModule.class);
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("paypal", PaymentMapper.class);
		service.addMapperClass("paypal", PaypaiReturnLogMapper.class);

	}

	@Override
	public void registerPaymentProvider(Multibinder<IPaymentProvider> providers) {
		providers.addBinding().to(PayPalPaymentProvider.class);
	}

	@Override
	public void registerLoginProvider(Multibinder<ILoginProvider> providers) {
		providers.addBinding().to(PayPalLoginProvider.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(PaypalPaymentLogHandler.class));
		eventBus.register(injector.getInstance(PaymentStatusHandler.class));
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		binder.bind(IExpressCheckoutNvpService.class).to(ExpressCheckoutNvpService.class);
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("paypal_ecService", IExpressCheckoutNvpService.class,
				ExpressCheckoutNvpService.class);
		
	}

	
}
