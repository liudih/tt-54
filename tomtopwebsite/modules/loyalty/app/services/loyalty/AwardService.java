package services.loyalty;

import java.util.Map;
import java.util.Set;

import services.base.utils.StringUtils;
import services.loyalty.award.IAwardProvider;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class AwardService {

	@Inject
	Set<IAwardProvider> providers;

	public IAwardProvider getIAwardProviderByType(String type) {
		if (StringUtils.isEmpty(type)) {
			return null;
		}
		Map<String, IAwardProvider> maps = Maps.uniqueIndex(providers,
				e -> e.awardtype());
		return maps.get(type);

	}
}
