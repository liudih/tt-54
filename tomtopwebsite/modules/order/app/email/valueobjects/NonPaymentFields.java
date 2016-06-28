package email.valueobjects;

import email.model.IEmailModel;

public class NonPaymentFields extends IEmailModel {
	private String orderId;
	private String firstName;
	private String address;
	private String date;
	private String symbol;
	private String shippingMethod;
	private String grandTotal;
	private String subtotal;
	private String items;
	private String freight;
	private String url;

	public NonPaymentFields(String emailType, Integer language) {
		super(emailType, language);
	}

	public NonPaymentFields(String emailType, Integer language, String orderId,
			String firstName, String address, String date, String symbol,
			String shippingMethod, String grandTotal, String subtotal,
			String items, String freight, String url) {
		super(emailType, language);
		this.orderId = orderId;
		this.firstName = firstName;
		this.address = address;
		this.date = date;
		this.symbol = symbol;
		this.shippingMethod = shippingMethod;
		this.grandTotal = grandTotal;
		this.subtotal = subtotal;
		this.items = items;
		this.freight = freight;
		this.url = url;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
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

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
