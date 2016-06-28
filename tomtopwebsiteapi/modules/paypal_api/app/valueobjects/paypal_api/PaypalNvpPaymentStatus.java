package valueobjects.paypal_api;

import java.io.Serializable;
import java.util.Map;

/**
 * Paypal支付状态
 * 
 * @author lijun
 *
 */
public class PaypalNvpPaymentStatus implements IPaypalPaymentStatus, Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String STAGE_SET = "setExpressCheckout";
	public static final String STAGE_GET = "GetExpressCheckoutDetails";
	public static final String STAGE_DO = "DoExpressCheckoutPayment";

	private boolean isCompleted = false;
	// 支付所处阶段(setExpressCheckout GetExpressCheckoutDetails
	// DoExpressCheckoutPayment)
	private String stage;
	// 失败详细信息
	private StringBuilder failedInfo = new StringBuilder();
	// paypal返回的错误code
	private String errorCode;
	// 跳转到paypal的地址(https://...)
	private String redirectURL;
	// paypay是否可以下一步(比如当setExpressCheckout这一步没有异常出现就可以引导用户跳转到paypal进行付款)
	private boolean isNextStep = false;
	// paypal返回的原生信息
	private transient Map<String, String> paypalReply;
	//订单号
	private String orderNum;
	//站点id
	private Integer webSiteId;
	//支付完成后的支付状态
	private String paymentStatus;
	
	
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	/**
	 * 
	 * @param stage
	 *            this value from PaypalNvpPaymentStatus
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	public Map<String, String> getPaypalReply() {
		return paypalReply;
	}

	public void setPaypalReply(Map<String, String> paypalReply) {
		this.paypalReply = paypalReply;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public boolean isNextStep() {
		return isNextStep;
	}

	public void setNextStep(boolean isNextStep) {
		this.isNextStep = isNextStep;
	}

	public String getStage() {
		return stage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public void setFailedInfo(String failedInfo) {
		this.failedInfo.append(failedInfo);
	}

	@Override
	public boolean isCompleted() {
		return this.isCompleted;
	}

	@Override
	public String getFailedInfo() {
		return failedInfo.toString();
	}

	
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public void setWebSiteId(Integer webSiteId) {
		this.webSiteId = webSiteId;
	}

	@Override
	public Integer getWebSiteId() {
		return this.webSiteId;
	}

	@Override
	public String getOrderNum() {
		return this.orderNum;
	}

}
