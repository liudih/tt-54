package valueobjects.order_api;

import java.io.Serializable;

import dto.order.Order;

public class DropShipOrderMessage extends Order implements Serializable {
	private String useremail;
	
	private String userorderid;
	
	private String cdropshippingid;

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUserorderid() {
		return userorderid;
	}

	public void setUserorderid(String userorderid) {
		this.userorderid = userorderid;
	}

	public String getCdropshippingid() {
		return cdropshippingid;
	}

	public void setCdropshippingid(String cdropshippingid) {
		this.cdropshippingid = cdropshippingid;
	}
	
}
