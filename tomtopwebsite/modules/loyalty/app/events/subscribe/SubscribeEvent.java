package events.subscribe;

import java.util.Date;
import java.util.List;

public class SubscribeEvent {

	final String email;
	final Date timestamp;
	final List<String> categoryNames;
	// add by lijun
	final int siteId;
	final Boolean isExist;
	final int language;

	public SubscribeEvent(String email, Date timestamp,
			List<String> categoryNames, int siteId, Boolean isExist,
			int language) {
		this.email = email;
		this.timestamp = timestamp;
		this.categoryNames = categoryNames;
		this.siteId = siteId;
		this.isExist = isExist;
		this.language = language;
	}

	public Boolean getIsExist() {
		return isExist;
	}

	public int getLanguage() {
		return language;
	}

	public String getEmail() {
		return email;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public List<String> getCategoryNames() {
		return categoryNames;
	}

	public int getSiteId() {
		return siteId;
	}

}
