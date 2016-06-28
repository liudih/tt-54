package valueobjects.order_api.payment;

import java.io.Serializable;
import java.util.Map;

import valueobjects.order_api.ConfirmedOrder;
import dto.Currency;
import dto.ShippingMethodDetail;

public class PaymentContext implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final ConfirmedOrder order;
	final ShippingMethodDetail shippingMethod;
	final Currency currency;
	private Map<String, String> map;
	private String orderLable;
	private String backUrl;
	private Integer billID;

	// 支付是走新流程还是老流程
	private boolean modeNew = false;

	public PaymentContext(ConfirmedOrder order,
			ShippingMethodDetail shippingMethod, Currency currency) {
		this.order = order;
		this.shippingMethod = shippingMethod;
		this.currency = currency;
	}

	public boolean isModeNew() {
		return modeNew;
	}

	public void setModeNew(boolean modeNew) {
		this.modeNew = modeNew;
	}

	public ConfirmedOrder getOrder() {
		return order;
	}

	public ShippingMethodDetail getShippingMethod() {
		return shippingMethod;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public String getOrderLable() {
		return orderLable;
	}

	public void setOrderLable(String orderLable) {
		this.orderLable = orderLable;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public Integer getBillID() {
		return billID;
	}

	public void setBillID(Integer billID) {
		this.billID = billID;
	}

}
