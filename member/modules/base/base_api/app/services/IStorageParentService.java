package services;

import java.util.List;

import dto.StorageParent;

public interface IStorageParentService {

	

	List<StorageParent> getAllStorageParentList();
	
	StorageParent getStorageParentByStorageName(String cstorageName);

	void update(StorageParent storageParent);

	void insert(StorageParent storageParent);

	void delete(String name);


	//public List<StorageParent> getStorageInfo(String cstorageName,String type);

	public List<StorageParent> getStorageInfo(String cstorageName, String type,int pageNum,int pageSize );

}