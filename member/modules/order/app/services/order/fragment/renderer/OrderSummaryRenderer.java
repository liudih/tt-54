package services.order.fragment.renderer;

import play.twirl.api.Html;
import services.order.ExistingOrderRenderContext;
import services.order.IOrderFragmentRenderer;
import services.order.OrderRenderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderSummaryInfo;

public class OrderSummaryRenderer implements IOrderFragmentRenderer {

	@Override
	public Html render(IOrderFragment fragment, OrderRenderContext context) {
		return views.html.order.summary.render((OrderSummaryInfo) fragment,
				context.getComposite().getCurrency());
	}

	@Override
	public Html renderExisting(IOrderFragment fragment,
			ExistingOrderRenderContext context) {
		return views.html.order.summary.render((OrderSummaryInfo) fragment,
				context.getComposite().getCurrency());
	}

}
