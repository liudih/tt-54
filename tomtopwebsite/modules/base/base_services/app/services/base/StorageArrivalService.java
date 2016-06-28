package services.base;

import java.util.List;

import javax.inject.Inject;

import mapper.base.StorageArrivalMapper;
import services.IStorageArrivalService;
import dto.StorageArrival;

/**
 * 仓库可送达地区
 * 
 * @author Administrator
 *
 */
public  class StorageArrivalService implements IStorageArrivalService {

	@Inject
	StorageArrivalMapper storageArrivalMapper;

	/*
	 * 根据仓库id和地区名 ，查询记录 (non-Javadoc)
	 * 
	 * @see
	 * services.IStorageArrivalService#getStorageArrivalByParams(java.lang.Integer
	 * , java.lang.String)
	 */
	@Override
	public StorageArrival getStorageArrivalByParams(Integer istorageid,
			String ccarrivalcountry) {
		return storageArrivalMapper.getStorageArrivalByParams(istorageid,
				ccarrivalcountry);
	}


	@Override
	public int getCountStorageArrivalList(int storageId) {
		return storageArrivalMapper.getCountStorageArrivalList(storageId);
	}

	/*
	 * 通过仓库ID获取可送达地区列表 (non-Javadoc)
	 * 
	 * @see
	 * services.IStorageArrivalService#getStorageArrivalListByStorageId(java
	 * .lang.String)
	 */
	@Override
	public List<StorageArrival> getStorageArrivalListByStorageId(
			int istorageid, int pageNum, int pageSize) {
		return storageArrivalMapper.getStorageArrivalListByStorageId(
				istorageid, pageNum, pageSize);
	}


	/*
	 * 更新记录 (non-Javadoc)
	 * 
	 * @see services.IStorageArrivalService#update(dto.StorageArrival)
	 */
	@Override
	public void update(StorageArrival storageArrival) {
		storageArrivalMapper.update(storageArrival);

	}

	/*
	 * 删除记录 (non-Javadoc)
	 * 
	 * @see services.IStorageArrivalService#delete(int)
	 */
	@Override
	public void delete(int iid) {
		storageArrivalMapper.delete(iid);
	}

	/*
	 * 插入记录 (non-Javadoc)
	 * 
	 * @see services.IStorageArrivalService#insert(dto.StorageArrival)
	 */
	@Override
	public void insert(StorageArrival storageArrival) {
		StorageArrival arrival = getStorageArrivalByParams(
				storageArrival.getIstorageid(),
				storageArrival.getCcarrivalcountry());
		if (arrival == null) {
			storageArrivalMapper.insert(storageArrival);
		} else {
			storageArrivalMapper.update(storageArrival);
		}
	}


}
