package plugins.mobile.order;

import plugins.mobile.order.provider.IOrderFragmentProvider;
import plugins.mobile.order.renderer.IOrderFragmentRenderer;
import extensions.InjectorInstance;

public class SimpleOrderFragmentPlugin extends AbstractOrderFragmentPlugin {

	final private String name;
	final private Class<? extends IOrderFragmentProvider> provider;
	final private Class<? extends IOrderFragmentRenderer> renderer;

	public SimpleOrderFragmentPlugin(String name,
			Class<? extends IOrderFragmentProvider> provider,
			Class<? extends IOrderFragmentRenderer> renderer) {
		this.name = name;
		this.provider = provider;
		this.renderer = renderer;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IOrderFragmentProvider getFragmentProvider() {
		if (provider != null) {
			return InjectorInstance.getInjector().getInstance(provider);
		}
		return null;
	}

	@Override
	public IOrderFragmentRenderer getFragmentRenderer() {
		if (renderer != null) {
			return InjectorInstance.getInjector().getInstance(renderer);
		}
		return null;
	}

}
