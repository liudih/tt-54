package extensions.interaction;

import handlers.interaction.MemberProductViewHandler;

import java.util.List;
import java.util.Set;

import services.base.template.ITemplateFragmentProvider;
import services.interaction.fragment.InteractionCommentFragmentProvider;
import services.interaction.fragment.MemberLoginProvider;
import services.interaction.fragment.RecentHistoryFragmentProvider;
import services.interaction.fragment.renderer.InteractionCommentFragmentRenderer;
import services.product.IProductBadgePartProvider;
import services.product.IProductDetailPartProvider;
import services.product.IProductFragmentPlugin;
import services.product.SimpleProductFragmentPlugin;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.IShareProvider;
import extensions.base.ITemplateExtension;
import extensions.event.IEventExtension;
import extensions.interaction.collect.CollectBoxProvider;
import extensions.interaction.member.CartQuickMenuProvider;
import extensions.interaction.member.CategoriesQuickMenuProvider;
import extensions.interaction.member.WishListQuickMenuProvider;
import extensions.interaction.share.IShareExtension;
import extensions.interaction.shareProviders.FacebookShareProvider;
import extensions.interaction.shareProviders.GoogleShareProvider;
import extensions.interaction.shareProviders.TwitterShareProvider;
import extensions.member.MemberModule;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.order.collect.ICollectExtension;
import extensions.order.collect.ICollectProvider;
import extensions.product.IProductBadgeFragmentExtension;
import extensions.product.IProductDetailFragmentExtension;
import extensions.product.IProductFragmentExtension;
import extensions.product.ProductModule;
import extensions.runtime.IApplication;

public class InteractionModule extends ModuleSupport implements
		IEventExtension, IProductBadgeFragmentExtension, IShareExtension,
		IProductDetailFragmentExtension, ICollectExtension, ITemplateExtension, 
		IMemberAccountExtension, IProductFragmentExtension {
	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(ProductModule.class, MemberModule.class);
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		Multibinder<IShareProvider> multibinder = Multibinder.newSetBinder(
				binder, IShareProvider.class);
		FluentIterable.from(modules).forEach(c -> {
			if (IShareExtension.class.isInstance(c)) {
				((IShareExtension) c).registerShareProvider(multibinder);
			}
		});
		binder.bind(IProductBadgePartProvider.class).annotatedWith(Names.named("RatingReview")).to(
				RatingReviewProvider.class);

	}

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(MemberProductViewHandler.class));
	}

	@Override
	public void registerShareProvider(Multibinder<IShareProvider> binder) {
		binder.addBinding().to(FacebookShareProvider.class);
		binder.addBinding().to(GoogleShareProvider.class);
		binder.addBinding().to(TwitterShareProvider.class);
	}

	@Override
	public void registerProductBadgePartProvider(
			Multibinder<IProductBadgePartProvider> provider) {
		
		provider.addBinding().to(CollectProvider.class);
	}

	@Override
	public void registerProductDetailPartProvider(
			Multibinder<IProductDetailPartProvider> provider) {
		provider.addBinding().to(ProductDetailCollectProvider.class);

	}

	@Override
	public void registerCollectProvider(Multibinder<ICollectProvider> binder) {
		binder.addBinding().to(CollectBoxProvider.class);
	}
	
	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(RecentHistoryFragmentProvider.class);
		binder.addBinding().to(MemberLoginProvider.class);
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders) {
		fragmentProviders.addBinding().to(WishListQuickMenuProvider.class);
		fragmentProviders.addBinding().to(CartQuickMenuProvider.class);
		fragmentProviders.addBinding().to(CategoriesQuickMenuProvider.class);
	}
	
	
	@Override
	public void registerProductFragment(
			Multibinder<IProductFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("comment",
						InteractionCommentFragmentProvider.class,
						InteractionCommentFragmentRenderer.class));
	}
	
}
