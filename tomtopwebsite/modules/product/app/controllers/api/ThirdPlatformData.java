package controllers.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.receivedata.HandleReceivedDataType;
import services.product.ThirdPlatformDataUpdateService;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import controllers.annotation.ApiHistory;
import controllers.annotation.ApiPermission;

@ApiPermission
public class ThirdPlatformData extends Controller {
	@Inject
	ThirdPlatformDataUpdateService thirdPlatformDataUpdateService;

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_THIRDPLATFORMDATA, createuser = "api")
	public Result push() {
		try {
			JsonNode jnode = request().body().asJson();
			if (null == jnode) {
				return internalServerError("Expecting Json data");
			}
			boolean result = true;
			List<dto.product.ThirdPlatformData> errorDatas = Lists
					.newArrayList();
			if (jnode.isArray()) {
				dto.product.ThirdPlatformData[] datas = Json.fromJson(jnode,
						dto.product.ThirdPlatformData[].class);
				List<dto.product.ThirdPlatformData> thirdPlatformDatas = Arrays
						.asList(datas);
				for (dto.product.ThirdPlatformData thirdPlatformData : thirdPlatformDatas) {
					boolean flag = thirdPlatformDataUpdateService
							.addThirdPlatformData(thirdPlatformData);
					if (!flag) {
						result = false;
						errorDatas.add(thirdPlatformData);
					}
				}
			} else {
				dto.product.ThirdPlatformData datas = Json.fromJson(jnode,
						dto.product.ThirdPlatformData.class);
				result = thirdPlatformDataUpdateService
						.addThirdPlatformData(datas);
				if (!result) {
					errorDatas.add(datas);
				}
			}
			if (result) {
				return ok("successfully");
			} else {
				HashMap<String, List<dto.product.ThirdPlatformData>> errorMap = new HashMap<String, List<dto.product.ThirdPlatformData>>();
				errorMap.put("errorData", errorDatas);
				return internalServerError(Json.toJson(errorMap));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.debug("push ThirdPlatformData error: " + e.getMessage());
			return internalServerError(e.getMessage());
		}
	}
}
