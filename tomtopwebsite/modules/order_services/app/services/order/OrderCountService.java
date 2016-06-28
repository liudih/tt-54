package services.order;

import java.util.List;

import javax.inject.Inject;

import com.google.api.client.util.Lists;

import valueobjects.order_api.OrderCount;
import forms.order.DropShipOrderSearchForm;
import forms.order.OrderForm;

public class OrderCountService implements IOrderCountService {
	@Inject
	IOrderEnquiryService enquiryService;
	@Inject
	IOrderStatusService orderStatusService;

	/* (non-Javadoc)
	 * @see services.order.IOrderCountService#getCountByEmail(java.lang.String, java.lang.Integer, java.lang.Integer, boolean)
	 */
	@Override
	public OrderCount getCountByEmail(String email, Integer siteId,
			Integer isShow, boolean isNormal) {
		Integer all = enquiryService.countByEmailAndStatus(email, null, siteId,
				isShow, isNormal);
		Integer pending = enquiryService.countByEmailAndStatus(email,
				IOrderStatusService.PAYMENT_PENDING, siteId, isShow, isNormal);
		Integer confirmed = enquiryService.countByEmailAndStatus(email,
				IOrderStatusService.PAYMENT_CONFIRMED, siteId, isShow, isNormal);
		Integer processing = enquiryService.countByEmailAndStatus(email,
				IOrderStatusService.PROCESSING, siteId, isShow, isNormal);
		Integer onHold = enquiryService.countByEmailAndStatus(email,
				IOrderStatusService.ON_HOLD, siteId, isShow, isNormal);
		Integer dispatched = enquiryService.countByEmailAndStatus(email,
				IOrderStatusService.DISPATCHED, siteId, isShow, isNormal);
		Integer cancelled = enquiryService.countByEmailAndStatus(email,
				IOrderStatusService.ORDER_CANCELLED, siteId, isShow, isNormal);
		Integer refunded = enquiryService.countByEmailAndStatus(email,
				IOrderStatusService.REFUNDED, siteId, isShow, isNormal);
		Integer recycle = enquiryService.countByEmailAndStatus(email, null,
				siteId, 2, isNormal);

		OrderCount count = new OrderCount();

		count.setAll(all);
		count.setCancelled(cancelled);
		count.setConfirmed(confirmed);
		count.setDispatched(dispatched);
		count.setOnHold(onHold);
		count.setPending(pending);
		count.setProcessing(processing);
		count.setRefunded(refunded);
		count.setRecycle(recycle);

		return count;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderCountService#getCountByEmailAndTag(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public OrderCount getCountByEmailAndTag(String email, Integer siteId,
			Integer isShow, String tag) {
		Integer all = enquiryService.countByEmailAndStatusAndTag(email, null,
				siteId, isShow, tag);
		Integer pending = enquiryService.countByEmailAndStatusAndTag(email,
				IOrderStatusService.PAYMENT_PENDING, siteId, isShow, tag);
		Integer confirmed = enquiryService.countByEmailAndStatusAndTag(email,
				IOrderStatusService.PAYMENT_CONFIRMED, siteId, isShow, tag);
		Integer processing = enquiryService.countByEmailAndStatusAndTag(email,
				IOrderStatusService.PROCESSING, siteId, isShow, tag);
		Integer onHold = enquiryService.countByEmailAndStatusAndTag(email,
				IOrderStatusService.ON_HOLD, siteId, isShow, tag);
		Integer dispatched = enquiryService.countByEmailAndStatusAndTag(email,
				IOrderStatusService.DISPATCHED, siteId, isShow, tag);
		Integer cancelled = enquiryService.countByEmailAndStatusAndTag(email,
				IOrderStatusService.ORDER_CANCELLED, siteId, isShow, tag);
		Integer refunded = enquiryService.countByEmailAndStatusAndTag(email,
				IOrderStatusService.REFUNDED, siteId, isShow, tag);
		Integer recycle = enquiryService.countByEmailAndStatusAndTag(email,
				null, siteId, 2, tag);

		OrderCount count = new OrderCount();

		count.setAll(all);
		count.setCancelled(cancelled);
		count.setConfirmed(confirmed);
		count.setDispatched(dispatched);
		count.setOnHold(onHold);
		count.setPending(pending);
		count.setProcessing(processing);
		count.setRefunded(refunded);
		count.setRecycle(recycle);

		return count;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderCountService#getDropShippingOrderCountByEmail(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public OrderCount getDropShippingOrderCountByEmail(String email,
			Integer siteId, Integer isShow) {
		OrderForm form = new OrderForm();
		form.setUseremail(email);
		form.setSiteId(siteId);
		form.setIsShow(isShow);
		String statusList = null;
		Integer all = enquiryService.searchDropShipOrderCount(form);
		Integer statusId = orderStatusService
				.getIdByName(IOrderStatusService.PAYMENT_PENDING);
		statusList = String.valueOf(statusId);
		form.setStatus(statusList);
		Integer pending = enquiryService.searchDropShipOrderCount(form);
		statusId = orderStatusService
				.getIdByName(IOrderStatusService.PAYMENT_CONFIRMED);
		statusList = String.valueOf(statusId);
		form.setStatus(statusList);
		Integer confirmed = enquiryService.searchDropShipOrderCount(form);
		statusId = orderStatusService
				.getIdByName(IOrderStatusService.PROCESSING);
		statusList = String.valueOf(statusId);
		form.setStatus(statusList);
		Integer processing = enquiryService.searchDropShipOrderCount(form);
		statusId = orderStatusService.getIdByName(IOrderStatusService.ON_HOLD);
		statusList = String.valueOf(statusId);
		form.setStatus(statusList);
		Integer onHold = enquiryService.searchDropShipOrderCount(form);
		statusId = orderStatusService
				.getIdByName(IOrderStatusService.DISPATCHED);
		statusList = String.valueOf(statusId);
		form.setStatus(statusList);
		Integer dispatched = enquiryService.searchDropShipOrderCount(form);
		statusId = orderStatusService
				.getIdByName(IOrderStatusService.ORDER_CANCELLED);
		statusList = String.valueOf(statusId);
		form.setStatus(statusList);
		Integer cancelled = enquiryService.searchDropShipOrderCount(form);
		statusId = orderStatusService.getIdByName(IOrderStatusService.REFUNDED);
		statusList = String.valueOf(statusId);
		form.setStatus(statusList);
		Integer refunded = enquiryService.searchDropShipOrderCount(form);
		form.setStatus(null);
		form.setIsShow(2);
		Integer recycle = enquiryService.searchDropShipOrderCount(form);

		OrderCount count = new OrderCount();

		count.setAll(all);
		count.setCancelled(cancelled);
		count.setConfirmed(confirmed);
		count.setDispatched(dispatched);
		count.setOnHold(onHold);
		count.setPending(pending);
		count.setProcessing(processing);
		count.setRefunded(refunded);
		count.setRecycle(recycle);

		return count;
	}
}
