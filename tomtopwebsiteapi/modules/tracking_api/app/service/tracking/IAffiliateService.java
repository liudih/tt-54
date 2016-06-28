package service.tracking;

import valueobjects.base.Page;
import entity.tracking.AffiliateBanner;
import entity.tracking.AffiliateInfo;
import forms.tracking.AffiliateForm;

public interface IAffiliateService {

	public abstract boolean addAffilateInfo(AffiliateForm form);

	public abstract boolean updateAffiliateInfo(AffiliateForm form);

	public abstract String checkValidAId(AffiliateForm form);

	public abstract AffiliateInfo getAffiliateInfoByEmail(String email);

	public abstract Page<AffiliateBanner> getAffiliateBannerPage(Integer lang,
			Integer siteid, Integer page, Integer pageSize, Integer type);

	public abstract AffiliateBanner getBannerById(Integer id);

	public abstract AffiliateBanner getAffiliateBannerById(Integer id);

	public abstract String getAidByEmail(String email);

}