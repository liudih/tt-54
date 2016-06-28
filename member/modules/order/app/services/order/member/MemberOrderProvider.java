package services.order.member;

import java.util.List;

import javax.inject.Inject;

import dto.order.Order;
import services.order.IOrderEnquiryService;
import forms.order.MemberOrderForm;

public class MemberOrderProvider {
	@Inject
	IOrderEnquiryService enquiryService;

	public List<Order> searchOrders(String email, MemberOrderForm form,
			Integer siteId, boolean isNormal) {
		return enquiryService.searchOrders(email, form, siteId, isNormal);
	}

	public List<Order> searchOrdersByTag(String email, MemberOrderForm form,
			Integer siteId, String tag) {
		return enquiryService.searchOrdersByTag(email, form, siteId, tag);
	}

	public Integer searchOrderCount(String email, MemberOrderForm form,
			Integer siteId, boolean isNormal) {
		return enquiryService.searchOrderCount(email, form, siteId, isNormal);
	}

	public Integer searchOrderCount(String email, MemberOrderForm form,
			Integer siteId, String tag) {
		return enquiryService.searchOrderCountByTag(email, form, siteId, tag);
	}

	public Integer searchOrderPage(String email, MemberOrderForm form,
			Integer siteId, boolean isNormal) {
		int i = searchOrderCount(email, form, siteId, isNormal);
		int page = 0;
		if ((i % form.getPageSize()) > 0) {
			page = i / form.getPageSize() + 1;
		} else {
			page = i / form.getPageSize();
		}
		return page;
	}

	public Integer searchOrderPageByTag(String email, MemberOrderForm form,
			Integer siteId, String tag) {
		int i = searchOrderCount(email, form, siteId, tag);
		int page = 0;
		if ((i % form.getPageSize()) > 0) {
			page = i / form.getPageSize() + 1;
		} else {
			page = i / form.getPageSize();
		}
		return page;
	}
}
