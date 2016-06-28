package mapper.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import entity.loyalty.Bulk;

public interface BulkMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(Bulk record);

    int insertSelective(Bulk record);

    Bulk selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(Bulk record);

    int updateByPrimaryKey(Bulk record);
    
    @Select("select * from t_bulk order by iid asc limit #{1} offset #{0}")
    List<Bulk> getBulksPage(Integer page,Integer pageSize);
    
    @Select("select count(*) from t_bulk ")
    int getBulksCount();
}