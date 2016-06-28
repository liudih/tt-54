package services.loyalty.theme;

import java.util.Set;

import javax.inject.Inject;

import entity.loyalty.Theme;
import services.product.IProductFragmentPlugin;
import services.product.IProductFragmentProvider;
import valueobjects.loyalty.ThemeComposite;
import valueobjects.loyalty.ThemeContext;
import valueobjects.product.ProductComposite;
import valueobjects.product.ProductContext;

public class ThemeCompositeEnquiry {
	@Inject
	Set<IThemeFragmentPlugin> fragmentPlugins;
	
	/**
	 * 
	 * @param context 
	 * @param theme 
	 * @Title: getThemeComposite
	 * @Description: TODO(获取专题复合)
	 * @param @return
	 * @return ThemeComposite
	 * @throws 
	 * @author yinfei
	 */
	public ThemeComposite getThemeComposite(ThemeContext context, Theme theme) {
		ThemeComposite composite = new ThemeComposite(context);
		for (IThemeFragmentPlugin fp : fragmentPlugins) {
			IThemeFragmentProvider provider = fp.getFragmentProvider();
			if (provider != null) {
				composite.put(fp.getName(), provider.getFragment(context,theme));
			}
		}
		return composite;
	}
}
