package extensions.product;

import java.util.List;
import java.util.Set;

import services.dodocool.banner.HomeBannerServiceFragmentProvider;
import services.dodocool.base.template.ITemplateFragmentProvider;
import services.dodocool.product.IProductFragmentPlugin;
import services.dodocool.product.SimpleProductFragmentPlugin;
import services.dodocool.product.Category.IndexCategoryFragmentProvider;
import services.dodocool.product.fragment.HotProductFragmentProvider;
import services.dodocool.product.fragment.MayAlsoLikeFragmentProvider;
import services.dodocool.product.fragment.ProductAmazonLinkUrlFragmentProvider;
import services.dodocool.product.fragment.ProductAttributeFragmentProvider;
import services.dodocool.product.fragment.ProductBaseFragmentProvider;
import services.dodocool.product.fragment.ProductBreadcrumbFragmentProvider;
import services.dodocool.product.fragment.ProductEntityMapFragmentProvider;
import services.dodocool.product.fragment.ProductImagesFragmentProvider;
import services.dodocool.product.fragmentRenderer.MayAlsoLikeFramentRenderer;
import services.dodocool.product.fragmentRenderer.MayAlsoLikeMenuFramentRenderer;
import services.dodocool.product.fragmentRenderer.ProductAmazonLinkUrlFragmentRenderer;
import services.dodocool.product.fragmentRenderer.ProductAttributeFragmentRenderer;
import services.dodocool.product.fragmentRenderer.ProductBaseFragmentRenderer;
import services.dodocool.product.fragmentRenderer.ProductBreadcrumbFragmentRenderer;
import services.dodocool.product.fragmentRenderer.ProductEntityMapFragmentRenderer;
import services.dodocool.product.fragmentRenderer.ProductImagesFragmentRenderer;
import services.dodocool.product.fragmentRenderer.ProductOverviewFramentRenderer;
import services.dodocool.product.fragmentRenderer.ProductShareFragmentRenderer;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.BaseModule;
import extensions.base.template.TemplateFragmentExtension;
import extensions.common.CommonModule;
import extensions.product.template.SearchBar;
import extensions.runtime.IApplication;

public class ProductModule extends ModuleSupport implements
		IProductFragmentExtension, TemplateFragmentExtension {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(CommonModule.class, BaseModule.class);
	}

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {

			}
		};
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
	}

	@Override
	public void registerProductFragment(
			Multibinder<IProductFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("base",
						ProductBaseFragmentProvider.class,
						ProductBaseFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-images",
						ProductImagesFragmentProvider.class,
						ProductImagesFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-overview", null,
						ProductOverviewFramentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-amazon-link-url",
						ProductAmazonLinkUrlFragmentProvider.class,
						ProductAmazonLinkUrlFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-share", null,
						ProductShareFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-entity-map",
						ProductEntityMapFragmentProvider.class,
						ProductEntityMapFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-multi-attribute",
						ProductAttributeFragmentProvider.class,
						ProductAttributeFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product_breadcrumb",
						ProductBreadcrumbFragmentProvider.class,
						ProductBreadcrumbFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("you-may-also-like",
						MayAlsoLikeFragmentProvider.class,
						MayAlsoLikeFramentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("you-may-also-like-menu",
						null,
						MayAlsoLikeMenuFramentRenderer.class));
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(IndexCategoryFragmentProvider.class);
		binder.addBinding().to(SearchBar.class);
		binder.addBinding().to(HotProductFragmentProvider.class);
		binder.addBinding().to(HomeBannerServiceFragmentProvider.class);
	}

}
