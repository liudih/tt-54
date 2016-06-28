package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.StorageArrival;

/**
 * 仓库可送达地区
 * N对M
 * @author Administrator
 *
 */
public interface StorageArrivalMapper {

	/**
	 * 根据仓库id和地区名 ，查询记录
	 * @param istorageid
	 * @param ccarrivalcountry
	 * @return
	 */
	@Select("select  iid,istorageid,ccarrivalcountry,ccreateuser,dcreatedate "
			+ "from t_storage_arrival  "
			+ "where istorageid=#{0} "
			+ "and ccarrivalcountry=#{1} limit 1")
	StorageArrival getStorageArrivalByParams(int istorageid,String ccarrivalcountry);
	
	@Select("select  count(1) from t_storage_arrival where istorageid=#{storageId}")
	int getCountStorageArrivalList(int storageId);
	/**
	 * 查询此仓库可送达那些地区
	 * @param istorageid
	 * @return
	 */
	@Select("select  iid,istorageid,ccarrivalcountry,ccreateuser,dcreatedate "
			+ "from t_storage_arrival  where istorageid=#{ 0 } "
			+ "order by iid desc limit #{2} offset "
			+"(#{2} * (#{1} - 1))")
	List<StorageArrival> getStorageArrivalListByStorageId(int storageid,int pageNum,int pageSize );

	@Update("UPDATE t_storage_arrival " + "SET istorageid =#{ istorageid },"
			+ "ccarrivalcountry=#{ ccarrivalcountry },"
			+ "ccreateuser=#{ ccreateuser } " + "WHERE	iid = #{ iid }")
	void update(StorageArrival storageArrival);

	@Insert("insert into t_storage_arrival(istorageid,ccarrivalcountry,ccreateuser)"
			+ " values(#{istorageid},#{ccarrivalcountry},#{ccreateuser})")
	void insert(StorageArrival storageArrival);

	@Delete("delete from t_storage_arrival where iid=#{iid}")
	void delete(int iid);
}