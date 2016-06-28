package events.interaction;

public class SuperDealEvent {

	private Integer siteId;
	private Integer langaugeId;

	public SuperDealEvent(Integer siteId, Integer langaugeId) {
		this.siteId = siteId;
		this.langaugeId = langaugeId;
	}

	public Integer getLangaugeId() {
		return langaugeId;
	}

	public Integer getSiteId() {
		return siteId;
	}
}
