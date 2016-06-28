package services.campaign;

public class CampaignContextFactory {

	public CampaignContext createContext(Object payload,
			ICampaignInstance instance) {
		return new CampaignContext(payload, instance);
	}

}
