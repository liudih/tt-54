package valueobjects.product;

import java.io.Serializable;
import java.util.List;

public class ProductBadgePartContext implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	final List<String> listingIds;

	final int lang;

	final int siteID;

	final String currency;

	public ProductBadgePartContext(List<String> listingIDs, int lang,
			int siteID, String currency) {
		super();
		this.listingIds = listingIDs;
		this.lang = lang;
		this.siteID = siteID;
		this.currency = currency;
	}

	public int getLang() {
		return lang;
	}

	public List<String> getListingIds() {
		return listingIds;
	}

	public int getSiteID() {
		return siteID;
	}

	public String getCurrency() {
		return currency;
	}

	@Override
	public String toString() {
		return "ProductBadgePartContext [listingIds=" + listingIds + ", lang="
				+ lang + ", siteID=" + siteID + ", currency=" + currency + "]";
	}

}
