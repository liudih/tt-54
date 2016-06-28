package com.rabbit.services.iservice;

import java.util.List;

import com.rabbit.dto.Storage;

public interface IStorageService {

	public abstract List<Storage> getAllStorages();
	
	public abstract int getCountStorages(int parentStorageId);

	public abstract Storage getStorageForStorageId(int iid);

	public abstract Storage getNotOverseasStorage();

	public abstract Integer getStorageIdByName(String storageName);
	
	void updateStorageParentById(Storage storage);
	
	void deleteStorageParent(Storage storage);

	public abstract List<Storage> getStorageByStorageIds(List<Integer> storageIds);

}