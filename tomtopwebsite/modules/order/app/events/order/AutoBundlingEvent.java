package events.order;

public class AutoBundlingEvent {

	private final int siteId;

	public AutoBundlingEvent(int siteId) {
		this.siteId = siteId;
	}

	public int getSiteId() {
		return siteId;
	}

}
