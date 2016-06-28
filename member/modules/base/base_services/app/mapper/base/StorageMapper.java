package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.Storage;

/**
 * 真实仓库（子仓库）
 * 
 * @author Administrator
 *
 */
public interface StorageMapper {
	@Select("select iid,cstoragename,ioverseas,ccreateuser,iparentstorage from t_storage where iid=#{iid}")
	Storage getStorageForStorageId(Integer iid);

	@Select("select * from t_storage")
	List<Storage> getAllStorageList();
	
	@Select("select count(1) from t_storage where iparentstorage=#{parentStorageId}")
	int getCountStorage(int parentStorageId);

	@Select("select * from t_storage where ioverseas=0 limit 1")
	Storage getNotOverseasStorage();

	@Select("select iid from t_storage where cstoragename=#{0} limit 1")
	Integer getStorageIdByStorageName(String storageName);

	/**
	 * 通过子通过父级ID，获取子仓库集合
	 * 
	 * @param iid
	 * @return
	 */
	@Select("select s.iid,s.iparentstorage,s.ioverseas,s.cstoragename,s.ccreateuser,s.dcreatedate "
			+ "from t_storage_parent pa ,t_storage s  "
			+ "where pa.iid=s.iparentstorage "
			+ "and pa.iid=#{0} "
			+ "order by s.iid desc limit #{2} offset "
			+"(#{2} * (#{1} - 1))")
	List<Storage> getStorageListByParentStorageId(int iid, int pageNum, int pageSize);
	
	@Update("UPDATE t_storage SET iparentstorage =#{ iparentstorage } WHERE	iid = #{ iid }")
	void updateStorageParentById(Storage storage);

	/**
	 * 
	 * @Title: getStorageByStorageIds
	 * @Description: TODO(通过仓库ID查询仓库)
	 * @param @param storageIds
	 * @param @return
	 * @return List<Storage>
	 * @throws 
	 * @author yinfei
	 */
	@Select("<script>"
			+ "select * from t_storage "
			+ "where iid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "</script>")
	List<Storage> getStorageByStorageIds(@Param("list")List<Integer> storageIds);
}