package events.search;

import java.io.Serializable;
import java.util.Date;

import valueobjects.base.Page;

public class KeywordSearchEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final String keyword;
	final Page<String> results;
	final int websiteID;
	final int languageID;
	final String ip;
	final String ltc;
	final String stc;
	final Date timestamp;

	public KeywordSearchEvent(String keyword, Page<String> results,
			int websiteID, int languageID, String ip, String ltc, String stc) {
		this.keyword = keyword;
		this.results = results;
		this.websiteID = websiteID;
		this.languageID = languageID;
		this.ip = ip;
		this.ltc = ltc;
		this.stc = stc;
		this.timestamp = new Date();
	}

	public String getKeyword() {
		return keyword;
	}

	public Page<String> getResults() {
		return results;
	}

	public int getWebsiteID() {
		return websiteID;
	}

	public int getLanguageID() {
		return languageID;
	}

	public String getIp() {
		return ip;
	}

	public String getLTC() {
		return ltc;
	}

	public String getSTC() {
		return stc;
	}

	public Date getTimestamp() {
		return timestamp;
	}

}
