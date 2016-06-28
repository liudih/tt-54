package mappers.tracking;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.tracking.AffiliateReferrer;

public interface AffiliateReferrerMapper {
    
    @Select({ "<script>", "select * from t_affiliate_referrer where 1=1 ",
    	"<if test=\"website!=null and website != 0\">", " and iwebsiteid = #{website}", "</if>",
    	"</script>"})
    public List<AffiliateReferrer> getAllAffiliateReferrer(@Param("website")Integer website);
}