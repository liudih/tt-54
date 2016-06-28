package services.loyalty.fragment.renderer;

import play.twirl.api.Html;
import services.loyalty.theme.IThemeFragmentRenderer;
import valueobjects.loyalty.IThemeFragment;
import valueobjects.loyalty.ThemeRenderContext;

public class ThemeGroupFragmentRenderer implements IThemeFragmentRenderer{

	/*
	 * (non-Javadoc)
	 * <p>Title: render</p>
	 * <p>Description: 专题片段显示</p>
	 * @param fragment
	 * @param context
	 * @return
	 * @see services.loyalty.theme.IThemeFragmentRenderer#render(valueobjects.loyalty.IThemeFragment, valueobjects.loyalty.ThemeRenderContext)
	 */
	public Html render(IThemeFragment fragment, ThemeRenderContext context) {
		return views.html.loyalty.theme.theme_group.render((valueobjects.loyalty.ThemeGroupFragment)fragment);
	}
	
}
