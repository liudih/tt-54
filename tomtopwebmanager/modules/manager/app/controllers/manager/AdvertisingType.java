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
import services.advertising.AdvertisingTypeService;
import forms.AdvertisingTypeForm;

@controllers.AdminRole(menuName = "AdvertisingTypeMgr")
public class AdvertisingType extends Controller {

	final static int NOI_ERROR = 0;

	final static int DELETE_ERROR = 1;

	@Inject
	AdvertisingTypeService typeService;

	public Result advertisingTypeManager() {
		List<dto.advertising.AdvertisingType> advertisingTypeist = typeService
				.getAdvertisingPage();
		return ok(views.html.manager.advertising.advert_type_manager
				.render(advertisingTypeist));
	}

	public Result addAdvertisingType() {

		Form<AdvertisingTypeForm> form = Form.form(AdvertisingTypeForm.class)
				.bindFromRequest();
		dto.advertising.AdvertisingType advertisingType = new dto.advertising.AdvertisingType();
		AdvertisingTypeForm uform = form.get();
		BeanUtils.copyProperties(uform, advertisingType);
		dto.advertising.AdvertisingType type = typeService
				.addAdvertisingType(advertisingType);
		return ok(Json.toJson(type));
	}

	public Result updateAdvertisingType() {

		Form<AdvertisingTypeForm> form = Form.form(AdvertisingTypeForm.class)
				.bindFromRequest();

		dto.advertising.AdvertisingType advertisingType = new dto.advertising.AdvertisingType();
		AdvertisingTypeForm uform = form.get();
		BeanUtils.copyProperties(uform, advertisingType);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (typeService.updateAdvertisingType(advertisingType)) {
			resultMap.put("errorCode", NOI_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result deleteAdvertisingType(Long iid) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (typeService.deleteAdvertisingType(iid)) {
			resultMap.put("errorCode", NOI_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result validateKey(Long itypeid) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (typeService.validateKey(itypeid)) {
			resultMap.put("errorCode", NOI_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));
	}
}
