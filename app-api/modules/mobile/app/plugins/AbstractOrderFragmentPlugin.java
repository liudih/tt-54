package plugins;


import play.twirl.api.Html;
import plugins.provider.IOrderFragmentProvider;
import plugins.renderer.IOrderFragmentRenderer;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;

/**
 * 抽象OrderFragmentPlugin
 * 
 * @author lijun
 *
 */
public abstract class AbstractOrderFragmentPlugin implements
		IOrderFragmentPlugin {

	abstract IOrderFragmentProvider getFragmentProvider();

	abstract IOrderFragmentRenderer getFragmentRenderer();

	@Override
	public Html render(OrderContext context) {
		IOrderFragmentProvider provider = this.getFragmentProvider();
		IOrderFragmentRenderer renderer = this.getFragmentRenderer();
		if (provider == null || renderer == null) {
			return null;
		}
		IOrderFragment dateFragment = provider.getFragment(context);
		return renderer.render(dateFragment);
	}

}