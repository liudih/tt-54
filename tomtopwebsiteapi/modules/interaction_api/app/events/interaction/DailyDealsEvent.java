package events.interaction;

public class DailyDealsEvent {
	private Integer websiteId;
	private String type;
	private String currency;
	private boolean initDate;

	public DailyDealsEvent(Integer websiteId, String type, String currency,
			boolean initDate) {
		super();
		this.websiteId = websiteId;
		this.type = type;
		this.currency = currency;
		this.initDate = initDate;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isInitDate() {
		return initDate;
	}

	public void setInitDate(boolean initDate) {
		this.initDate = initDate;
	}
	
}
