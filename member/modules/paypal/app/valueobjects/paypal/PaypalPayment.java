package valueobjects.paypal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Configuration;
import play.Play;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.StringUtils;
import services.base.utils.Utils;
import dto.order.Order;
import dto.order.OrderDetail;
import entity.payment.PaymentBase;

;

public class PaypalPayment {
	private Order order;
	private List<OrderDetail> orderDetails;
	private PaymentBase paymentbase;
	private String postaddress;
	private String returnUrl;

	// final Map<String, String> map = new HashMap<String,String>();

	public Order getOrder() {
		return order;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public PaymentBase getPaymentbase() {
		return paymentbase;
	}

	public void setPaymentbase(PaymentBase paymentbase) {
		this.paymentbase = paymentbase;
	}

	public Map<String, String> getMap() {
		Configuration config = Play.application().configuration()
				.getConfig("paypal");
		if (StringUtils.isEmpty(returnUrl)) {
			returnUrl = config.getString("return");
		}
		String notifyurl = config.getString("notifyurl");

		Map<String, String> map = new HashMap<String, String>();
		map.put("form_key", this.getPaymentbase().getCfromkey());
		map.put("cmd", "_cart");
		map.put("paymentaction", "sale");
		map.put("return", returnUrl);
		map.put("upload", "1");
		map.put("item_name", this.getPaymentbase().getCitemname());
		map.put("page_style", "paypal");
		map.put("first_name", this.getOrder().getCfirstname());
		map.put("last_name", this.getOrder().getClastname());
		map.put("notify_url", notifyurl);
		map.put("business", this.getPaymentbase().getCbusiness());
		map.put("invoice", this.getOrder().getCordernumber());
		map.put("email", this.getOrder().getCemail());
		map.put("currency_code", this.getOrder().getCcurrency());
		map.put("country", this.getOrder().getCcountry());
		Integer i = 1;
		String currency = order.getCcurrency();
		for (OrderDetail od : this.getOrderDetails()) {
			String item_name = od.getCtitle();
			if (StringUtils.isEmpty(item_name)) {
				item_name = od.getCsku();
			}
			map.put("item_number" + "_" + i.toString(), od.getCsku());
			map.put("item_name" + "_" + i.toString(), item_name);
			map.put("quantity" + "_" + i.toString(), od.getIqty().toString());
			// map.put("amount" + "_" + i.toString(), String
			// .valueOf(new DoubleCalculateUtils(od.getFprice())
			// .doubleValue()));
			map.put("amount" + "_" + i.toString(),
					Utils.money(od.getFprice(), currency));

			i++;
		}
		if (this.getOrder().getFshippingprice() != null
				&& this.getOrder().getFshippingprice() != 0
				&& this.getOrder() != null) {
			map.put("item_number" + "_" + i.toString(), "shipping Cost");
			map.put("item_name" + "_" + i.toString(), "shipping Cost");
			map.put("quantity" + "_" + i.toString(), "1");
			// map.put("amount" + "_" + i.toString(),
			// String.valueOf(this.getOrder().getFshippingprice()));
			map.put("amount" + "_" + i.toString(),
					Utils.money(this.getOrder().getFshippingprice(), currency));

			i++;
		}
		if (this.getOrder().getFextra() != null
				&& this.getOrder().getFextra() < 0) {
			map.put("discount_amount_cart",
					Utils.money(new DoubleCalculateUtils(this.getOrder()
							.getFextra()).multiply(-1).doubleValue()));
		}
		// map.put("amount", String.valueOf(new DoubleCalculateUtils(this
		// .getOrder().getFgrandtotal()).doubleValue()));
		map.put("amount",
				Utils.money(this.getOrder().getFgrandtotal(), currency));

		return map;
	}

	public String getPostaddress() {
		return postaddress;
	}

	public void setPostaddress(String postaddress) {
		this.postaddress = postaddress;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}
