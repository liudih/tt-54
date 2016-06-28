package mappers.tracking;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.tracking.AffiliateBanner;

public interface AffiliateBannerMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(AffiliateBanner record);

    int insertSelective(AffiliateBanner record);

    AffiliateBanner selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(AffiliateBanner record);

    int updateByPrimaryKeyWithBLOBs(AffiliateBanner record);

    int updateByPrimaryKey(AffiliateBanner record);
    
    @Select({"<script>",
    	"select * from t_affiliate_banner where iwebsiteid=#{0} ",
    	"<if test=\"t!=null and t==1\">",
    	"and cbannertype!='text'",
    	"</if>",
    	"<if test=\"t!=null and t==2\">",
    	"and cbannertype='text'",
    	"</if>",
    	"order by iid asc limit #{2} offset #{1}",
    	"</script>"})
    public List<AffiliateBanner> getAffiliateBannerPage(Integer siteid,Integer page,
    		Integer pageSize,@Param("t")Integer type);
    
    @Select({"<script>",
	    "select count(*) from t_affiliate_banner where iwebsiteid=#{0} ",
	    "<if test=\"t!=null and t==1\">",
    	"and cbannertype!='text'",
    	"</if>",
    	"<if test=\"t!=null and t==2\">",
    	"and cbannertype='text'",
    	"</if>",
    	"</script>"})
    public int getAffiliateBannerCount(Integer siteid,@Param("t")Integer type);
}