package services.campaign;

import context.WebContext;

public class CampaignContext {

	final Object payload;
	final ICampaignInstance instance;
	WebContext webContext;
	Object actionOn;

	public CampaignContext(Object payload, ICampaignInstance instance) {
		this.payload = payload;
		this.instance = instance;
	}

	public Object getActionOn() {
		return actionOn;
	}

	/**
	 * For example, if actions should be acting on Order, then actionOn should
	 * be an order object.
	 * 
	 * @param actionOn
	 */
	public void setActionOn(Object actionOn) {
		this.actionOn = actionOn;
	}

	public Object getPayload() {
		return payload;
	}

	public ICampaignInstance getInstance() {
		return instance;
	}

	public WebContext getWebContext() {
		return webContext;
	}

	public void setWebContext(WebContext webContext) {
		this.webContext = webContext;
	}

}
