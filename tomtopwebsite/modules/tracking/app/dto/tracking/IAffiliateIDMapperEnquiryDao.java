package dto.tracking;

import entity.tracking.Affiliate;

import org.apache.ibatis.annotations.Select;

public interface IAffiliateIDMapperEnquiryDao {

	String getAffiliateIDByReferer(int siteID, String referer);

	Affiliate getAffiliate(int siteID, String aid);

}
