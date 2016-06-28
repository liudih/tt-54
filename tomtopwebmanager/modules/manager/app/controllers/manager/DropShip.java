package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.dropship.DropShipBaseEnquiryService;
import services.dropship.DropShipBaseUpdateService;
import services.dropship.DropShipLevelEnquiryService;
import services.member.IMemberEmailService;
import services.order.IOrderEnquiryService;
import session.ISessionService;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import controllers.InterceptActon;
import entity.manager.AdminUser;
import dto.member.DropShipBase;
import dto.member.DropShipLevel;
import event.messaging.JoinDropShipMessagingEvent;
import events.member.JoinDropShipEvent;
import form.dropship.DropShipSearchForm;

@With(InterceptActon.class)
public class DropShip extends Controller {
	@Inject
	DropShipBaseEnquiryService dropShipBaseEnquiryService;
	@Inject
	DropShipBaseUpdateService dropShipBaseUpdateService;
	@Inject
	DropShipLevelEnquiryService levelEnquiryService;
	@Inject
	IOrderEnquiryService orderEnquiryService;
	@Inject
	IMemberEmailService memberEmailService;
	@Inject
	EventBus eventBus;
	@Inject
	ISessionService sessionService;

	public Result dropShipManager() {
		HashMap<Integer, String> statusMap = new HashMap<Integer, String>();
		statusMap.put(0, "待审核");
		statusMap.put(1, "审核通过");
		statusMap.put(2, "审核不通过");
		DropShipSearchForm dropShipSearchForm = new DropShipSearchForm();
		dropShipSearchForm.setStatus(0);

		return ok(views.html.manager.member.dropship.dropship_manager.render(
				getDropShipList(dropShipSearchForm), statusMap));
	}

	public Html getDropShipList(DropShipSearchForm dropShipSearchForm) {
		List<DropShipBase> dropShipBases = dropShipBaseEnquiryService
				.getDropShipBasesBySearch(dropShipSearchForm);
		if (null == dropShipBases || dropShipBases.size() <= 0) {
			return views.html.manager.member.dropship.dropship_table_list
					.render(null, null, null, null, 0, 0, 0);
		}
		HashMap<Integer, String> statusMap = new HashMap<Integer, String>();
		statusMap.put(0, "待审核");
		statusMap.put(1, "审核通过");
		statusMap.put(2, "审核不通过");
		HashMap<String, Double> grandMap = new HashMap<String, Double>();
		dropShipBases.forEach(d -> {
			String email = d.getCemail();
			Double memberGrandtotal = orderEnquiryService
					.getMemberGrandtotal(email);
			grandMap.put(email, memberGrandtotal);
		});
		Integer pageCount = dropShipBaseEnquiryService.getDropShipBasesCount(
				dropShipSearchForm.getStatus(), dropShipSearchForm.getEmail(),
				dropShipSearchForm.getLevel());
		Integer pageNum = dropShipSearchForm.getPageNum();
		Integer pageTotal = pageCount / dropShipSearchForm.getPageSize()
				+ ((pageCount % dropShipSearchForm.getPageSize() > 0) ? 1 : 0);

		List<DropShipLevel> dropShipLevels = levelEnquiryService
				.getDropShipLevels();
		Map<Integer, String> levelMap = dropShipLevels.stream().collect(
				Collectors.toMap(e -> e.getIid(), e -> e.getClevelname()));

		return views.html.manager.member.dropship.dropship_table_list.render(
				dropShipBases, statusMap, levelMap, grandMap, pageCount,
				pageNum, pageTotal);
	}

	public Result search() {
		Form<DropShipSearchForm> dropShipSearchForm = Form.form(
				DropShipSearchForm.class).bindFromRequest();

		return ok(getDropShipList(dropShipSearchForm.get()));
	}

	public Result updateStatus(Integer iid, Integer statusid) {
		boolean updateStatusByIid = dropShipBaseUpdateService
				.updateStatusByIid(iid, statusid);
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", updateStatusByIid);
		if (updateStatusByIid) {
			AdminUser user = (AdminUser) sessionService
					.get("ADMIN_LOGIN_CONTEXT");
			DropShipBase dropShipBaseByIid = dropShipBaseEnquiryService
					.getDropShipBaseByIid(iid);
			String email = dropShipBaseByIid.getCemail();
			Integer iwebsiteId = dropShipBaseByIid.getIwebsiteid();
			Integer ilanguageId = dropShipBaseByIid.getIlanguageid() == null ? 1
					: dropShipBaseByIid.getIlanguageid();
			JoinDropShipEvent event = new JoinDropShipEvent(email, statusid,
					iwebsiteId, ilanguageId);
			JoinDropShipMessagingEvent messagingEvent = new JoinDropShipMessagingEvent(
					email, statusid, iwebsiteId, ilanguageId, user.getIid());
			eventBus.post(event);
			eventBus.post(messagingEvent);
		}
		return ok(Json.toJson(result));
	}

	public Result updateLevel(Integer iid, Integer levelId) {
		boolean updateLevel = dropShipBaseUpdateService.updateLevelByIid(iid,
				levelId);
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", updateLevel);

		return ok(Json.toJson(result));
	}
}
