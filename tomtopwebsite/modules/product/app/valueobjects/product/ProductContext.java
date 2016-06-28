package valueobjects.product;

public class ProductContext {

	final String listingID;

	final String sku;

	final int lang;

	final int siteID;

	final String currency;

	public ProductContext(String listingID, String sku, int siteID, int lang,
			String currency) {
		this.listingID = listingID;
		this.sku = sku;
		this.lang = lang;
		this.siteID = siteID;
		this.currency = currency;
	}

	public String getCurrency() {
		return this.currency;
	}

	public String getListingID() {
		return listingID;
	}

	public int getSiteID() {
		return siteID;
	}

	public int getLang() {
		return lang;
	}

	public String getSku() {
		return sku;
	}

}
