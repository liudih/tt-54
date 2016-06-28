package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.StorageDefault;
/**
 * 地区支持默认仓库操作
 * 1对N
 * @author Administrator
 *
 */

public interface StorageDefaultMapper {

	@Select("select count(1) from t_storage_default where idefaultstorage= #{storageId}")
	int getCountStorageDefault(int storageId);
	/**
	 * 根据地区名，获取信息0
	 * @param countryName
	 * @return
	 */
	@Select("select iid,idefaultstorage,ccountryname,ccreateuser,dcreatedate from t_storage_default "
			+ " where ccountryname=#{countryName}  limit 1")
	StorageDefault getStorageDefaultByCountryName(String countryName);
	/**
	 * 根据仓库信息，获取那些地区设置此仓库为默认仓库
	 * @param countryName
	 * @return
	 */
	@Select("select iid,idefaultstorage,ccountryname,ccreateuser,dcreatedate "
			+ "from t_storage_default where idefaultstorage=#{0}"
			+ "order by iid desc limit #{2} offset "
			+"(#{2} * (#{1} - 1))")
	List<StorageDefault> getStorageDefaultByDefaultStorage(int idefaultstorage, int pageNum, int pageSize);

	@Update("UPDATE t_storage_default "
			+ "SET idefaultstorage =#{ idefaultstorage },"
			+ "ccountryname=#{ ccountryname },"
			+ "ccreateuser=#{ ccreateuser } " + "WHERE	iid = #{ iid }")
	void update(StorageDefault storageDefault);

	@Insert("insert into t_storage_default(idefaultstorage,ccountryname,ccreateuser)"
			+ " values(#{idefaultstorage},#{ccountryname},#{ccreateuser})")
	void insert(StorageDefault storageDefault);

	@Delete("delete from t_storage_default where iid=#{iid}")
	void delete(int iid);
}