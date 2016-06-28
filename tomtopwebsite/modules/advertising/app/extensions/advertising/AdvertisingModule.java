package extensions.advertising;

import java.util.Set;

import javax.inject.Singleton;

import mapper.advertising.AdvertisingBaseMapper;
import mapper.advertising.AdvertisingContentMapper;
import mapper.advertising.AdvertisingDistributionMapper;
import mapper.advertising.AdvertisingPositionMapper;
import mapper.advertising.AdvertisingTypeMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import service.advertising.fragment.AdvertisingProvider;
import services.IAdvertisingService;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.product.IProductAdvertisingExtension;
import extensions.product.IProductAdvertisingProvider;
import extensions.product.ProductModule;
import extensions.runtime.IApplication;

public class AdvertisingModule extends ModuleSupport implements
		MyBatisExtension, IEventExtension,
		IProductAdvertisingExtension, HessianServiceExtension {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(ProductModule.class);
	}

	@Override
	public Module getModule(IApplication application) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("advertising", AdvertisingBaseMapper.class);
		service.addMapperClass("advertising",
				AdvertisingDistributionMapper.class);
		service.addMapperClass("advertising", AdvertisingContentMapper.class);
		service.addMapperClass("advertising", AdvertisingTypeMapper.class);
		service.addMapperClass("advertising", AdvertisingPositionMapper.class);

	}


	@Override
	public void registerProductAdvertisingFragment(
			Multibinder<IProductAdvertisingProvider> plugins) {
		// TODO Auto-generated method stub
		plugins.addBinding().to(AdvertisingProvider.class).in(Singleton.class);
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("advertisingService", IAdvertisingService.class,
				AdvertisingProvider.class);
		
	}

}
