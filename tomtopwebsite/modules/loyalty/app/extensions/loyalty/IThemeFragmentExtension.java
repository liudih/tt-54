package extensions.loyalty;

import services.loyalty.theme.IThemeFragmentPlugin;
import services.product.IProductFragmentPlugin;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IThemeFragmentExtension extends IExtensionPoint{
	
	/**
	 * 
	 * @Title: registerThemeFragment
	 * @Description: TODO(注册专题扩展)
	 * @param @param plugins
	 * @return void
	 * @throws 
	 * @author yinfei
	 */
	public void registerThemeFragment(
			Multibinder<IThemeFragmentPlugin> plugins);
}
