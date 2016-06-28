package valueobjects.livechat.session;

/**
 * This is put into a queue, waiting to be scheduled.
 * 
 * @author kmtong
 *
 */
public class ConnectRequest {
	String originLTC;
	String destinationAlias;
	int siteID;
	int languageID;

	public ConnectRequest() {
		super();
	}

	public ConnectRequest(String originLTC, String destinationAlias,
			int siteID, int languageID) {
		super();
		this.originLTC = originLTC;
		this.destinationAlias = destinationAlias;
		this.siteID = siteID;
		this.languageID = languageID;
	}

	public String getOriginLTC() {
		return originLTC;
	}

	public void setOriginLTC(String originLTC) {
		this.originLTC = originLTC;
	}

	public String getDestinationAlias() {
		return destinationAlias;
	}

	public void setDestinationAlias(String destinationAlias) {
		this.destinationAlias = destinationAlias;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public int getLanguageID() {
		return languageID;
	}

	public void setLanguageID(int languageID) {
		this.languageID = languageID;
	}

}
