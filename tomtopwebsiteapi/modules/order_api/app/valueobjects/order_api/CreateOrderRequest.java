package valueobjects.order_api;

import java.io.Serializable;
import java.util.List;

import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyPrefer;

/**
 * 
 * @author lijun
 *
 */
public class CreateOrderRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	final List<CartItem> items;
	final Integer siteId;
	final Integer addressId;
	final Integer shippingMethodId;
	final String origin;
	final String message;
	final String ip;
	final Integer langID;
	final String currency;
	final String vhost;
	final Integer storage;
	// 优惠
	final List<LoyaltyPrefer> prefer;
	private String cpaymenttype;	//支付类型	
	
	private Integer shipMethodId;	//物流方式id
	private String shipCode;	//物流方式code

	public CreateOrderRequest(List<CartItem> items, Integer siteId,
			Integer addressId, Integer shippingMethodId, String origin,
			String message, String ip, Integer langID, String currency,
			String vhost, List<LoyaltyPrefer> prefer, Integer storage) {
		this.items = items;
		this.siteId = siteId;
		this.addressId = addressId;
		this.shippingMethodId = shippingMethodId;
		this.origin = origin;
		this.message = message;
		this.ip = ip;
		this.langID = langID;
		this.currency = currency;
		this.vhost = vhost;
		this.prefer = prefer;
		this.storage = storage;
	}

	public Integer getStorage() {
		return storage;
	}

	public List<LoyaltyPrefer> getPrefer() {
		return prefer;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public Integer getShippingMethodId() {
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

	public Integer getLangID() {
		return langID;
	}

	public String getCurrency() {
		return currency;
	}

	public String getVhost() {
		return vhost;
	}

	public String getCpaymenttype() {
		return cpaymenttype;
	}

	public void setCpaymenttype(String cpaymenttype) {
		this.cpaymenttype = cpaymenttype;
	}

	public Integer getShipMethodId() {
		return shipMethodId;
	}

	public void setShipMethodId(Integer shipMethodId) {
		this.shipMethodId = shipMethodId;
	}

	public String getShipCode() {
		return shipCode;
	}

	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}

}
