package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.advertising.AdvertisingPositionService;
import forms.AdvertisingPositionForm;

@controllers.AdminRole(menuName = "AdvertisingPositionMgr")
public class AdvertisingPosition extends Controller {

	final static int NOI_ERROR = 0;

	final static int DELETE_ERROR = 1;

	@Inject
	AdvertisingPositionService positionService;

	public Result advertisingPositionManager() {
		List<dto.advertising.AdvertisingPosition> advertisingPositionist = positionService
				.getAdvertisingPage();
		return ok(views.html.manager.advertising.advert_position_manager
				.render(advertisingPositionist));
	}

	public Result addAdvertisingPosition() {
		Form<AdvertisingPositionForm> form = Form.form(
				AdvertisingPositionForm.class).bindFromRequest();

		dto.advertising.AdvertisingPosition advertisingPosition = new dto.advertising.AdvertisingPosition();
		AdvertisingPositionForm uform = form.get();
		BeanUtils.copyProperties(uform, advertisingPosition);

		dto.advertising.AdvertisingPosition type = positionService
				.addAdvertisingPosition(advertisingPosition);
		return ok(Json.toJson(type));
	}

	public Result updateAdvertisingPosition() {

		Form<AdvertisingPositionForm> form = Form.form(
				AdvertisingPositionForm.class).bindFromRequest();

		dto.advertising.AdvertisingPosition advertisingPosition = new dto.advertising.AdvertisingPosition();
		AdvertisingPositionForm uform = form.get();
		BeanUtils.copyProperties(uform, advertisingPosition);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (positionService.updateAdvertisingPosition(advertisingPosition)) {
			resultMap.put("errorCode", NOI_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result deleteAdvertisingPosition(Long iid) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (positionService.deleteAdvertisingPosition(iid)) {
			resultMap.put("errorCode", NOI_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result validateKey(Long ipositionid) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (positionService.validateKey(ipositionid)) {
			resultMap.put("errorCode", NOI_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));
	}
}
