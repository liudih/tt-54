package valueobjects.paypal_api;

import java.util.Map;

/**
 * 
 * @author lijun
 *
 */
public interface IPaypalPaymentStatus extends IPaymentStatus{
	/** 以下几种状态认为支付成功 **/
	// The payment has been completed, and the funds have been added
	// successfully to your account balance
	public static final String COMPLETED = "Completed";
	// A payment has been accepted
	public static final String PROCESSED = "Processed";
	// A reversal has been canceled; for example, when you win a dispute and the
	// funds for the reversal have been returned to you
	public static final String CANCELED_REVERSAL = "Canceled-Reversal";

	/** 以下状态付款未成功 **/
	// No status
	public static final String None = "None";
	// The payment is pending. See the PendingReason field for more information.
	public static final String PENDING = "Pending";

	// You denied the payment. This happens only if the payment was previously
	// pending because of possible reasons described for the PendingReason
	// element.
	public static final String DENIED = "Denied";
	// the authorization period for this payment has been reached.
	public static final String EXPIRED = "Expired";
	// The payment has failed. This happens only if the payment was made from
	// your buyer's bank account
	public static final String FAILED = "Failed";
	// The transaction has not terminated, e.g. an authorization may be awaiting
	// completion.
	public static final String PROGRESSING = "In-Progress";
	// The payment has been partially refunded. Pending – The payment is
	// pending. See the PendingReason field for more information.
	public static final String PARTIALLY_REFUNDED = "Partially-Refunded";
	// You refunded the payment.
	public static final String REFUNDED = "Refunded";
	// A payment was reversed due to a chargeback or other type of reversal. The
	// funds have been removed from your account balance and returned to the
	// buyer. The reason for the reversal is specified in the ReasonCode
	// element. Processed – A payment has been accepted
	public static final String REVERSED = "Reversed";
	// An authorization for this transaction has been voided.
	public static final String VOIDED = "Voided";
	// The payment has been completed, and the funds have been added
	// successfully to your pending balance. See the PAYMENTINFO_n_HOLDDECISION
	// field for more information
	public static final String COMPLETED_FUNDS_HELD = "Completed-Funds-Held";

	/**
	 * 获取paypal返回的响应信息
	 * 
	 * @return
	 */
	public Map<String, String> getPaypalReply();
}
