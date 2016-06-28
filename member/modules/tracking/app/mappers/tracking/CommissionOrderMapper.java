package mappers.tracking;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.tracking.CommissionOrder;

public interface CommissionOrderMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(CommissionOrder record);

    int insertSelective(CommissionOrder record);

    CommissionOrder selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(CommissionOrder record);

    int updateByPrimaryKey(CommissionOrder record);
    
    @Select({"<script>",
    	"select * from t_commission_order where icommissionid=#{0} ",
    	"<if test=\"st!=-1\">",
    	"and istatus=#{st} ",
    	"</if>",
    	"<if test=\"page!=-1\">",
    	"limit #{2} offset #{page}",
    	"</if>",
    	"</script>"})
    List<CommissionOrder> getCommissionOrderPage(int hid,@Param("page")int pageIndex,int pageSize,
    		@Param("st")int hstatus);
    
    @Select({"<script>",
    	"select count(*) from t_commission_order where icommissionid=#{0} ",
    	"<if test=\"st!=-1\">",
    	"and istatus=#{st} ",
    	"</if>",
    	"</script>"})
    int getCommissionOrderCount(int hid,@Param("st")int hstatus);
    
    @Select({
		"<script>",
		"select * from t_commission_order ",
		"<if test=\"list!=null and list.size()>0\">",
		"where icommissionid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
		"</if>",
		"</script>" })
    List<CommissionOrder> getCommissionOrderByhids(@Param("list") List<Integer> hids);
    
    @Select("select * from t_commission_order where iorderid=#{0} limit 1")
    CommissionOrder getCommissionOrderByOrderId(Integer oid);

    @Select("select * from t_commission_order where iid=#{0} limit 1")
    CommissionOrder getCommissionOrderByPrimaryKey(Integer id);    
}