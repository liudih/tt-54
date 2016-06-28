package services.product;

import extensions.InjectorInstance;

public class SimpleProductFragmentPlugin implements IProductFragmentPlugin {

	final private String name;
	final private Class<? extends IProductFragmentProvider> provider;
	final private Class<? extends IProductFragmentRenderer> renderer;

	public SimpleProductFragmentPlugin(String name,
			Class<? extends IProductFragmentProvider> provider,
			Class<? extends IProductFragmentRenderer> renderer) {
		super();
		this.name = name;
		this.provider = provider;
		this.renderer = renderer;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IProductFragmentProvider getFragmentProvider() {
		if (provider != null) {
			return InjectorInstance.getInjector().getInstance(provider);
		}
		return null;
	}

	@Override
	public IProductFragmentRenderer getFragmentRenderer() {
		if (renderer != null) {
			return InjectorInstance.getInjector().getInstance(renderer);
		}
		return null;
	}

}
