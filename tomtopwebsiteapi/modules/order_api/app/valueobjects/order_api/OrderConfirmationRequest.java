package valueobjects.order_api;

import java.io.Serializable;

public class OrderConfirmationRequest implements Serializable {
	final String cartId;
	final int siteId;
	final int addressId;
	final int shippingMethodId;
	final String origin;
	final String message;
	final String ip;
	final int langID;
	final String currency;
	final String vhost;
	//add by lijun
	//非会员用户直接通过购物车快捷支付时是不需要地址的
	boolean noAddress;

	public OrderConfirmationRequest(String cartId, int siteId, int addressId,
			int shippingMethodId, String origin, String message, String ip,
			int langID, String currency, String vhost) {
		this.cartId = cartId;
		this.siteId = siteId;
		this.addressId = addressId;
		this.shippingMethodId = shippingMethodId;
		this.origin = origin;
		this.message = message;
		this.ip = ip;
		this.langID = langID;
		this.currency = currency;
		this.vhost = vhost;
	}

	
	public boolean getNoAddress() {
		return noAddress;
	}


	public void setNoAddress(boolean noAddress) {
		this.noAddress = noAddress;
	}


	public String getCartId() {
		return cartId;
	}

	public int getSiteId() {
		return siteId;
	}

	public int getAddressId() {
		return addressId;
	}

	public int getShippingMethodId() {
		return shippingMethodId;
	}

	public String getOrigin() {
		return origin;
	}

	public String getMessage() {
		return message;
	}

	public String getIp() {
		return ip;
	}

	public int getLangID() {
		return langID;
	}

	public String getCurrency() {
		return currency;
	}

	public String getVhost() {
		return vhost;
	}

}
