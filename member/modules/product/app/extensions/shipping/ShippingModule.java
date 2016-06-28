package extensions.shipping;

import mapper.shipping.ShippingStorageMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import com.google.inject.Module;

import extensions.ModuleSupport;
import extensions.runtime.IApplication;

public class ShippingModule extends ModuleSupport implements MyBatisExtension {

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("product", ShippingStorageMapper.class);
	}
}
