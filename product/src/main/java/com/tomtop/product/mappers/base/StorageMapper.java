package com.tomtop.product.mappers.base;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.StorageParentDto;

public interface StorageMapper {
	/**
	 * 查询所有父仓库
	 * @param listingID
	 * 
	 * @author renyy
	 */
	@Select("select iid,cstoragename from t_storage_parent order by iid ")
	List<StorageParentDto> getStorageParentAll();
	/**
	 * 是否为海外仓
	 * @param listingID
	 * 
	 * @author renyy
	 */
	@Select("select iid from t_storage where ioverseas=0 limit 1")
	Integer getNotOverseasStorage();
	
	@Select("<script>select iid,cstoragename from t_storage where iid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ " order by iid </script>")
	List<StorageParentDto> getStorage(List<Integer> list);
}
