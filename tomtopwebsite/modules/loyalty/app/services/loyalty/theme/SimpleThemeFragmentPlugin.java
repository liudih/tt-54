package services.loyalty.theme;

import extensions.InjectorInstance;
import services.product.IProductFragmentProvider;
import services.product.IProductFragmentRenderer;

public class SimpleThemeFragmentPlugin implements IThemeFragmentPlugin{
	final private String name;
	final private Class<? extends IThemeFragmentProvider> provider;
	final private Class<? extends IThemeFragmentRenderer> renderer;
	
	public SimpleThemeFragmentPlugin(String name,
			Class<? extends IThemeFragmentProvider> provider,
			Class<? extends IThemeFragmentRenderer> renderer){
		super();
		this.name = name;
		this.provider = provider;
		this.renderer = renderer;
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getName</p>
	 * <p>Description: 获取专题片段名称</p>
	 * @return
	 * @see services.loyalty.theme.IThemeFragmentPlugin#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getFragmentProvider</p>
	 * <p>Description: 获取片段数据提供者</p>
	 * @return
	 * @see services.loyalty.theme.IThemeFragmentPlugin#getFragmentProvider()
	 */
	public IThemeFragmentProvider getFragmentProvider() {
		if (provider != null) {
			return InjectorInstance.getInjector().getInstance(provider);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getFragmentRenderer</p>
	 * <p>Description: 获取片段显示者</p>
	 * @return
	 * @see services.loyalty.theme.IThemeFragmentPlugin#getFragmentRenderer()
	 */
	public IThemeFragmentRenderer getFragmentRenderer() {
		if (renderer != null) {
			return InjectorInstance.getInjector().getInstance(renderer);
		}
		return null;
	}
	

}
