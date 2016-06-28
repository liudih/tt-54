package events.payment;

/**
 * 
 * @author lijun
 *
 */
public class PaymentEvent {
	// 订单号
	final String orderNum;
	// 订单状态(该状态是tomtop数据库t_order_status中的状态),用于更新订单状态
	final String status;
	// 交易id
	final String transactionId;
	// 接受付款的账号
	final String receiverAccount;
	//实付金额,会用该参数和数据库对比,如果不相等则不修改订单状态
	final String amt;
	//支付工具(eg. paypal)
	final String paymentId;

	public PaymentEvent(String orderNum, String status, String transactionId,
			String receiverAccount,String amt,String paymentId) {
		this.orderNum = orderNum;
		this.status = status;
		this.transactionId = transactionId;
		this.receiverAccount = receiverAccount;
		this.amt = amt;
		this.paymentId = paymentId;
	}

	
	public String getPaymentId() {
		return paymentId;
	}


	public String getOrderNum() {
		return orderNum;
	}

	public String getStatus() {
		return status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getReceiverAccount() {
		return receiverAccount;
	}

	public String getAmt() {
		return amt;
	}

}
