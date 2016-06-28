package mappers.tracking;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.tracking.CommissionHistory;

public interface CommissionHistoryMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(CommissionHistory record);

    int insertSelective(CommissionHistory record);

    CommissionHistory selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(CommissionHistory record);

    int updateByPrimaryKey(CommissionHistory record);
    
    @Select({"<script>",
    	"select * from t_commission_history where caid=#{aid} ",
    	"<if test=\"sd!=null and sd!=''\">",
    	"and dcreatedate &gt;= #{sd} ",
    	"</if>",
    	"<if test=\"ed!=null and ed!=''\">",
    	"and dcreatedate &lt;= #{ed} ",
    	"</if>",
    	"<if test=\"sn!=null and sn!=''\">",
    	"and (cwithdrawlid like '%'||trim(#{sn})||'%' or ctransactionid like '%'||trim(#{sn})||'%') ",
    	"</if>",
    	"<if test=\"st!=-1\">",
    	"and icommissionstatus=#{st} ",
    	"</if>",
    	"order by dcreatedate desc ",
    	"<if test=\"page!=null\">",
    	"limit #{1} offset #{page}",
    	"</if>",
    	"</script>"})
    List<CommissionHistory> getCommissionHistoryPage(@Param("page")Integer page,Integer pageSize,
    		@Param("sd")Date startdate,@Param("ed")Date enddate,
    		@Param("sn")String searchname,@Param("aid")String aid,
    		@Param("st")int hstatus);
    
    @Select({"<script>",
    	"select count(*) from t_commission_history where caid=#{aid} ",
    	"<if test=\"sd!=null and sd!=''\">",
    	"and dcreatedate &gt;= #{sd} ",
    	"</if>",
    	"<if test=\"ed!=null and ed!=''\">",
    	"and dcreatedate &lt;= #{ed} ",
    	"</if>",
    	"<if test=\"sn!=null and sn!=''\">",
    	"and (cwithdrawlid like '%'||trim(#{sn})||'%' or ctransactionid like '%'||trim(#{sn})||'%') ",
    	"</if>",
    	"<if test=\"st!=-1\">",
    	"and icommissionstatus=#{st} ",
    	"</if>",
    	"</script>"})
    int getCommissionHistoryCount(@Param("sd")Date startdate,@Param("ed")Date enddate,
    		@Param("sn")String searchname,@Param("aid")String aid,
    		@Param("st")int hstatus);
    
    
	@Select({ "<script>" + "select * from t_commission_history where 1=1"
			+ "<if test=\"sd!=null and sd!=''\">" + " and dcreatedate &gt;= #{sd}"	+ "</if>" 
			+ "<if test=\"ed!=null and ed!=''\">" + " and dcreatedate &lt;= #{ed}" + "</if>"
			+ "<if test=\"aid!=null and aid!=''\">" + " and caid= #{aid}" + "</if>"
			+ "<if test=\"pid!=null and pid!=''\">" + " and ctransactionid= #{pid}"	+ "</if>" 
			+ "<if test=\"status!=null\">" + " and icommissionstatus = #{status}" + "</if>"
			+ " order by iid desc"
			+"<if test=\"page!=null\">"+ " limit #{ps} offset #{page}"+ "</if>"
			+ "</script>" })
	List<CommissionHistory> getManagerCommissionHistoryPage(
			@Param("page") Integer page, @Param("ps") Integer pageSize,
			@Param("sd") Date startdate, @Param("ed") Date enddate,
			@Param("aid") String aid, @Param("pid") String transactionid,
			@Param("status") Integer status);
    
	
	@Select({ "<script>" + "select count(*) from t_commission_history where 1=1"
			+ "<if test=\"sd!=null and sd!=''\">" + " and dcreatedate &gt;= #{sd}"	+ "</if>" 
			+ "<if test=\"ed!=null and ed!=''\">" + " and dcreatedate &lt;= #{ed}" + "</if>"
			+ "<if test=\"aid!=null and aid!=''\">" + " and caid= #{aid}" + "</if>"
			+ "<if test=\"pid!=null and pid!=''\">" + " and ctransactionid= #{pid}"	+ "</if>" 
			+ "<if test=\"status!=null\">" + " and icommissionstatus = #{status}" + "</if>"
			+ "</script>" })
	int getManagerCommissionHistoryCount(@Param("sd") Date startdate, @Param("ed") Date enddate,
			@Param("aid") String aid, @Param("pid") String transactionid,
			@Param("status") Integer status);
	
	@Select("select count(*) from t_commission_history where cwithdrawlid=#{0}")
	int getCommissionHistoryCountByWid(String wid);

	@Select("select * from t_commission_history where iid=#{iid}")
	CommissionHistory getOneRecordByPrimaryKey(int iid);

}