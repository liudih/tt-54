package com.rabbit.services.serviceImp.rabbitproduct.api;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.rabbit.dto.product.ThirdPlatformData;
import com.rabbit.services.serviceImp.product.ThirdPlatformDataUpdateService;

@Service
public class ThirdPlatformDataApi{
	private static Logger log=Logger.getLogger(ThirdPlatformDataApi.class.getName());
	@Autowired
	ThirdPlatformDataUpdateService thirdPlatformDataUpdateService;

	public String push(JsonNode jnode ) throws Exception {
		try {
			if (null == jnode) {
				log.info("Expecting Json data");
				throw new Exception("Expecting Json data");
			}
			boolean result = true;
			List<ThirdPlatformData> errorDatas = Lists
					.newArrayList();
			ObjectMapper om = new ObjectMapper();
			if (jnode.isArray()) {
				ThirdPlatformData[] ThirdPlatformDataArray=om.convertValue(jnode,
						ThirdPlatformData[].class);
				List<ThirdPlatformData> thirdPlatformDatas= Lists.newArrayList(ThirdPlatformDataArray);
				for (ThirdPlatformData thirdPlatformData : thirdPlatformDatas) {
					boolean flag = thirdPlatformDataUpdateService
							.addThirdPlatformData(thirdPlatformData);
					if (!flag) {
						result = false;
						errorDatas.add(thirdPlatformData);
					}
				}
			} else {
				ThirdPlatformData datas = om.convertValue(jnode,
						ThirdPlatformData.class);
				result = thirdPlatformDataUpdateService
						.addThirdPlatformData(datas);
				if (!result) {
					errorDatas.add(datas);
				}
			}
			if (result) {
				return "successfully";
			} else {
				HashMap<String, List<ThirdPlatformData>> errorMap = new HashMap<String, List<ThirdPlatformData>>();
				errorMap.put("errorData", errorDatas);
				log.info("errorData  data:"+JSON.toJSONString(errorMap));
				throw new Exception(JSON.toJSONString(errorMap));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("push ThirdPlatformData error: " + e.getMessage());
			throw e;
		}
	}
}
