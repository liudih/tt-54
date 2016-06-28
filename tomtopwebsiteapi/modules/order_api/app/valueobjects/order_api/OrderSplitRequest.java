package valueobjects.order_api;

public class OrderSplitRequest {
	final int siteId;
	final String origin;
	final String ip;
	final int langID;
	final String currency;
	final String vhost;
	final Integer memberID;

	public OrderSplitRequest(int siteId, String origin, String ip, int langID,
			String currency, String vhost, Integer memberID) {
		this.siteId = siteId;
		this.origin = origin;
		this.ip = ip;
		this.langID = langID;
		this.currency = currency;
		this.vhost = vhost;
		this.memberID = memberID;
	}

	public int getSiteId() {
		return siteId;
	}

	public String getOrigin() {
		return origin;
	}

	public String getIp() {
		return ip;
	}

	public int getLangID() {
		return langID;
	}

	public String getCurrency() {
		return currency;
	}

	public String getVhost() {
		return vhost;
	}

	public Integer getMemberID() {
		return memberID;
	}

}
