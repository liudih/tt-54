package services;

import java.util.List;

import dto.StorageDefault;

public interface IStorageDefaultService {


	
	int getCountStorageDefault(int storageId);

	/**
	 * 根据地区名，获取信息0
	 * 
	 * @param countryName
	 * @return
	 */
	StorageDefault getStorageDefaultByCountryName(String countryName);

	/**
	 * 根据仓库信息，获取那些地区设置此仓库为默认仓库
	 * 
	 * @param countryName
	 * @return
	 */
	List<StorageDefault> getStorageDefaultByDefaultStorage(
			Integer idefaultstorage, int pageNum, int pageSize);

	void update(StorageDefault storageDefault);

	void insert(StorageDefault storageDefault);

	void delete(int iid);


}