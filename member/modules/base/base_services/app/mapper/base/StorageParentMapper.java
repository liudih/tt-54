package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.StorageParent;

/**
 * 仓库总表操作 1对N
 * 
 * @author Administrator
 *
 */

public interface StorageParentMapper {

	@Select("select iid,cstoragename,ccreateuser,dcreatedate from t_storage_parent")
	List<StorageParent> getAllStorageParentList();

	@Select("select iid,cstoragename,ccreateuser,dcreatedate from t_storage_parent  where cstoragename=#{cstorageName}  limit 1")
	StorageParent getStorageParentByStorageName(String cstorageName);


	@Update("UPDATE t_storage_parent " + "SET cstoragename =#{ cstoragename },"
			+ "ccreateuser=#{ ccreateuser }" + "WHERE iid = #{ iid }")
	void update(StorageParent storageParent);

	@Insert("insert into t_storage_parent(cstoragename,ccreateuser)"
			+ " values(#{cstoragename},#{ccreateuser})")
	void insert(StorageParent storageParent);
	
	@Delete("delete from t_storage_parent where cstoragename= #{name}")
	void deleteByName(String name);
	
}