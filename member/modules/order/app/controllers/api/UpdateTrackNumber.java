package controllers.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import mapper.order.OrderMapper;
import mapper.order.OrderPackMapper;
import mapper.order.OrderStatusMapper;
import mapper.order.StatusHistoryMapper;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.base.api.ApiBase;
import services.base.api.ServiceResponse;
import services.base.api.ServiceResponseCode;
import services.order.IOrderStatusService;
import services.order.OrderStatisticsService;
import services.order.OrderUpdateService;

import com.fasterxml.jackson.databind.JsonNode;

import dto.order.OrderPack;

public class UpdateTrackNumber extends ApiBase {
	@Inject
	OrderPackMapper orderPackMapper;
	@Inject
	OrderMapper orderMapper;
	@Inject
	IOrderStatusService orderStatusService;
	@Inject
	OrderStatusMapper statusMapper;
	@Inject
	StatusHistoryMapper historyMapper;
	
	public final static String url = "http://www.tomtop.com/api/order/updateTrackNumberFromERP";
	
	@Inject
	OrderUpdateService orderUpdateService;
	
	@BodyParser.Of(BodyParser.Json.class)
	public Result updateTrackNumberFromERP () {
		ServiceResponse serviceResponse = new ServiceResponse();
		try {		
			Logger.debug("----debug: " + request().body().asJson());
			JsonNode jnode = request().body().asJson();		
			HashMap<String, Object> requestMap = Json.fromJson(jnode, HashMap.class);			
			System.out.println(requestMap);			
			if ( ! getToken().equals(requestMap.get("token"))) {
				serviceResponse.setCode(ServiceResponseCode.ERROR);
				serviceResponse.setAck("Error");
				serviceResponse.setDescription("token does not match");	
				
				return ok(Json.toJson(serviceResponse));
			}
			Integer isTracking = Integer.parseInt(requestMap.get("isTrackCode").toString());
			List<Map<String, Object>> products = (List<Map<String, Object>>) requestMap.get("products");
			String trackNumber = "";
			String localTrackNumber = "";
			Boolean isTrackCode = false;
			if (1 == isTracking) {
				isTrackCode = true;
				trackNumber = requestMap.get("trackNumber").toString();
			} else {
				localTrackNumber = requestMap.get("localTrackNumber").toString();
			}
			Map<String, Object> productResult = new HashMap<>();
			for (Map<String, Object> productMap: products) {
				String sku = productMap.get("sku").toString();				
				Integer orderId = Integer.parseInt(productMap.get("itemId").toString());
				OrderPack orderPack = orderPackMapper.getOrderPackByOrderIdAndSku(orderId, sku);
				if (null == orderPack) {
					productResult.put(sku, ServiceResponseCode.ERROR);
					continue;
				}
				orderPack.setIisregister(isTracking);
				if (isTrackCode) {
					orderPack.setCtrackingnumber(trackNumber);
				} else {
					orderPack.setClocaltracknumber(localTrackNumber);
				}
				int saveResult = orderPackMapper.updateByPrimaryKeySelective(orderPack);				
				if (saveResult > 0) {						
					productResult.put(sku, ServiceResponseCode.SUCCESS);
				} else {
					productResult.put(sku, ServiceResponseCode.ERROR);
				}
			}
			serviceResponse.setCode(ServiceResponseCode.SUCCESS);
			serviceResponse.setAck(ServiceResponseCode.SUCCESS_ACK);
			serviceResponse.setDescription("update succesfully");
			serviceResponse.setResult(productResult);
			Logger.debug(Json.toJson(serviceResponse).toString());
			
			return ok(Json.toJson(serviceResponse));
		} catch (Exception e) {
			Logger.error("exception error", e);
			serviceResponse.setCode(ServiceResponseCode.ERROR);
			serviceResponse.setAck(ServiceResponseCode.ERROR_ACK);
			serviceResponse.setDescription("update fail");
			serviceResponse.setResult(null);
			
			return ok(Json.toJson(serviceResponse));			
		}
	}
	
	
}
