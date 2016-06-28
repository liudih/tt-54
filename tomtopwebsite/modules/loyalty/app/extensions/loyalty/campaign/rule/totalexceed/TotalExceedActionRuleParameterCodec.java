package extensions.loyalty.campaign.rule.totalexceed;

import services.campaign.IActionRuleParameter;
import services.campaign.SimpleJsonCodec;

public class TotalExceedActionRuleParameterCodec extends
		SimpleJsonCodec<IActionRuleParameter> {

	@Override
	public Class<TotalExceedActionRuleParameter> getSourceClass() {
		return TotalExceedActionRuleParameter.class;
	}

}
