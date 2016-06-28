package controllers.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.member.IMemberEnquiryService;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import services.product.ProductEnquiryService;
import controllers.InterceptActon;
import forms.order.MemberOrderForm;

@With(InterceptActon.class)
public class Application extends Controller {
	@Inject
	IMemberEnquiryService memberEnquiryService;
	@Inject
	IOrderEnquiryService orderEnquiryService;
	@Inject
	IOrderStatusService orderStatusService;
	@Inject
	ProductEnquiryService productEnquiryService;

	public Result index() {
		MemberOrderForm memberOrderForm = new MemberOrderForm();
		Date start = null;
		Date end = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		start = calendar.getTime();
		Integer newOrderCount = orderEnquiryService.searchOrderCount(
				memberOrderForm, start, end);
		memberOrderForm.setStatus(orderStatusService
				.getIdByName(orderStatusService.PAYMENT_CONFIRMED));
		ArrayList<Integer> orderStatus = new ArrayList<Integer>();
		orderStatus.add(orderStatusService
				.getIdByName(orderStatusService.PAYMENT_CONFIRMED));
		orderStatus.add(orderStatusService
				.getIdByName(orderStatusService.PROCESSING));
		orderStatus.add(orderStatusService
				.getIdByName(orderStatusService.ON_HOLD));
		orderStatus.add(orderStatusService
				.getIdByName(orderStatusService.DISPATCHED));
		orderStatus.add(orderStatusService
				.getIdByName(orderStatusService.COMPLETED));
		Integer newPaymentConfirmedOrder = orderEnquiryService
				.getOrderCountByDateAndStatus(start, end, orderStatus);
		Integer newarrivalsCount = productEnquiryService.selectNewProductCount(
				start, end);
		Integer newMemberCount = memberEnquiryService
				.searchMemberCountByDate(start, end);
		return ok(views.html.manager.index.render(newMemberCount,
				newOrderCount, newPaymentConfirmedOrder, newarrivalsCount));
	}

}