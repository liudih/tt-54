package services.loyalty.theme;

import play.twirl.api.Html;
import valueobjects.loyalty.IThemeFragment;
import valueobjects.loyalty.ThemeRenderContext;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public interface IThemeFragmentRenderer {
	
	/**
	 * 
	 * @Title: render
	 * @Description: TODO(显示专题片段)
	 * @param @param fragment
	 * @param @param context
	 * @param @return
	 * @return Html
	 * @throws 
	 * @author yinfei
	 */
	Html render(IThemeFragment fragment, ThemeRenderContext context);

}
