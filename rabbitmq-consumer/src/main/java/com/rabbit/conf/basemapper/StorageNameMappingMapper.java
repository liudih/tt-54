package  com.rabbit.conf.basemapper;

import org.apache.ibatis.annotations.Select;

public interface StorageNameMappingMapper {
	@Select("select cstoragename from t_storage_name_mapping where cerpstoragename=#{0} limit 1")
	String getStorageNameByStorageName(String storageName);
}