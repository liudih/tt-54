package mappers.tracking;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import entity.tracking.AffiliateReferrer;

public interface AffiliateReferrerMapper {
    
    @Select("select * from t_affiliate_referrer")
    public List<AffiliateReferrer> getAllAffiliateReferrer();
}