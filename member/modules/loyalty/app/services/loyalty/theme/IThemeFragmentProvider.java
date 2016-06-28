package services.loyalty.theme;

import entity.loyalty.Theme;
import valueobjects.loyalty.IThemeFragment;
import valueobjects.loyalty.ThemeContext;

public interface IThemeFragmentProvider {
	
	/**
	 * 
	 * @param context 
	 * @param theme 
	 * @Title: getFragment
	 * @Description: TODO(获取专题片段)
	 * @param @return
	 * @return IThemeFragment
	 * @throws 
	 * @author yinfei
	 */
	public IThemeFragment getFragment(ThemeContext context, Theme theme);
}
