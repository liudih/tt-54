package extensions.image;

import mapper.image.ImageCacheMapper;
import mapper.image.ImageMapper;
import mapper.image.ImgUseMappingMapper;
import mapper.image.UploadFilePathMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import services.image.IImageUpdateService;
import services.image.ImageUpdateService;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

import extensions.ModuleSupport;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.runtime.IApplication;

public class ImageModule extends ModuleSupport implements MyBatisExtension,
		HessianServiceExtension {

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(IImageUpdateService.class).to(
						ImageUpdateService.class);
			}
		};
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("image", ImageMapper.class);
		service.addMapperClass("image", ImageCacheMapper.class);
		service.addMapperClass("image", UploadFilePathMapper.class);
		service.addMapperClass("image", ImgUseMappingMapper.class);
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("imageUpdate", IImageUpdateService.class,
				ImageUpdateService.class);
	}
}
