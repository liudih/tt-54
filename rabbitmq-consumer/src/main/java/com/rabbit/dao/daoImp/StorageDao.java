package com.rabbit.dao.daoImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.basemapper.StorageMapper;
import com.rabbit.dao.idao.IStorageDao;
@Component
public class StorageDao implements IStorageDao {

	@Autowired
	StorageMapper storageMapper;

	@Override
	public Integer getStorageIdByStorageName(String storageName) {
		return storageMapper.getStorageIdByStorageName(storageName);
	}
}
