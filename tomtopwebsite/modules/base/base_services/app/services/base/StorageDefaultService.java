package services.base;

import java.util.List;

import javax.inject.Inject;

import dto.StorageDefault;
import mapper.base.StorageDefaultMapper;
import services.IStorageDefaultService;

/**
 * 默认仓库
 * 
 * @author Administrator
 *
 */

public  class StorageDefaultService implements IStorageDefaultService {

	@Inject
	StorageDefaultMapper storageDefaultMapper;


	@Override
	public int getCountStorageDefault(int storageId) {
		return storageDefaultMapper.getCountStorageDefault(storageId);
	}
	/*
	 * 通过地区获取该地区的默认仓库信息 (non-Javadoc)
	 * 
	 * @see
	 * services.IStorageDefaultService#getStorageDefaultByCountryName(java.lang
	 * .String)
	 */
	@Override
	public StorageDefault getStorageDefaultByCountryName(String countryName) {
		return storageDefaultMapper.getStorageDefaultByCountryName(countryName);
	}

	/*
	 * 通过仓库ID，获取那些地区设置此仓库为默认仓库 (non-Javadoc)
	 * 
	 * @see
	 * services.IStorageDefaultService#getStorageDefaultByDefaultStorage(java
	 * .lang.Integer)
	 */

	@Override
	public List<StorageDefault> getStorageDefaultByDefaultStorage(
			Integer idefaultstorage, int pageNum, int pageSize) {
		return storageDefaultMapper
				.getStorageDefaultByDefaultStorage(idefaultstorage,  pageNum,  pageSize);
	}

	/*
	 * 更新记录 (non-Javadoc)
	 * 
	 * @see services.IStorageDefaultService#update(dto.StorageDefault)
	 */
	@Override
	public void update(StorageDefault storageDefault) {
		storageDefaultMapper.update(storageDefault);
	}

	/*
	 * 插入记录 (non-Javadoc)
	 * 
	 * @see services.IStorageDefaultService#insert(dto.StorageDefault)
	 */
	@Override
	public void insert(StorageDefault storageDefault) {
		StorageDefault tempDefault = getStorageDefaultByCountryName(storageDefault
				.getCcountryname());
		if (tempDefault == null) {
			storageDefaultMapper.insert(storageDefault);
		} else {
			storageDefaultMapper.update(storageDefault);
		}
	}

	/*
	 * 删除记录 (non-Javadoc)
	 * 
	 * @see services.IStorageDefaultService#delete(int)
	 */
	@Override
	public void delete(int iid) {
		storageDefaultMapper.delete(iid);
	}
}
