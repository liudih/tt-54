package services.cart;

import extensions.InjectorInstance;

public class SimpleCartFragmentPlugin implements ICartFragmentPlugin {

	final private String name;
	final private Class<? extends ICartFragmentProvider> provider;
	final private Class<? extends ICartFragmentRenderer> renderer;

	public SimpleCartFragmentPlugin(String name,
			Class<? extends ICartFragmentProvider> provider,
			Class<? extends ICartFragmentRenderer> renderer) {
		super();
		this.name = name;
		this.provider = provider;
		this.renderer = renderer;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public ICartFragmentProvider getFragmentProvider() {
		if (provider != null) {
			return InjectorInstance.getInjector().getInstance(provider);
		}
		return null;
	}

	@Override
	public ICartFragmentRenderer getFragmentRenderer() {
		if (renderer != null) {
			return InjectorInstance.getInjector().getInstance(renderer);
		}
		return null;
	}

}
