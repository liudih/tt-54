package services.base;

import java.util.List;

import javax.inject.Inject;

import mapper.base.StorageMapper;
import mapper.base.StorageParentMapper;

import org.apache.commons.lang3.StringUtils;

import services.IStorageParentService;

import com.google.api.client.util.Lists;

import dto.StorageParent;

public  class StorageParentService implements IStorageParentService {

	@Inject
	StorageParentMapper storageParentMapper;
	@Inject
	StorageDefaultService storageDefaultService;
	@Inject
	StorageArrivalService storageArrivalService;
	@Inject
	StorageMapper storageMapper;
	//仓库常量类
	public static final String STORAGE_TYPE_PARENT="1";
	public static final String STORAGE_TYPE_ARRIVAL="2";
	public static final String STORAGE_TYPE_DEFAULT="3";
	public static final String STORAGE_TYPE_SUB="4";
	public static final String STORAGE_TYPE_ALL="5";
	

	/*
	 * 获取所有总仓库信息 (non-Javadoc)
	 * 
	 * @see services.IStorageParentService#getAllStorageParentList()
	 */
	@Override
	public List<StorageParent> getAllStorageParentList() {
		return storageParentMapper.getAllStorageParentList();
	}

	/*
	 * 通过仓库名字获取总仓库记录 (non-Javadoc)
	 * 
	 * @see
	 * services.IStorageParentService#getStorageParentByStorageName(java.lang
	 * .String)
	 */
	@Override
	public StorageParent getStorageParentByStorageName(String cstorageName) {
		return storageParentMapper.getStorageParentByStorageName(cstorageName);
	}

	
	/*
	 * 更新记录 (non-Javadoc)
	 * 
	 * @see services.IStorageParentService#update(dto.StorageParent)
	 */
	@Override
	public void update(StorageParent storageParent) {
		storageParentMapper.update(storageParent);
	}

	/*
	 * 插入记录 (non-Javadoc)
	 * 
	 * @see services.IStorageParentService#insert(dto.StorageParent)
	 */

	@Override
	public void insert(StorageParent storageParent) {
		StorageParent storageParenTemp = getStorageParentByStorageName(storageParent
				.getCstoragename());
		if (storageParenTemp == null) {
			storageParentMapper.insert(storageParent);
		} else {
			storageParentMapper.update(storageParent);
		}

	}

	/*
	 * 删除记录 (non-Javadoc)
	 * 
	 * @see services.IStorageParentService#delete(int)
	 */
	@Override
	public void delete(String  name) {
		storageParentMapper.deleteByName(name);
	}

	/**
	 * 
	 */
	@Override
	public List<StorageParent> getStorageInfo(String cstorageName, String type, int pageNum, int pageSize) {

		List<StorageParent> storageParentList = Lists.newArrayList();
		if (StringUtils.equals(StorageParentService.STORAGE_TYPE_ALL, type)) {
			storageParentList = storageParentMapper.getAllStorageParentList();
		} else {
			StorageParent storageParent = storageParentMapper
					.getStorageParentByStorageName(cstorageName);
			if (storageParent != null) {
				if (StringUtils.equals(StorageParentService.STORAGE_TYPE_ARRIVAL, type)) {
					storageParent.setStorageArrivalInfo(storageArrivalService
							.getStorageArrivalListByStorageId(
									storageParent.getIid(), pageNum, pageSize));
				} else if (StringUtils.equals(StorageParentService.STORAGE_TYPE_DEFAULT, type)) {

					storageParent.setStorageDefaultInfo(storageDefaultService
							.getStorageDefaultByDefaultStorage(
									storageParent.getIid(), pageNum, pageSize));
				} else if (StringUtils.equals(StorageParentService.STORAGE_TYPE_SUB, type)) {

					storageParent.setStorageSubInfo(storageMapper
							.getStorageListByParentStorageId(
									storageParent.getIid(), pageNum, pageSize));
				}

				storageParentList.add(storageParent);
			}
		}
		return storageParentList;
	}


}
