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
	  Environment.SANDBOX,
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
		LoginContext loginCtx = foundation.getLoginContext();
		boolean isLogin = loginCtx.isLogin();
		if (!isLogin) {
			paymentStatus.setRe(0);
			paymentStatus.setMsg("no login");
			return paymentStatus;
		}
		String email = loginCtx.getMemberID();
		PaymentContext paymentContext = orderService.getPaymentContext(orderNum,
				foundation.getLanguage());
		if(paymentContext==null || paymentContext.getOrder()==null){
			paymentStatus.setRe(0);
			paymentStatus.setMsg("order is null");
			return paymentStatus;
		}
		Order order = paymentContext.getOrder().getOrder();
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
        .submitForSettlement(true)	//预授权交易
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
        .done();
		
		if(billid!=null && !"".equals(billid)){
			MemberAddress address = addressService.getMemberAddressById(Integer
					.valueOf(billid));
			Country country = countryService.getCountryByCountryId(address
					.getIcountry());
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
	        .countryCodeAlpha2(country.getCshortname())
	        .done();
		}
		
		String test_request = com.braintreegateway.test.Nonce.Transactable;
		
		try{
			com.braintreegateway.Result<Transaction> result = BraintreeService.gateway.transaction().sale(request);
			Logger.debug("result===={}",result.getParameters());
			if (result.isSuccess()) {
			    // transaction successfully voided
				Logger.debug("ssssssssssssssss");
				Transaction transaction = result.getTarget();
				String orderid = transaction.getOrderId();
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
				
				//支付方式更新
				paymentId = paymentId + transaction.getPaymentInstrumentType();
				
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
				
			} else {
				StringBuilder sb = new StringBuilder();
			    for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
			        Logger.debug(error.getMessage());
			        sb.append(error.getMessage());
			    }
			    paymentStatus.setRe(0);
			    paymentStatus.setMsg(sb.toString());
			}
		}catch(BraintreeException e){
			paymentStatus.setRe(0);
		    paymentStatus.setMsg(e.toString());
		}
		return paymentStatus;
	}
}
