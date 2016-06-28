package com.rabbit.services.iservice.shipping;

import com.fasterxml.jackson.databind.JsonNode;
import com.rabbit.dto.shipping.ShippingMethod;

public interface IShippingMethodService {


	public abstract ShippingMethod getShippingMethodById(Integer id);

	public abstract String push(JsonNode jsonNode) throws Exception;

}