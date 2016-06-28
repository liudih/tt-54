package mapper.base;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.base.VisitLog;

public interface VisitLogMapper {
    int insert(VisitLog record);

    int insertSelective(VisitLog record);
    
    @Select({"<script>",
    	"select curl,caid,csource,cip,dcreatedate,ctaskid,itasktype from t_visit_log where 1=1 ",
	    "<if test=\"aid!=null and aid!=''\">",
		"and caid=#{aid} ",
		"</if>",
    	"<if test=\"sd!=null and sd!=''\">",
    	"and dcreatedate &gt;= #{sd} ",
    	"</if>",
    	"<if test=\"ed!=null and ed!=''\">",
    	"and dcreatedate &lt;= #{ed} ",
    	"</if>",
    	"order by dcreatedate asc ",
    "</script>"})
    List<dto.VisitLog> getVisitLogs(@Param("aid")String aid,@Param("sd")Date sd,@Param("ed")Date ed);
}