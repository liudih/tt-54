package services.campaign;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.FluentIterable;

/**
 * Action 是可执行的对象，用来绑定到 ICampaign 和 ICampaignInstance 中，目的是奖励某种行为。
 * 
 * @author kmtong
 *
 */
@Singleton
public class ActionRepository {

	@Inject
	Set<IAction> allActions;

	public List<IAction> getAllActions() {
		return FluentIterable.from(allActions).toList();
	}

	public IAction findAction(String actionID) {
		return FluentIterable.from(allActions)
				.firstMatch(r -> r.getId().equals(actionID)).orNull();
	}

}
