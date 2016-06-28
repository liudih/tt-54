package dao.impl;

import mapper.base.StorageNameMappingMapper;

import com.google.inject.Inject;

import dao.IStorageNameMappingDao;

public class StorageNameMappingDao implements IStorageNameMappingDao {

	@Inject
	StorageNameMappingMapper storageNameMappingMapper;

	@Override
	public String getStorageNameByStorageName(String storageName) {
		return storageNameMappingMapper
				.getStorageNameByStorageName(storageName);
	}

}
