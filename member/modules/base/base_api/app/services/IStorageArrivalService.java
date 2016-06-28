package services;

import java.util.List;

import dto.StorageArrival;

public interface IStorageArrivalService {

	StorageArrival getStorageArrivalByParams(Integer istorageid,
			String ccarrivalcountry);


	int getCountStorageArrivalList(int storageId);
	/**
	 * 查询此仓库可送达那些地区
	 * 
	 * @param istorageid
	 * @return
	 */

	List<StorageArrival> getStorageArrivalListByStorageId(int istorageid,
			int pageNum, int pageSize);

	void update(StorageArrival storageArrival);

	void insert(StorageArrival storageArrival);

	void delete(int iid);



}