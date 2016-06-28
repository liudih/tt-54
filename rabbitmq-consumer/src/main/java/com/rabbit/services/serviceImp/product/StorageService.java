package com.rabbit.services.serviceImp.product;

/*import interceptors.CacheResult;*/

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.conf.basemapper.StorageMapper;
import com.rabbit.dto.Storage;
import com.rabbit.services.iservice.IStorageService;

@Service
public class StorageService implements IStorageService {

	@Autowired
	StorageMapper storageMapper;
	public static final int DEL_STATE=-1;
	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IStorageService#getAllStorages()
	 */
	
	/*@CacheResult("product.badges")*/
	public List<Storage> getAllStorages() {

		return storageMapper.getAllStorageList();

	}

	public int getCountStorages(int parentStorageId) {
		return storageMapper.getCountStorage(parentStorageId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IStorageService#getStorageForStorageId(int)
	 */
	public Storage getStorageForStorageId(int iid) {
		return storageMapper.getStorageForStorageId(iid);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IStorageService#getNotOverseasStorage()
	 */
	public Storage getNotOverseasStorage() {
		return storageMapper.getNotOverseasStorage();
	}

	/*
	 * 根据仓库名称获取仓库ID
	 * 
	 * @see services.IStorageService#getStorageIdByName(java.lang.String)
	 */
	public Integer getStorageIdByName(String storageName) {
		return storageMapper.getStorageIdByStorageName(storageName);
	}

	public void updateStorageParentById(Storage storage) {
		storageMapper.updateStorageParentById(storage);
	}

	public void deleteStorageParent(Storage storage) {
		storage.setIparentstorage(StorageService.DEL_STATE);
		storageMapper.updateStorageParentById(storage);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getStorageByStorageIds</p>
	 * <p>Description: 通过仓库ID查询仓库</p>
	 * @param storageIds
	 * @return
	 * @see services.IStorageService#getStorageByStorageIds(java.util.List)
	 */
	public List<Storage> getStorageByStorageIds(List<Integer> storageIds){
		return storageMapper.getStorageByStorageIds(storageIds);
	}
}
