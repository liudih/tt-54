package services.research.vote;

import extensions.InjectorInstance;

public class SimpleVoteFragmentPlugin implements IVoteFragmentPlugin {

	final private String name;
	final private Class<? extends IVoteFragmentProvider> provider;
	final private Class<? extends IVoteFragmentRenderer> renderer;
	
	public SimpleVoteFragmentPlugin(String name,
			Class<? extends IVoteFragmentProvider> provider,
			Class<? extends IVoteFragmentRenderer> renderer){
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
	public IVoteFragmentProvider getFragmentProvider() {
		if (provider != null) {
			return InjectorInstance.getInjector().getInstance(provider);
		}
		return null;
	}

	@Override
	public IVoteFragmentRenderer getFragmentRenderer() {
		if (renderer != null) {
			return InjectorInstance.getInjector().getInstance(renderer);
		}
		return null;
	}

}
