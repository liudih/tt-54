package extensions.interaction;

import services.dodocool.product.IProductFragmentPlugin;
import services.dodocool.product.SimpleProductFragmentPlugin;
import services.interaction.prodcut.fragment.ProductFaqFragmentProvider;
import services.interaction.prodcut.fragment.renderer.ProductAskQuestionFragmentRenderer;
import services.interaction.prodcut.fragment.renderer.ProductFaqFragmentRenderer;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.ModuleSupport;
import extensions.product.IProductFragmentExtension;
import extensions.runtime.IApplication;

public class InteractionModule extends ModuleSupport implements
		IProductFragmentExtension {

	@Override
	public Module getModule(IApplication arg0) {
		return new AbstractModule() {
			@Override
			protected void configure() {

			}
		};
	}

	@Override
	public void registerProductFragment(
			Multibinder<IProductFragmentPlugin> plugins) {
		// 暂时先屏蔽
		// plugins.addBinding().toInstance(
		// new SimpleProductFragmentPlugin("review-count-and-score",
		// ProductReviewsStarFragmentProvider.class,
		// ProductReviewsStarFragmentRenderer.class));
		//
		// plugins.addBinding().toInstance(
		// new SimpleProductFragmentPlugin("product-reviews",
		// ProductCustomerReviewFragmentProvider.class,
		// ProductCustomerReviewFragmentRenderer.class));

		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-faq",
						ProductFaqFragmentProvider.class,
						ProductFaqFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-ask-question", null,
						ProductAskQuestionFragmentRenderer.class));
	}

}