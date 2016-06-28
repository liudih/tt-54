package com.rabbit.dao.daoImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.basemapper.StorageNameMappingMapper;
import com.rabbit.dao.idao.IStorageNameMappingDao;
@Component
public class StorageNameMappingDao implements IStorageNameMappingDao {

	@Autowired
	StorageNameMappingMapper storageNameMappingMapper;

	@Override
	public String getStorageNameByStorageName(String storageName) {
		return storageNameMappingMapper
				.getStorageNameByStorageName(storageName);
	}

}
