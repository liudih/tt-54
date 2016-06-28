package extensions.cart;

import java.util.List;
import java.util.Set;

import mapper.cart.CartHistoryMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import services.cart.base.template.ITemplateFragmentProvider;
import services.cart.CartEnquiryService;
import services.cart.CookieCartBuilderService;
import services.cart.CookieCartService;
import services.cart.CookieLaterService;
import services.cart.ICartBuilderService;
import services.cart.ICartEnquiryService;
import services.cart.ICartLaterServices;
import services.cart.ICartServices;
import LoyaltyForProviderService.LoyaltyForProviderService;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.BaseModule;
import extensions.base.ITemplateExtension;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.loyalty.IOrderLoyaltyProvider;
import extensions.runtime.IApplication;

public class CartModule extends ModuleSupport implements MyBatisExtension,
		IEventExtension, HessianServiceExtension, ITemplateExtension {

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(ICartBuilderService.class).to(
						CookieCartBuilderService.class);
				bind(ICartServices.class).to(CookieCartService.class);
				bind(ICartEnquiryService.class).to(CartEnquiryService.class);
				bind(ICartLaterServices.class).to(CookieLaterService.class);
				bind(IOrderLoyaltyProvider.class).to(
						LoyaltyForProviderService.class);
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(BaseModule.class);
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("cart", CartHistoryMapper.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("cartEnquiryService", ICartEnquiryService.class,
				CartEnquiryService.class);
		reg.publishService("loyaltyServiceCart", IOrderLoyaltyProvider.class,
				LoyaltyForProviderService.class);
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		// TODO Auto-generated method stub

		binder.addBinding()
				.to(services.cart.member.provider.MemberBrowseHistoryTemplateProvider.class);
		binder.addBinding()
				.to(services.cart.member.fragment.MemberBrowseHistoryTemplateProvider.class);
	}

}
