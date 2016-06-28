package services.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import mapper.order.PaymentAccountMapper;

import org.elasticsearch.common.collect.Maps;

import play.Logger;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderDetail;
import security.Encryption;
import services.base.CurrencyService;
import services.base.SystemParameterService;
import services.base.utils.JsonFormatUtils;
import services.base.utils.StringUtils;
import services.member.IMemberEnquiryService;
import services.order.IBillDetailService;
import services.order.IOrderEnquiryService;
import valueobjects.payment.OceanPaymentResult;
import valueobjects.payment.SummaryInformation;
import extensions.order.IOrderExtrasProvider;
import extensions.payment.impl.oceanpayment.OceanPaymentCreditPaymentProvider;

public class OceanPaymentService {

	@Inject
	IBillDetailService billDetailService;

	@Inject
	Set<IOrderExtrasProvider> providers;

	@Inject
	IOrderEnquiryService enquiryService;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	Encryption encryption;

	@Inject
	PaymentAccountMapper accountMapper;

	@Inject
	CurrencyService currencyService;

	@Inject
	IOrderEnquiryService orderEnquiryService;
	
	@Inject
	SystemParameterService systemParameterService;

	public List<SummaryInformation> getInformations(Integer orderId) {
		List<SummaryInformation> list = new ArrayList<SummaryInformation>();
		List<BillDetail> temp = billDetailService.getExtraBill(orderId);
		Map<String, BillDetail> map = Maps.uniqueIndex(temp, e -> e.getCtype());
		for (IOrderExtrasProvider provider : providers) {
			BillDetail detail = map.get(provider.getId());
			if (null != detail) {
				SummaryInformation information = new SummaryInformation();
				information.setDisplayOrder(provider.getDisplayOrder());
				information.setMoney(detail.getFtotalprice());
				information.setType(provider.getId());
				information.setMsg(detail.getCmsg());
				list.add(information);
			}
		}
		Collections.sort(list,
				(a, b) -> a.getDisplayOrder() - b.getDisplayOrder());
		return list;
	}

	/**
	 * 将order post到ocean所需的signvalue
	 *
	 * @param form
	 * @param secureCode
	 * @return
	 */
	public String getOceanPostSignValue(LinkedHashMap<String, String> form,
			String secureCode) {
		String text = form.get("account") + form.get("terminal")
				+ form.get("backUrl") + form.get("order_number")
				+ form.get("order_currency") + form.get("order_amount")
				+ form.get("billing_firstName") + form.get("billing_lastName")
				+ form.get("billing_email") + secureCode;
		return encryption.encodeSHA256(text);
	}

	/**
	 * 验证ocean返回数据所需的signvalue
	 *
	 * @param form
	 * @param secureCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getOceanValidSignValue(OceanPaymentResult result,
			String orderId) {
		Order order = orderEnquiryService.getOrderById(orderId);
		if (order == null) {
			return null;
		}
		String ccy = order.getCcurrency();
		double usdgrandprice = currencyService.exchange(order.getFgrandtotal(), ccy, "USD");
		String paymentid = order.getCpaymentid();	//支付方式
		String paramValue = systemParameterService.getSystemParameter(1, null,
				"credit_"+ccy);
		double grandprice = order.getFgrandtotal();
		if(paramValue==null){
			grandprice = usdgrandprice;
			paramValue = systemParameterService.getSystemParameter(1, null,
					"credit_USD","300");
		}
		double pricelimit = Double.parseDouble(paramValue); 
		
		Logger.debug("pricelimit====={},{}",ccy,pricelimit);
		//如果大于某金额就进入信用卡3D验证支付
		if(grandprice>pricelimit){
			Logger.debug("goto  credit_3D_payment+++ completed");
			paymentid = OceanPaymentCreditPaymentProvider.credit_3D_payment;
		}
		String accountString = accountMapper.getAccount(order.getIwebsiteid(),
				usdgrandprice,paymentid);
		if (StringUtils.isEmpty(accountString)) {
			return null;
		}
		LinkedHashMap<String, String> account = JsonFormatUtils.jsonToBean(
				accountString, LinkedHashMap.class);
		String text = result.getAccount() + result.getTerminal()
				+ result.getOrder_number() + result.getOrder_currency()
				+ result.getOrder_amount() + result.getOrder_notes()
				+ result.getCard_number() + result.getPayment_id()
				+ result.getPayment_authType() + result.getPayment_status()
				+ result.getPayment_details() + result.getPayment_risk()
				+ account.get("secureCode");
		return encryption.encodeSHA256(text);
	}

	public Map<String, String> getOceanProductMap(List<OrderDetail> list) {
		Map<String, String> map = Maps.newHashMap();
		StringBuffer productSku = new StringBuffer();
		StringBuffer productName = new StringBuffer();
		StringBuffer productNum = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			OrderDetail detail = list.get(i);
			if (i == list.size() - 1) {
				productSku.append(detail.getCsku());
				productName.append(detail.getCtitle());
				productNum.append(detail.getIqty());
			} else {
				productSku.append(detail.getCsku() + ";");
				productName.append(detail.getCtitle() + ";");
				productNum.append(detail.getIqty() + ";");
			}
		}
		map.put("productSku", productSku.toString());
		map.put("productName", productName.toString());
		map.put("productNum", productNum.toString());
		return map;
	}
}
