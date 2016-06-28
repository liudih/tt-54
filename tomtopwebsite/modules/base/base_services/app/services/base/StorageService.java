package services.base;

import interceptors.CacheResult;

import java.util.List;

import javax.inject.Inject;

import mapper.base.StorageMapper;
import services.IStorageService;
import dto.Storage;

public class StorageService implements IStorageService {

	@Inject
	StorageMapper storageMapper;
	public static final int DEL_STATE=-1;
	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IStorageService#getAllStorages()
	 */
	@Override
	@CacheResult("product.badges")
	public List<Storage> getAllStorages() {

		return storageMapper.getAllStorageList();

	}

	@Override
	public int getCountStorages(int parentStorageId) {
		return storageMapper.getCountStorage(parentStorageId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IStorageService#getStorageForStorageId(int)
	 */
	@Override
	public Storage getStorageForStorageId(int iid) {
		return storageMapper.getStorageForStorageId(iid);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IStorageService#getNotOverseasStorage()
	 */
	@Override
	public Storage getNotOverseasStorage() {
		return storageMapper.getNotOverseasStorage();
	}

	/*
	 * 根据仓库名称获取仓库ID
	 * 
	 * @see services.IStorageService#getStorageIdByName(java.lang.String)
	 */
	@Override
	public Integer getStorageIdByName(String storageName) {
		return storageMapper.getStorageIdByStorageName(storageName);
	}

	@Override
	public void updateStorageParentById(Storage storage) {
		storageMapper.updateStorageParentById(storage);
	}

	@Override
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
