package services.order.payment;

import javax.inject.Inject;

import services.order.IOrderContextPretreatment;
import services.order.IOrderFragmentPlugin;
import services.order.IOrderFragmentProvider;
import services.order.IOrderFragmentRenderer;

public class OrderPaymentFragment implements IOrderFragmentPlugin {

	@Inject
	OrderPaymentFragmentRenderer renderer;
	
	@Override
	public String getName() {
		return "payment";
	}

	@Override
	public IOrderFragmentProvider getFragmentProvider() {
		return null;
	}

	@Override
	public IOrderFragmentRenderer getFragmentRenderer() {
		return renderer;
	}

	@Override
	public IOrderContextPretreatment getContextPretreatment() {
		return null;
	}

}
