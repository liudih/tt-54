package dto.payment;

import java.io.Serializable;

public abstract class AbstractPaymentParam implements IPaymentParam,
		Serializable {
	private static final long serialVersionUID = 1L;

	private final String orderNumber;
	private final int langID;
	// 支付成功后要返回到页面url add by lijun
	private String returnUrl;
	private String cancelUrl;

	public AbstractPaymentParam(String orderNumber, int langID) {
		this.orderNumber = orderNumber;
		this.langID = langID;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public int getLangID() {
		return langID;
	}

	public abstract String getPaymentID();

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

}
