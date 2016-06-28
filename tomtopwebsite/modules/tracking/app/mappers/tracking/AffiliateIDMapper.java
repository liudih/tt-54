package mappers.tracking;

import org.apache.ibatis.annotations.Select;

import entity.tracking.Affiliate;

public interface AffiliateIDMapper {

	@Select("SELECT caid FROM t_affiliate_referrer WHERE iwebsiteid=#{0} AND creferrer=#{1}")
	String getAffiliateIDByReferer(int siteID, String referer);

	@Select("SELECT * FROM t_affiliate_base WHERE iwebsiteid=#{0} AND caid=#{1}")
	Affiliate getAffiliate(int siteID, String aid);

}
