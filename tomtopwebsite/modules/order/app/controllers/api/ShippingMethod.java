package controllers.api;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.shipping.IShippingMethodService;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import controllers.annotation.ApiPermission;

@ApiPermission
public class ShippingMethod extends Controller {
	@Inject
	private IShippingMethodService service;

	@BodyParser.Of(BodyParser.Json.class)
	public Result push() {
		JsonNode jnode = request().body().asJson();
		if (null == jnode) {
			return badRequest("Expecting Json data");
		}
		int i = 0;
		if (jnode.isArray()) {
			dto.shipping.ShippingMethod[] methods = Json.fromJson(jnode,
					dto.shipping.ShippingMethod[].class);
			i = service.add(Lists.newArrayList(methods));
			Logger.debug("push shipping methods receive: {}, save: {}",
					methods.length, i);
		} else {
			dto.shipping.ShippingMethod method = Json.fromJson(jnode,
					dto.shipping.ShippingMethod.class);
			i = service.add(Lists.newArrayList(method));
			Logger.debug("push shipping methods receive: 1, save: {}", i);
		}
		return ok(String.valueOf(i));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result updateRule() {
		JsonNode jnode = request().body().asJson();
		if (null == jnode) {
			return internalServerError("Expecting Json data");
		}
		LinkedHashMap<String, String> map = Maps.newLinkedHashMap();
		try {
			if (jnode.isArray()) {
				dto.shipping.ShippingMethod[] rules = Json.fromJson(jnode,
						dto.shipping.ShippingMethod[].class);
				for (dto.shipping.ShippingMethod rule : rules) {
					map.put(rule.getIid().toString(), service.updateRule(rule)
							.toString());
				}
			} else {
				dto.shipping.ShippingMethod rule = Json.fromJson(jnode,
						dto.shipping.ShippingMethod.class);
				map.put(rule.getIid().toString(), service.updateRule(rule)
						.toString());
			}
		} catch (Exception e) {
			map.put("exception", e.toString());
			Logger.error("ShippingMethod updateRule: ", e);
		}
		Logger.debug("save shipping rule result: {}", Json.toJson(map));
		return ok(Json.toJson(map));
	}
}
