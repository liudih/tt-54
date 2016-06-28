package email.valueobjects;

import email.model.IEmailModel;

public class OrderPaymentFields extends IEmailModel {
	private String orderId;
	private String firstName;
	private String address;
	private String createDate;
	private String currencySymbol;
	private String shippingMethod;
	private String grandTotal;

	public OrderPaymentFields(String emailType, Integer language,
			String orderId, String firstName, String address,
			String createDate, String currencySymbol, String shippingMethod,
			String grandTotal) {
		super(emailType, language);
		this.orderId = orderId;
		this.firstName = firstName;
		this.address = address;
		this.createDate = createDate;
		this.currencySymbol = currencySymbol;
		this.shippingMethod = shippingMethod;
		this.grandTotal = grandTotal;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}

}
