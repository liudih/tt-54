package dto.tracking.impl;

import mappers.tracking.AffiliateIDMapper;

import com.google.inject.Inject;

import dto.tracking.IAffiliateIDMapperEnquiryDao;
import entity.tracking.Affiliate;

public class AffiliateIDMapperEnquiryDao implements
		IAffiliateIDMapperEnquiryDao {

	@Inject
	AffiliateIDMapper affiliateIDMapper;

	@Override
	public String getAffiliateIDByReferer(int siteID, String referer) {
		return affiliateIDMapper.getAffiliateIDByReferer(siteID, referer);
	}

	@Override
	public Affiliate getAffiliate(int siteID, String aid) {
		return affiliateIDMapper.getAffiliate(siteID, aid);
	}

}
