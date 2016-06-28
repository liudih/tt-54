package com.rabbit.dao.idao;


public interface IStorageNameMappingDao {
	
	
	/**       
	 * getStorageNameByStorageName       
	 * TODO(根据传入的仓库名称，返回网站与之对应的仓库名称)  
	 * jiang
	*/
	public String getStorageNameByStorageName(String storageName);
}
