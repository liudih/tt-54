package services.payment;

public interface IPaymentConfirmationService {

	/**
	 *
	 * @param orderId
	 * @param transactionId交易号
	 * @author luojiaheng
	 */
	public abstract boolean confirmPayment(String orderId,
			String transactionId, int langId);

	/**
	 *
	 * @param orderId
	 * @param transactionId交易号
	 * @author luojiaheng
	 */
	public abstract boolean confirmPayment(Integer orderId, String transactionId);

	public abstract boolean confirmPayment(String orderNumber,
			String transactionId);

	/**
	 *
	 * @param orderId
	 * @param transactionId交易号
	 * @author luojiaheng
	 */
	public abstract boolean confirmPayment(String orderId,
			String transactionId, String status);

}