package plugins.renderer;


import play.twirl.api.Html;
import valueobjects.order_api.IOrderFragment;

/**
 * 订单Fragment绘制器
 * 
 * @author renyy
 *
 */
public interface IOrderFragmentRenderer {
	/**
	 * do the actual HTML rendering
	 *
	 * @param fragment
	 * @return
	 */
	Html render(IOrderFragment fragment);

}
