package services.payment;

import java.math.BigDecimal;
import java.util.List;

import play.Logger;
import play.Play;
import play.libs.Json;
import services.ICountryService;
import services.IWebsiteService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.member.address.IAddressService;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderUpdateService;
import valueobjects.base.LoginContext;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.payment.BraintreePaymentStatus;
import valueobjects.payment.BraintreeToken;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.PaymentInstrumentType;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.braintreegateway.exceptions.BraintreeException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.Transaction.Status;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import dto.Country;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.order.OrderDetail;
import events.payment.PaymentEvent;

public class BraintreeService {
	
	@Inject
	IOrderService orderService;
	@Inject
	FoundationService foundation;
	@Inject
	IOrderStatusService statusService;
	@Inject
	OrderUpdateService updateService;
	@Inject
	IAddressService addressService;
	@Inject
	ICountryService countryService;
	@Inject
	private IPaymentConfirmationService paymentEnquiry;
	@Inject
	private IOrderEnquiryService orderEnquiry;
	@Inject
	private EventBus eventBus;
	@Inject
	private IWebsiteService websiteService;

	public static BraintreeGateway gateway = new BraintreeGateway(
	  Environment.PRODUCTION,
	  Play.application().configuration().getString("braintree.merchant_id"),
	  Play.application().configuration().getString("braintree.public_key"),
	  Play.application().configuration().getString("braintree.private_key")
	);
	
	public BraintreeToken getToken(){
		BraintreeToken bt = new BraintreeToken();
		try{
			bt.setToken(gateway.clientToken().generate());
		}catch(BraintreeException e){
			bt.setRe(0);
			bt.setMsg(e.toString());
			Logger.debug("getToken exception ======={}",e.fillInStackTrace());
			return bt;
		}
		bt.setRe(1);
		return bt;
	}
	
	
	public BraintreePaymentStatus doCheckoutPayment(String orderNum, String billid, String method, String deviceData){
		BraintreePaymentStatus paymentStatus = new BraintreePaymentStatus();
		if(StringUtils.isEmpty(orderNum) || StringUtils.isEmpty(method) || StringUtils.isEmpty(deviceData)){
			paymentStatus.setRe(0);
			paymentStatus.setMsg("Incomplete parameter!");
			return paymentStatus;
		}
//		LoginContext loginCtx = foundation.getLoginContext();
//		boolean isLogin = loginCtx.isLogin();
//		if (!isLogin) {
//			paymentStatus.setRe(0);
//			paymentStatus.setMsg("no login");
//			return paymentStatus;
//		}
//		String email = loginCtx.getMemberID();
		PaymentContext paymentContext = orderService.getPaymentContext(orderNum,
				foundation.getLanguage());
		if(paymentContext==null || paymentContext.getOrder()==null){
			paymentStatus.setRe(0);
			paymentStatus.setMsg("order is null");
			return paymentStatus;
		}
		Order order = paymentContext.getOrder().getOrder();
		String email = order.getCmemberemail();
		// 检查订单是否已经支付完成
		boolean isAlreadyPaid = orderService.isAlreadyPaid(orderNum);
		if (isAlreadyPaid) {
			paymentStatus.setRe(0);
			paymentStatus.setMsg("order is already paid");
			return paymentStatus;
		}
		//设置订单为padding状态
		String paymentId = "braintree";
		Integer status = statusService
				.getIdByName(IOrderStatusService.PAYMENT_PENDING);
		boolean isSuccess = updateService.replaceOrder(order.getIid(),
				status, paymentId, email);
		
		TransactionRequest request = new TransactionRequest()
	    .amount(new BigDecimal(order.getFgrandtotal()+""))
	    .paymentMethodNonce(method)
	    .options()
        .submitForSettlement(false)	//预授权交易
        .done()
        .deviceData(deviceData)	//激活风控
        .orderId(orderNum)
        .shippingAddress()	//邮寄地址
        .firstName(order.getCfirstname())
        .lastName(order.getClastname())
        .company("")
        .streetAddress(order.getCstreetaddress())
        .extendedAddress("")
        .locality(order.getCcity())
        .region(order.getCprovince())		//可能paypal要美国的州的简拼？
        .postalCode(order.getCpostalcode())
        .countryCodeAlpha2(order.getCcountrysn())
        .done()
        .customer()
        .email(email)
        .done();
		
		if(billid!=null && !"".equals(billid)){
			MemberAddress address = addressService.getMemberAddressById(Integer
					.valueOf(billid));
			if(address!=null && address.getIcountry()!=null){
				Country country = countryService.getCountryByCountryId(address
						.getIcountry());
				//国家代码
				String countryCode = "";
				if(country!=null){
					countryCode = country.getCshortname();
				}
				request = request
				.billingAddress()
		        .firstName(address.getCfirstname())
		        .lastName(address.getClastname())
		        .company("")
		        .streetAddress(address.getCstreetaddress())
		        .extendedAddress("")
		        .locality(address.getCcity())
		        .region(address.getCprovince())
		        .postalCode(address.getCpostalcode())
		        .countryCodeAlpha2(countryCode)
		        .done();
			}
		}
		
		String test_request = com.braintreegateway.test.Nonce.Transactable;
		
		try{
			com.braintreegateway.Result<Transaction> result = BraintreeService.gateway.transaction().sale(request);
			Transaction transaction = result.getTransaction();
			if(transaction==null){
				transaction = result.getTarget();
			}
			Logger.debug("result11===={}",Json.toJson(result));
			if(transaction==null){
				paymentStatus.setRe(0);
			    paymentStatus.setMsg("not find transaction");
			    return paymentStatus;
			}
			String orderid = transaction.getOrderId();
			//支付方式更新
			paymentId = paymentId + transaction.getPaymentInstrumentType();
			Logger.debug("orderid==="+orderid);
			if (result.isSuccess()) {
				String tid = transaction.getId();
				//AUTHORIZATION_EXPIRED, AUTHORIZED, AUTHORIZING, FAILED, GATEWAY_REJECTED, PROCESSOR_DECLINED, SETTLED, SETTLEMENT_CONFIRMED, SETTLEMENT_DECLINED, SETTLEMENT_PENDING, SETTLING, SUBMITTED_FOR_SETTLEMENT, UNRECOGNIZED, VOIDED;
				String tstatus = transaction.getStatus().toString();
				Logger.debug("transaction.getId()==="+transaction.getId()+",tstatus==="+tstatus);
				//授权成功，检查Kount结果
				if("AUTHORIZED".equals(tstatus)){
					Logger.debug("Status.AUTHORIZED====");
					String risk = transaction.getRiskData().getDecision();
					Logger.debug("risk==="+risk);
					//批准交易，进行扣款
					if("Approve".equals(risk) || "Not Evaluated".equals(risk)){
						com.braintreegateway.Result<Transaction> settlementResult = BraintreeService.gateway.transaction().submitForSettlement(tid);
						Transaction resultTransaction = settlementResult.getTransaction();
						if(resultTransaction==null){
							resultTransaction = settlementResult.getTarget();
						}
						if(resultTransaction==null){
							paymentStatus.setMsg("not find transaction");
						    return paymentStatus;
						}
						String resultStatus = resultTransaction.getStatus().toString();
						Logger.debug("settlementResult==={}",resultStatus);
						if("SUBMITTED_FOR_SETTLEMENT".equals(resultStatus)){
							Logger.debug("SUBMITTED_FOR_SETTLEMENT++++++");
							
							//订单设置成功
							paymentEnquiry.confirmPayment(orderid, transaction.getId(), IOrderStatusService.PAYMENT_CONFIRMED);
							// 支付成功传递赠送积分事件
							List<OrderDetail> detailsPoint = orderEnquiry
									.getOrderDetails(orderid);
							if (null != order) {
								Integer webisteId = order.getIwebsiteid();
								Integer ilanguageid = websiteService.getWebsite(webisteId)
										.getIlanguageid();
								eventBus.post(new events.order.PaymentConfirmationEvent(
										new valueobjects.order_api.OrderValue(order,
												detailsPoint), transaction.getId(),
										ilanguageid));
							}
							Logger.debug("---------->server braintree paypment confirmed,ordernum={}",
									order.getCordernumber());
							
							//订单成功，更改订单paymentstatus状态
							PaymentEvent event = new PaymentEvent(orderid,
									IOrderStatusService.PAYMENT_CONFIRMED , transaction.getId(), transaction.getMerchantAccountId(),
									transaction.getAmount()+"", paymentId);
							eventBus.post(event);
							Logger.debug("transaction====={}",Json.toJson(transaction));
							transaction.getPaymentInstrumentType().equals(PaymentInstrumentType.PAYPAL_ACCOUNT);
							// false
							transaction.getPaymentInstrumentType().equals(PaymentInstrumentType.CREDIT_CARD);
							Logger.debug("status===={}",transaction.getStatus());
							Logger.debug("type===={}",transaction.getPaymentInstrumentType());
							paymentStatus.setRe(1);
							paymentStatus.setMsg(resultStatus);
						}else{
							paymentStatus.setRe(0);
							paymentStatus.setMsg("AUTHORIZED and Approve and fail."+resultStatus);
						}
					}
					//人工审查交易
					else if("Review".equals(risk) || "Escalate".equals(risk)){
						//订单设置风控拦截状态
						paymentEnquiry.confirmPayment(orderid, transaction.getId(), IOrderStatusService.PAYMENT_PROCESSING);
						//支付方式更新
						PaymentEvent event = new PaymentEvent(orderid,
								IOrderStatusService.PAYMENT_PENDING , transaction.getId(), transaction.getMerchantAccountId(),
								transaction.getAmount()+"", paymentId);
						eventBus.post(event);
						paymentStatus.setRe(1);
						paymentStatus.setMsg(risk);
						//将订单状态设成"ON HOLD"
			            //发送通知给风控人员进行人工审查
					}
		            //处理无效状态
		            //Kount Decision = 'Declined'的情况不应该出现，因为交易结果会失败
		            //Kount Decision 为空可能出现，因为某些支付方式，比如Apple Pay不支持Kount
					else{
						paymentStatus.setRe(0);
						paymentStatus.setMsg("AUTHORIZED  but "+risk);
					}
				}
				// 交易失败
		        // 处理订单
				else if("failed".equals(tstatus) || "gateway_rejected".equals(tstatus)
						|| "processor_declined".equals(tstatus)){
					
					paymentStatus.setRe(0);
					paymentStatus.setMsg("NO AUTHORIZED, failed."+tstatus);
					return paymentStatus;
				}else{
					paymentStatus.setRe(0);
					paymentStatus.setMsg("NO AUTHORIZED and else failed."+tstatus);
				}
				
			} else {
				Logger.debug("errorrrrrrr==={}");
				
				String risk = transaction.getRiskData().getDecision();
				Logger.debug("risk==={}",risk);
				Logger.debug("transaction==getStatus=={}",transaction.getStatus());
				
				
				StringBuilder sb = new StringBuilder();
			    for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
			        Logger.debug(error.getMessage());
			        sb.append(error.getMessage());
			    }
			    paymentStatus.setRe(0);
			    paymentStatus.setMsg("Transaction error."+sb.toString());
			}
		}catch(BraintreeException e){
			paymentStatus.setRe(0);
		    paymentStatus.setMsg("BraintreeException."+e.toString());
		}
		return paymentStatus;
	}
}
