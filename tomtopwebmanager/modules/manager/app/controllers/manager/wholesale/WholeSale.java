package controllers.manager.wholesale;

import java.util.HashMap;
import java.util.List;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.member.IMemberEmailService;
import services.order.IOrderEnquiryService;
import services.wholesale.WholeSaleBaseEnquiryService;
import services.wholesale.WholeSaleBaseUpdateService;
import session.ISessionService;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import controllers.InterceptActon;
import entity.manager.AdminUser;
import entity.wholesale.WholeSaleBase;
import event.messaging.JoinWholeSaleMessagingEvent;
import events.member.JoinWholeSaleEvent;
import form.wholesale.WholeSaleSearchForm;

@With(InterceptActon.class)
public class WholeSale extends Controller {
	@Inject
	WholeSaleBaseEnquiryService wholeSaleBaseEnquiry;
	@Inject
	WholeSaleBaseUpdateService wholeSaleUpdateEnquiry;
	@Inject
	IOrderEnquiryService orderEnquiryService;
	@Inject
	IMemberEmailService memberEmailService;
	@Inject
	EventBus eventBus;
	@Inject
	ISessionService sessionService;

	public Result wholesaleManager() {
		HashMap<Integer, String> statusMap = new HashMap<Integer, String>();
		statusMap.put(0, "待审核");
		statusMap.put(1, "审核通过");
		statusMap.put(2, "审核不通过");
		WholeSaleSearchForm wholeSaleSearchForm = new WholeSaleSearchForm();
		wholeSaleSearchForm.setStatus(0);

		return ok(views.html.manager.member.wholesale.wholesale_manager.render(
				getWholeSaleList(wholeSaleSearchForm), statusMap));
	}

	public Html getWholeSaleList(WholeSaleSearchForm wholeSaleSearchForm) {
		List<WholeSaleBase> wholeSaleBases = wholeSaleBaseEnquiry
				.getWholeSaleBasesBySearchForm(wholeSaleSearchForm);
		if (null == wholeSaleBases || wholeSaleBases.size() <= 0) {
			return views.html.manager.member.wholesale.wholesalebase_table_list
					.render(null, null, 0, 0, 0);
		}
		HashMap<Integer, String> statusMap = new HashMap<Integer, String>();
		statusMap.put(0, "待审核");
		statusMap.put(1, "审核通过");
		statusMap.put(2, "审核不通过");
		Integer pageCount = wholeSaleBaseEnquiry
				.getWholeSaleBaseCount(wholeSaleSearchForm);
		Integer pageNum = wholeSaleSearchForm.getPageNum();
		Integer pageTotal = pageCount / wholeSaleSearchForm.getPageSize()
				+ ((pageCount % wholeSaleSearchForm.getPageSize() > 0) ? 1 : 0);

		return views.html.manager.member.wholesale.wholesalebase_table_list
				.render(wholeSaleBases, statusMap, pageCount, pageNum,
						pageTotal);
	}

	public Result search() {
		Form<WholeSaleSearchForm> wholeSaleSearchForm = Form.form(
				WholeSaleSearchForm.class).bindFromRequest();

		return ok(getWholeSaleList(wholeSaleSearchForm.get()));
	}

	public Result updateStatus(Integer iid, Integer statusid) {
		boolean updateStatusByIid = wholeSaleUpdateEnquiry.updateStatusByIid(
				iid, statusid);
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", updateStatusByIid);
		if (updateStatusByIid) {
			AdminUser user = (AdminUser) sessionService
					.get("ADMIN_LOGIN_CONTEXT");
			Integer sendIid = user.getIid();
			WholeSaleBase wholeSaleBaseByIid = wholeSaleBaseEnquiry
					.getWholeSaleBaseByIid(iid);
			String email = wholeSaleBaseByIid.getCemail();
			Integer iwebsiteId = wholeSaleBaseByIid.getIwebsiteid();
			Integer ilanguageid = wholeSaleBaseByIid.getIlanguageid() != null ? wholeSaleBaseByIid
					.getIlanguageid() : 1;
			JoinWholeSaleEvent event = new JoinWholeSaleEvent(email, statusid,
					iwebsiteId, sendIid);
			JoinWholeSaleMessagingEvent messagingEvent = new JoinWholeSaleMessagingEvent(
					email, statusid, iwebsiteId, ilanguageid, sendIid);
			eventBus.post(event);
			eventBus.post(messagingEvent);
		}
		return ok(Json.toJson(result));
	}
}
