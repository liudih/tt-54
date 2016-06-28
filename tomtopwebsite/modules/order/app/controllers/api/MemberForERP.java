package controllers.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mapper.member.MemberBaseMapper;
import mapper.order.OrderMapper;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.base.api.ApiBase;
import services.base.api.ServiceResponse;
import services.base.api.ServiceResponseCode;
import services.base.utils.DateFormatUtils;
import services.member.MemberBaseService;
import services.order.IOrderService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import dto.member.MemberBase;

public class MemberForERP extends ApiBase {
	@Inject
	MemberBaseService memberBaseService;

	@Inject
	MemberBaseMapper memberBaseMapper;

	@Inject
	OrderMapper orderMapper;

	@Inject
	IOrderService orderService;

	/**
	 * 未下过单的用户
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result getNoOrderMembers() {
		ServiceResponse serviceResponse = new ServiceResponse();
		try {
			JsonNode node = request().body().asJson();
			if (node == null) {
				return badRequest("Expecting Json data");
			}
			String token = request().getHeader("token");
			if (!TOKEN.equals(token)) {
				serviceResponse.setCode(ServiceResponseCode.ERROR);
				serviceResponse.setAck(ServiceResponseCode.ERROR_ACK);
				serviceResponse.setDescription("token invalid");

				return ok(Json.toJson(serviceResponse));
			}
			ObjectMapper mapper = new ObjectMapper();
			HashMap result = mapper.convertValue(node, HashMap.class);
			if (null == result.get("startDate")
					|| null == result.get("endDate")
					|| null == result.get("pageNum")
					|| null == result.get("pageSize")) {
				serviceResponse.setCode(ServiceResponseCode.ERROR);
				serviceResponse.setAck(ServiceResponseCode.ERROR_ACK);
				serviceResponse
						.setDescription("startDate, endDate, pageNum, pageSize is required");

				return ok(Json.toJson(serviceResponse));
			}
			String startDateStr = result.get("startDate").toString();
			String endDateStr = result.get("endDate").toString();
			Integer pageNum = Integer
					.parseInt(result.get("pageNum").toString());
			Integer pageSize = Integer.parseInt(result.get("pageSize")
					.toString());
			Date startDate = DateFormatUtils
					.getFormatDateYmdhmsByStr(startDateStr);
			Date endDate = DateFormatUtils.getFormatDateYmdhmsByStr(endDateStr);
			List<MemberBase> members = memberBaseService.getAllMembers(
					startDate, endDate, pageNum, pageSize);
			List<HashMap<String, String>> data = new ArrayList<>();
			for (MemberBase memberBase : members) {
				HashMap<String, String> dataMap = new HashMap<>();
				dataMap.put("email", memberBase.getCemail());
				dataMap.put("firstName", memberBase.getCfirstname());
				dataMap.put("lastName", memberBase.getClastname());
				dataMap.put("country", memberBase.getCcountry());
				dataMap.put("regiesterDate", memberBase.getDcreatedate()
						.toString());
				data.add(dataMap);
			}

			return ok(Json.toJson(data));
		} catch (Exception e) {
			Logger.error("exception error", e);
			serviceResponse.setCode(ServiceResponseCode.ERROR);
			serviceResponse.setAck(ServiceResponseCode.ERROR_ACK);
			serviceResponse.setDescription("fetch data fail");

			return ok(Json.toJson(serviceResponse));
		}
	}

	/**
	 * 已经下过单的用户
	 * 
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result getOrderMembers() {
		ServiceResponse serviceResponse = new ServiceResponse();
		try {
			JsonNode node = request().body().asJson();
			if (node == null) {
				return badRequest("Expecting Json data");
			}
			String token = request().getHeader("token");
			if (!TOKEN.equals(token)) {
				serviceResponse.setCode(ServiceResponseCode.ERROR);
				serviceResponse.setAck(ServiceResponseCode.ERROR_ACK);
				serviceResponse.setDescription("token invalid");

				return ok(Json.toJson(serviceResponse));
			}
			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, Object> result = mapper.convertValue(node,
					HashMap.class);
			if (null == result.get("startDate")
					|| null == result.get("endDate")
					|| null == result.get("pageNum")
					|| null == result.get("pageSize")) {
				serviceResponse.setCode(ServiceResponseCode.ERROR);
				serviceResponse.setAck(ServiceResponseCode.ERROR_ACK);
				serviceResponse
						.setDescription("startDate, endDate, pageNum, pageSize is required");

				return ok(Json.toJson(serviceResponse));
			}
			String startDateStr = result.get("startDate").toString();
			String endDateStr = result.get("endDate").toString();
			Date startDate = DateFormatUtils
					.getFormatDateYmdhmsByStr(startDateStr);
			Date endDate = DateFormatUtils.getFormatDateYmdhmsByStr(endDateStr);
			Integer pageNum = Integer
					.parseInt(result.get("pageNum").toString());
			Integer pageSize = Integer.parseInt(result.get("pageSize")
					.toString());
			List<String> members = orderService.getAllMemberEmails(startDate,
					endDate, pageNum, pageSize);

			return ok(Json.toJson(members));
		} catch (Exception e) {
			Logger.error("exception error", e);
			serviceResponse.setCode(ServiceResponseCode.ERROR);
			serviceResponse.setAck(ServiceResponseCode.ERROR_ACK);
			serviceResponse.setDescription("fetch data fail");

			return ok(Json.toJson(serviceResponse));
		}
	}

}
