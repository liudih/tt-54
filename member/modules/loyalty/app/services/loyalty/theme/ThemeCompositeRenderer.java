package services.loyalty.theme;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.product.IProductFragmentPlugin;
import services.product.IProductFragmentRenderer;
import valueobjects.loyalty.IThemeFragment;
import valueobjects.loyalty.ThemeComposite;
import valueobjects.loyalty.ThemeRenderContext;

public class ThemeCompositeRenderer {
	final Map<String, IThemeFragmentRenderer> renderers;
	
	@Inject
	public ThemeCompositeRenderer(final Set<IThemeFragmentPlugin> fragmentPlugins){
		this.renderers = new HashMap<String, IThemeFragmentRenderer>();
		for(IThemeFragmentPlugin r : fragmentPlugins){
			IThemeFragmentRenderer renderer = r.getFragmentRenderer();
			if(null != renderer){
				renderers.put(r.getName(), renderer);
			}
		}
	}
	
	/**
	 * 
	 * @Title: renderFragment
	 * @Description: TODO(显示专题片段)
	 * @param @param composite
	 * @param @param name
	 * @param @return
	 * @return Html
	 * @throws 
	 * @author yinfei
	 */
	public Html renderFragment(ThemeComposite composite, String name){
		IThemeFragment fragment = composite.get(name);
		ThemeRenderContext ctx = new ThemeRenderContext(composite,this);
		IThemeFragmentRenderer renderer = renderers.get(name);
		if(null != renderer){
			return renderer.render(fragment, ctx);
		}
		return null;
	}
}
