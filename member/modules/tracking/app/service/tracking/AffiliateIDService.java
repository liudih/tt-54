package service.tracking;

import interceptors.CacheResult;

import javax.inject.Inject;

import dto.tracking.IAffiliateIDMapperEnquiryDao;
import mappers.tracking.AffiliateIDMapper;
import entity.tracking.Affiliate;

public class AffiliateIDService implements IAffiliateIDService {

	@Inject
	IAffiliateIDMapperEnquiryDao mapper;

	@Override
	@CacheResult("tracking")
	public String getAffiliateIDByReferer(int siteID, String referer) {
		return mapper.getAffiliateIDByReferer(siteID, referer);
	}

	@Override
	@CacheResult("tracking")
	public Affiliate getAffiliate(int siteID, String aid) {
		return mapper.getAffiliate(siteID, aid);
	}

}
