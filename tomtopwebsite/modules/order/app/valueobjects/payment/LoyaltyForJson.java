package valueobjects.payment;

public class LoyaltyForJson {
	private String type;
	private String code;
	private String currency;
	private String price;

	public LoyaltyForJson(String type, String code, String currency,
			String price) {
		this.type = type;
		this.code = code;
		this.currency = currency;
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
