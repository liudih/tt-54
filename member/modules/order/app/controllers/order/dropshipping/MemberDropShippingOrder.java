package controllers.order.dropshipping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.utils.ExcelUtils;
import services.base.utils.StringUtils;
import services.order.IOrderCountService;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderPackService;
import services.order.OrderUpdateService;
import services.order.dropShipping.DropShippingOrderDisplay;
import services.order.member.MemberOrderProvider;
import services.order.member.OrderDetailDisplay;
import valueobjects.order_api.DropShipOrderMessage;
import valueobjects.order_api.OrderCount;
import authenticators.member.MemberLoginAuthenticator;

import com.google.common.collect.Lists;

import dto.order.Order;
import dto.order.OrderPack;
import dto.order.OrderStatus;
import forms.order.DropShipOrderSearchForm;
import forms.order.OrderForm;

@Authenticated(MemberLoginAuthenticator.class)
public class MemberDropShippingOrder extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	IOrderEnquiryService enquiry;

	@Inject
	MemberOrderProvider provider;

	@Inject
	FoundationService foundationService;

	@Inject
	IOrderCountService countService;

	@Inject
	DropShippingOrderDisplay dropShippingOrderDisplay;

	@Inject
	OrderUpdateService updateService;

	@Inject
	IOrderStatusService statusSerice;

	@Inject
	IOrderService orderService;

	@Inject
	IOrderEnquiryService orderEnquiryService;

	@Inject
	IOrderStatusService orderStatusService;

	@Inject
	OrderPackService orderPackService;
	
	private final int PageSize= 20;

	public Result index() {
		String email = foundation.getLoginContext().getMemberID();
		Integer siteId = foundationService.getSiteID();
		OrderCount count = countService.getDropShippingOrderCountByEmail(email,
				siteId, 1);
		OrderForm form = new OrderForm();
		form.setUseremail(email);
		form.setSiteId(siteId);
		form.setPageSize(PageSize);
		List<DropShipOrderMessage> dropShipOrderMessages = orderEnquiryService
				.searchDropShipOrders(form);
		List<Order> orders = Lists.transform(dropShipOrderMessages,
				dropShipOrderMessage -> {
					Order order = new Order();
					BeanUtils.copyProperties(dropShipOrderMessage, order);
					return order;
				});
		int pageCount = orderEnquiryService.searchDropShipOrderCount(form);
		int page = 0;
		if ((pageCount % form.getPageSize()) > 0) {
			page = pageCount / form.getPageSize() + 1;
		} else {
			page = pageCount / form.getPageSize();
		}
		Html result = dropShippingOrderDisplay.getHtml(orders, form.getIsShow(), page,
				form.getPageNum(), "dropshipping");
		Map<String, OrderStatus> nameMap = orderStatusService.getNameMap();
		Html orders1 = views.html.order.member.dropship.order_display.render(result,
				count, nameMap);
		return ok(views.html.order.member.dropship.dropshipping_order_list
				.render(orders1, count));
	}

	public Result search() {
		Form<OrderForm> orderForm = Form.form(
				OrderForm.class).bindFromRequest();
		if (orderForm.hasErrors()) {
			return badRequest("form error: " + orderForm.errorsAsJson());
		}
		OrderForm form = orderForm.get();
		String email = foundation.getLoginContext().getMemberID();
		form.setUseremail(email);
		form.setPageSize(PageSize);
		List<DropShipOrderMessage> dropShipOrderMessages = orderEnquiryService
				.searchDropShipOrders(form);
		List<Order> orders = Lists.transform(dropShipOrderMessages,
				dropShipOrderMessage -> {
					Order order = new Order();
					BeanUtils.copyProperties(dropShipOrderMessage, order);
					return order;
				});
		int totalCount = orderEnquiryService.searchDropShipOrderCount(form);
		int pages = totalCount / PageSize + ((totalCount % PageSize > 0) ? 1 : 0);
		return ok(dropShippingOrderDisplay.getHtml(orders, form.getIsShow(), pages,
				form.getPageNum(), "dropshipping"));
	}

	public Result download(String interval, String status, String orderNumber,
			String productName, String transactionId, String firstName) {
		OrderForm form = new OrderForm();
		if (StringUtils.notEmpty(interval)) {
			form.setInterval(Integer.parseInt(interval));
		}
		if (StringUtils.notEmpty(status)) {
			form.setStatus(status);
		}
		if (StringUtils.notEmpty(orderNumber)) {
			form.setOrderNumber(orderNumber);
		}
		if (StringUtils.notEmpty(productName)) {
			form.setProductName(productName);
		}
		if (StringUtils.notEmpty(transactionId)) {
			form.setTransactionId(transactionId);
		}
		if (StringUtils.notEmpty(firstName)) {
			form.setFirstName(firstName);
		}
		List<DropShipOrderMessage> dropShipOrderMessages = orderEnquiryService
				.searchDropShipOrders(form);
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> title = new ArrayList<Object>();
		title.add("orderNumber");
		title.add("status");
		title.add("trackingnumber");
		data.add(title);
		Map<Integer, OrderStatus> orderStatusMap = orderStatusService
				.getIdMap();
		for (DropShipOrderMessage order : dropShipOrderMessages) {
			ArrayList<Object> row = new ArrayList<Object>();
			row.add(order.getUserorderid());
			if (order.getIstatus() != null) {
				row.add(orderStatusMap.get(order.getIstatus()).getCname());
			} else {
				row.add(null);
			}
			String ctrackingnumber = "";
			List<OrderPack> orderPack = orderPackService.getByOrderId(order
					.getIid());
			if (!orderPack.isEmpty()) {
				for (OrderPack orPack : orderPack) {
					ctrackingnumber += orPack.getCtrackingnumber() + ";";
				}
			}
			row.add(ctrackingnumber);
			data.add(row);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String filename = "order-list-" + sdf.format(new Date()) + ".xlsx";
		ExcelUtils excel = new ExcelUtils();
		byte[] tmpFile = excel.arrayToXLSX(data);
		response().setHeader("Content-disposition",
				"attachment; filename=" + filename);
		return ok(tmpFile).as("application/vnd.ms-excel");
	}
}