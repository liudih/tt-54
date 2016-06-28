package services.loyalty.theme;

import services.product.IProductFragmentProvider;
import services.product.IProductFragmentRenderer;

public interface IThemeFragmentPlugin {
	/**
	 * 
	 * @Title: getName
	 * @Description: TODO(获取片段名称)
	 * @param @return
	 * @return String
	 * @throws 
	 * @author yinfei
	 */
	String getName();

	/**
	 * 
	 * @Title: getFragmentProvider
	 * @Description: TODO(获取片段数据提供者)
	 * @param @return
	 * @return IThemeFragmentProvider
	 * @throws 
	 * @author yinfei
	 */
	IThemeFragmentProvider getFragmentProvider();

	/**
	 * 
	 * @Title: getFragmentRenderer
	 * @Description: TODO(获取片段显示者)
	 * @param @return
	 * @return IThemeFragmentRenderer
	 * @throws 
	 * @author yinfei
	 */
	IThemeFragmentRenderer getFragmentRenderer();
}
