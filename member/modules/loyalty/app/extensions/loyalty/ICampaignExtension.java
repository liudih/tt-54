package extensions.loyalty;

import services.campaign.IAction;
import services.campaign.IActionRule;
import services.campaign.ICampaign;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ICampaignExtension extends IExtensionPoint {

	/**
	 * Register ICampaign, IActionRule, IAction implementations.
	 * 
	 * @param binder
	 * @param rules
	 * @param actions
	 */
	public void registerCampaign(Multibinder<ICampaign> binder,
			Multibinder<IActionRule> rules, Multibinder<IAction> actions);

}
