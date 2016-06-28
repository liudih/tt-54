package extensions.product;

import java.util.List;
import java.util.Set;

import services.base.template.ITemplateFragmentProvider;
import services.category.fragment.CategoryProvider;
import services.home.fragment.BannerFragmentProvider;
import services.home.fragment.DailyDealsFragmentProvider;
import services.home.fragment.FreeShippingFragmentProvider;
import services.home.fragment.HomeAdMiddleProvider;
import services.home.fragment.HotSalesFragmentProvider;
import services.home.fragment.NewArrivalsFragmentProvider;
import services.home.fragment.RecommendFragmentProvider;
import services.home.fragment.SearchNavigationBarProvider;
import services.product.IProductBadgePartProvider;
import services.product.IProductDetailPartProvider;
import services.product.IProductFragmentPlugin;
import services.product.SimpleProductFragmentPlugin;
import services.product.fragment.ProductBaseFragmentProvider;
import services.product.fragment.ProductImagesFragmentProvider;
import services.product.fragment.ProductRecommendFragmentProvider;
import services.product.fragment.renderer.ProductBaseFragmentRenderer;
import services.product.fragment.renderer.ProductColorSizeOptionsRenderer;
import services.product.fragment.renderer.ProductDescriptionRenderer;
import services.product.fragment.renderer.ProductImagesFragmentRenderer;
import services.product.fragment.renderer.ProductOptionsRenderer;
import services.product.fragment.renderer.ProductRecommendFragmentRenderer;
import services.product.variables.ClothingSizeReferenceVariableProvider;
import services.product.variables.ProductAttributeVariableProvider;
import services.product.variables.ProductImagesVariableProvider;
import services.search.SearchBarProvider;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.BaseModule;
import extensions.base.ITemplateExtension;
import extensions.event.IEventExtension;
import extensions.runtime.IApplication;

public class ProductModule extends ModuleSupport implements ITemplateExtension,
		IProductFragmentExtension,IProductDescriptionVariableExtension,
		IEventExtension {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(BaseModule.class);
	}

	@Override
	public Module getModule(IApplication app) {

		return null;
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		final Multibinder<IProductFragmentPlugin> miProviders = Multibinder
				.newSetBinder(binder, IProductFragmentPlugin.class);

		for (IProductFragmentExtension e : filterModules(modules,
				IProductFragmentExtension.class)) {
			e.registerProductFragment(miProviders);
		}

		final Multibinder<IProductDescriptionVariableProvider> descProviders = Multibinder
				.newSetBinder(binder, IProductDescriptionVariableProvider.class);
		
		for (IProductDescriptionVariableExtension e : filterModules(modules,
				IProductDescriptionVariableExtension.class)) {
			e.registerVariableProvider(descProviders);
		}
		
		final Multibinder<ITemplateFragmentProvider> tfProviders = Multibinder
				.newSetBinder(binder, ITemplateFragmentProvider.class);

		for (ITemplateExtension e : filterModules(modules,
				ITemplateExtension.class)) {
			e.registerTemplateProviders(tfProviders);
		}
		
		final Multibinder<IProductBadgePartProvider> MassPartProviders = Multibinder
				.newSetBinder(binder, IProductBadgePartProvider.class);
		for (IProductBadgeFragmentExtension e : filterModules(modules,
				IProductBadgeFragmentExtension.class)) {
			e.registerProductBadgePartProvider(MassPartProviders);
		}
		
		final Multibinder<IProductDetailPartProvider> detailPartProviders = Multibinder
				.newSetBinder(binder, IProductDetailPartProvider.class);
		for (IProductDetailFragmentExtension e : filterModules(modules,
				IProductDetailFragmentExtension.class)) {
			e.registerProductDetailPartProvider(detailPartProviders);
		}

	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(CategoryProvider.class);
		binder.addBinding().to(DailyDealsFragmentProvider.class);
		binder.addBinding().to(HotSalesFragmentProvider.class);
		binder.addBinding().to(FreeShippingFragmentProvider.class);
		binder.addBinding().to(NewArrivalsFragmentProvider.class);
		binder.addBinding().to(RecommendFragmentProvider.class);
		binder.addBinding().to(BannerFragmentProvider.class);
		binder.addBinding().to(HomeAdMiddleProvider.class);
		binder.addBinding().to(SearchNavigationBarProvider.class);
		binder.addBinding().to(SearchBarProvider.class);
	}

	@Override
	public void registerProductFragment(
			Multibinder<IProductFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("images",
						ProductImagesFragmentProvider.class,
						ProductImagesFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("base",
						ProductBaseFragmentProvider.class,
						ProductBaseFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("description", null,
						ProductDescriptionRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product_options", 
						ProductBaseFragmentProvider.class,
						ProductOptionsRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product_colorsize_options", 
						ProductBaseFragmentProvider.class,
						ProductColorSizeOptionsRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product_recommend", 
						ProductRecommendFragmentProvider.class,
						ProductRecommendFragmentRenderer.class));
	}

	@Override
	public void registerVariableProvider(
			Multibinder<IProductDescriptionVariableProvider> binder) {
		binder.addBinding().to(ProductAttributeVariableProvider.class);
		binder.addBinding().to(ClothingSizeReferenceVariableProvider.class);
		binder.addBinding().to(ProductImagesVariableProvider.class);		
	}
	
	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
	}

}
