package valueobjects.base;

public class Regional {

	private Integer countryId;
	private String countryCode;
	private String countryName;
	private Integer currencyId;
	private String currencyCode;
	private String currencySymbol;
	private String languageName;
	private Integer languageId;
	private Integer websiteId;

	public Integer getCountryId() {
		return countryId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public String getLanguageName() {
		return languageName;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

}
