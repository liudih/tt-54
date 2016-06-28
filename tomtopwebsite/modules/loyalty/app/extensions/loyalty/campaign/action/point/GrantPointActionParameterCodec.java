package extensions.loyalty.campaign.action.point;

import services.campaign.IActionParameter;
import services.campaign.SimpleJsonCodec;

public class GrantPointActionParameterCodec extends
		SimpleJsonCodec<IActionParameter> {

	@Override
	public Class<GrantPointActionParameter> getSourceClass() {
		return GrantPointActionParameter.class;
	}

}
