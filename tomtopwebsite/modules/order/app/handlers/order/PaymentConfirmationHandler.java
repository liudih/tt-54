package handlers.order;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import services.base.CurrencyService;
import services.base.EmailAccountService;
import services.base.EmailTemplateService;
import services.base.utils.DateFormatUtils;
import services.base.utils.StringUtils;
import services.cart.IExtraLineService;
import services.dropship.DropShipBaseUpdateService;
import services.order.IOrderEnquiryService;
import services.order.OrderUpdateService;
import services.product.inventory.InventoryTypeEnum;
import services.product.inventory.InventoryUpdateService;
import services.shipping.IShippingMethodService;
import valueobjects.order_api.cart.ExtraLine;
import base.util.mail.EmailUtil;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import dto.EmailAccount;
import email.model.EmailType;
import email.util.EmailSpreadUtil;
import email.valueobjects.OrderPaymentFields;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.product.InventoryHistory;
import events.order.PaymentConfirmationEvent;
import extensions.order.IOrderExtrasProvider;

public class PaymentConfirmationHandler {
	@Inject
	private EmailTemplateService emailTemplateService;
	@Inject
	private EmailAccountService emailAccountService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private IShippingMethodService shippingMethodService;
	@Inject
	private IOrderEnquiryService orderEnquiryService;
	@Inject
	private InventoryUpdateService inventoryUpdateService;
	@Inject
	private OrderUpdateService updateService;
	@Inject
	private DropShipBaseUpdateService dropshipBaseService;
	@Inject
	private IExtraLineService extraLineService;
	@Inject
	private Set<IOrderExtrasProvider> extrasProviders;
	@Inject
	EmailSpreadUtil emailSpread;

	@Subscribe
	public void onPaymentConfirmation(PaymentConfirmationEvent event) {
		try {
			updateService.updatePaymentTime(event.getOrderValue().getOrder()
					.getIid());
			Logger.debug("updatePaymentTime======orderid=={}",event.getOrderValue().getOrder().getIid());
			Boolean flag = false;
			int count = 1;
			while (!flag) {
				flag = sendEmail(event);
				if (null == flag || count >= 5) {
					break;
				}
				count++;
			}
		} catch (Exception e) {
			Logger.error("onPaymentConfirmation", e);
		}
		try {
			changeInventory(event);
		} catch (Exception e) {
			Logger.error("onPaymentConfirmation", e);
		}
		try {
			updateDropshipLevel(event);
		} catch (Exception e) {
			Logger.error("onPaymentConfirmation", e);
		}
		try {
			orderExtraOperation(event);
		} catch (Exception e) {
			Logger.error("onPaymentConfirmation", e);
		}
	}

	private void orderExtraOperation(PaymentConfirmationEvent event) {
		String cartID = event.getOrderValue().getOrder().getCcartid();
		if (StringUtils.isEmpty(cartID)) {
			return;
		}
		Map<String, ExtraLine> extras = extraLineService
				.getExtraLinesByCartId(cartID);
		Map<String, IOrderExtrasProvider> providers = Maps.uniqueIndex(
				extrasProviders, p -> p.getId());
		extras.forEach((id, line) -> {
			IOrderExtrasProvider provider = providers.get(id);
			if (provider != null) {
				provider.payOperation(line);
			}
		});
	}

	public void changeInventory(PaymentConfirmationEvent event) {
		List<OrderDetail> details = event.getOrderValue().getDetails();
		int websiteID = event.getOrderValue().getOrder().getIwebsiteid();
		String remark = "sale for order: "
				+ event.getOrderValue().getOrder().getIid().toString();
		for (OrderDetail d : details) {
			InventoryHistory ih = new InventoryHistory();
			ih.setClistingid(d.getClistingid());
			ih.setCreference(remark);
			ih.setIwebsiteid(websiteID);
			ih.setIqty(d.getIqty() * -1);
			ih.setCtype(InventoryTypeEnum.SALE.getValue());
			inventoryUpdateService.insert(ih);
		}
	}

	/**
	 * 发送邮件给用户
	 * 
	 * @param toeamil
	 * @return
	 */
	public boolean sendEmail(PaymentConfirmationEvent event) {
		Order order = event.getOrderValue().getOrder();
		Integer language = event.getLanguageId();
		Integer websiteid = order.getIwebsiteid();
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(websiteid);
		String address = order.getCstreetaddress() + " " + order.getCcity()
				+ " " + order.getCprovince() + " " + order.getCcountry();
		String date = DateFormatUtils.getInstance("EEE, d MMMM yyyy").getDate(
				order.getDcreatedate());
		String symbol = currencyService.getCurrencyByCode(order.getCcurrency())
				.getCsymbol();
		String shippingMethod = shippingMethodService.getShippingMethodDetail(
				order.getIshippingmethodid(), language).getCtitle();

		OrderPaymentFields orderPaymentFields = new OrderPaymentFields(
				EmailType.PAYMENT_ORDER.getType(), language, order
						.getCordernumber().toString(), order.getCfirstname(),
				address, date, symbol, shippingMethod, Double.toString(order
						.getFgrandtotal()));

		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(
					orderPaymentFields, order.getIwebsiteid());
			if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("title and content is null ,can not send email");
				return false;
			}
		} catch (Exception e) {
			Logger.error("can not deal with verify email content");
			e.printStackTrace();
		}
		if (emailaccount == null) {
			Logger.info("sendEmail email server account null!");
			return false;
		}
		//return EmailUtil.send(title, content, emailaccount, order.getCemail());
		return emailSpread.send(emailaccount.getCemail(), order.getCemail(), title,
				content);
	}

	public boolean updateDropshipLevel(PaymentConfirmationEvent event) {
		String cemail = event.getOrderValue().getOrder().getCemail();
		Integer iwebsiteid = event.getOrderValue().getOrder().getIwebsiteid();
		Double memberGrandtotal = orderEnquiryService
				.getMemberGrandtotal(cemail);
		Logger.debug("cemail: " + cemail + " iwebsiteid:" + iwebsiteid
				+ "memberGrandtotal:" + memberGrandtotal);
		return dropshipBaseService.updateLevelByEmail(cemail, iwebsiteid,
				memberGrandtotal);
	}

}
