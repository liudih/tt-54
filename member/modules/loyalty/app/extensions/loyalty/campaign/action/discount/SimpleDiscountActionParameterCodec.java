package extensions.loyalty.campaign.action.discount;

import services.campaign.IActionParameter;
import services.campaign.SimpleJsonCodec;

public class SimpleDiscountActionParameterCodec extends
		SimpleJsonCodec<IActionParameter> {

	@Override
	public Class<SimpleDiscountActionParameter> getSourceClass() {
		return SimpleDiscountActionParameter.class;
	}

}
