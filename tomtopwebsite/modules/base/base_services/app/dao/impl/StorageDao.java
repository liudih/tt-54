package dao.impl;

import mapper.base.StorageMapper;

import com.google.inject.Inject;

import dao.IStorageDao;

public class StorageDao implements IStorageDao {

	@Inject
	StorageMapper storageMapper;

	@Override
	public Integer getStorageIdByStorageName(String storageName) {
		return storageMapper.getStorageIdByStorageName(storageName);
	}
}
