package mapper.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import entity.loyalty.BulkRate;

public interface BulkRateMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(BulkRate record);

    int insertSelective(BulkRate record);

    BulkRate selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(BulkRate record);

    int updateByPrimaryKey(BulkRate record);
    
    @Select("select br.* from t_bulk_rate br "
			+ "INNER JOIN t_bulk b ON br.ibulkid = b.iid "
			+ "WHERE b.igroupid = #{groupId} ORDER BY br.iqty ASC")
	List<BulkRate> getBulkRates(Integer groupId);
    
    @Delete("delete from t_bulk_rate where ibulkid=#{0}")
    int delByBulkId(Integer id);
    
    @Select("select * from t_bulk_rate where ibulkid=#{0} order by iid limit #{2} offset #{1}")
    List<BulkRate> getBulkRatesPage(Integer bulkid,Integer page,Integer pageSize);
    
    @Select("select count(*) from t_bulk_rate where ibulkid = #{0}")
    int getBulkRatesCount(Integer bulkid);
}