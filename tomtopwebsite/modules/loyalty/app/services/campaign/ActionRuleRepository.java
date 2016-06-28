package services.campaign;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.FluentIterable;

/**
 * ActionRule 代表一个条件，用来绑定到 ICampaign 和 ICampaignInstance 中，用来判断条件是否满足，条件满足了，就会执行
 * CampaignInstance 定义的 Action
 * 
 * @author kmtong
 *
 */
@Singleton
public class ActionRuleRepository {

	@Inject
	Set<IActionRule> allRules;

	public List<IActionRule> getAllRules() {
		return FluentIterable.from(allRules).toList();
	}

	public IActionRule findRule(String ruleID) {
		return FluentIterable.from(allRules)
				.firstMatch(r -> r.getId().equals(ruleID)).orNull();
	}

}
