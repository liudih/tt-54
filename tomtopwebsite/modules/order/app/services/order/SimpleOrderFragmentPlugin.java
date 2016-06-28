package services.order;

import extensions.InjectorInstance;

public class SimpleOrderFragmentPlugin implements IOrderFragmentPlugin {

	final private String name;
	final private Class<? extends IOrderFragmentProvider> provider;
	final private Class<? extends IOrderFragmentRenderer> renderer;
	final private Class<? extends IOrderContextPretreatment> pretreatment;

	public SimpleOrderFragmentPlugin(String name,
			Class<? extends IOrderFragmentProvider> provider,
			Class<? extends IOrderFragmentRenderer> renderer,
			Class<? extends IOrderContextPretreatment> pretreatment) {
		this.name = name;
		this.provider = provider;
		this.renderer = renderer;
		this.pretreatment = pretreatment;
	}

	public SimpleOrderFragmentPlugin(String name,
			Class<? extends IOrderFragmentProvider> provider,
			Class<? extends IOrderFragmentRenderer> renderer) {
		this(name, provider, renderer, null);
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

	@Override
	public IOrderContextPretreatment getContextPretreatment() {
		if (pretreatment != null) {
			return InjectorInstance.getInjector().getInstance(pretreatment);
		}
		return null;
	}

}
