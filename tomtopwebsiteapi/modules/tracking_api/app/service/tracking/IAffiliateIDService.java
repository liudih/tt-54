package service.tracking;


import entity.tracking.Affiliate;

public interface IAffiliateIDService {
	
	String getAffiliateIDByReferer(int siteID, String referer);

	Affiliate getAffiliate(int siteID, String aid);
}
