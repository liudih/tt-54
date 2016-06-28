package services.paypal;

import java.util.List;

import mapper.paypal.payment.PaymentMapper;
import mapper.paypal.payment.PaypaiReturnLogMapper;
import services.base.CurrencyService;
import services.base.utils.JsonFormatUtils;

import com.google.inject.Inject;

import dto.order.Order;
import entity.payment.PaymentBase;
import entity.payment.PaypaiReturn;
import entity.payment.PaypaiReturnLog;
import extensions.InjectorInstance;

public class PaymentServices {

	@Inject
	PaymentMapper paymentmapper;

	@Inject
	PaypaiReturnLogMapper paypaiReturnLogMapper;

	@Inject
	CurrencyService currencyService;

	public PaymentBase GetPayment(Order order) {
		double dprice = currencyService.exchange(order.getFgrandtotal(),
				order.getCcurrency(), "USD");
		return paymentmapper.getPaymentAccountBase(order.getIwebsiteid(),
				dprice);
	}

	public void InsertLog(Integer iwebsiteid, String corderid, String content) {
		paypaiReturnLogMapper.Insert(iwebsiteid, corderid, content,null);
	}
	/**
	 * @author lijun
	 * @param iwebsiteid
	 * @param corderid orderid 或者 orderNum
	 * @param content 日志内容
	 * @param transactionId 交易号
	 */
	public void InsertLog(Integer iwebsiteid, String corderid, String content,String transactionId) {
		paypaiReturnLogMapper.Insert(iwebsiteid, corderid, content,transactionId);
	}

	public PaypaiReturn getPaypaiReturnByOrderId(String orderId) {
		PaypaiReturnLog paypaiReturnLog = paypaiReturnLogMapper
				.getPaypaiReturnLogByOrderId(orderId);
		if (paypaiReturnLog != null) {
			PaypaiReturn paypaiReturn = JsonFormatUtils.jsonToBean(
					paypaiReturnLog.getCcontent(), PaypaiReturn.class);
			return paypaiReturn;
		} else {
			return null;
		}
	}

	public static PaymentServices getInstance() {
		return InjectorInstance.getInstance(PaymentServices.class);
	}
	
	/**
	 * 根据订单号，获取数据信息
	 * @param corderid
	 * @return
	 */
	public List<PaypaiReturnLog> getPaypaiReturnLogByOrderIds(String corderid){
		return paypaiReturnLogMapper.getPaypaiReturnLogByOrderIds(corderid);
	}
}
