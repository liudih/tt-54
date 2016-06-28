package com.rabbit.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Collections2;
import com.rabbit.dto.Storage;
import com.rabbit.dto.transfer.base.StorageBack;
import com.rabbit.services.serviceImp.product.StorageService;
@Controller
public class StorageController{

	@Autowired
	StorageService storageService;
	@RequestMapping(value = "/base/storage")
	@ResponseBody
	public String getAllStorages() {
		List<Storage> storages = storageService
				.getAllStorages();
		Collection<StorageBack> dtoStorages = null;
		if (null != storages && storages.size() > 0) {
			dtoStorages = Collections2.transform(storages, obj -> {
				StorageBack storageBack = new StorageBack();
				storageBack.setId(obj.getIid());
				storageBack.setName(obj.getCstoragename());
				return storageBack;
			});
		}

		if (null == dtoStorages) {
			return null;
		} else {
			return JSON.toJSONString(dtoStorages);
		}
	}

}
