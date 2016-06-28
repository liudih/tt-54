package service.tracking;

import interceptors.CacheResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mappers.tracking.AffiliateBannerMapper;
import mappers.tracking.AffiliateBaseMapper;
import mappers.tracking.AffiliateInfoMapper;
import services.member.login.LoginService;
import services.product.CategoryEnquiryService;
import valueobjects.base.Page;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.product.CategoryWebsiteWithName;
import entity.tracking.AffiliateBanner;
import entity.tracking.AffiliateBase;
import entity.tracking.AffiliateInfo;
import forms.tracking.AffiliateForm;

public class AffiliateService implements IAffiliateService {
	@Inject
	private AffiliateInfoMapper affiliateInfoMapper;
	@Inject
	private AffiliateBannerMapper affiliateBannerMapper;
	@Inject
	private AffiliateBaseMapper affiliateBaseMapper;
	@Inject
	private LoginService loginService;
	@Inject
	CategoryEnquiryService categoryEnquiryService;

	/* (non-Javadoc)
	 * @see service.tracking.IAffiliateService#addAffilateInfo(forms.tracking.AffiliateForm)
	 */
	@Override
	public boolean addAffilateInfo(AffiliateForm form) {
		AffiliateInfo info = new AffiliateInfo();
		AffiliateBase base = new AffiliateBase();
		info.setBexternal(true);
		info.setBreceivenotification(form.getBreceivenotification());
		info.setBstatus(true);
		info.setCaid(form.getCaid());
		if (loginService.getLoginData() == null) {
			return false;
		}
		info.setCemail(loginService.getLoginData().getEmail());
		base.setCcreateuser(loginService.getLoginData().getUsername());

		info.setCpaypalemail(form.getCemail());
		info.setIsalerid(null);
		info.setDcreatedate(new Date());
		info.setIwebsiteid(form.getIwebsiteid());
		int flag = affiliateInfoMapper.insertSelective(info);

		base.setCaid(form.getCaid());
		base.setDcreatedate(new Date());
		base.setItype(1);
		base.setIwebsiteid(form.getIwebsiteid());
		int flag2 = affiliateBaseMapper.insertSelective(base);

		return (flag > 0 && flag2 > 0);
	}

	/* (non-Javadoc)
	 * @see service.tracking.IAffiliateService#updateAffiliateInfo(forms.tracking.AffiliateForm)
	 */
	@Override
	public boolean updateAffiliateInfo(AffiliateForm form) {
		AffiliateInfo a = affiliateInfoMapper.selectByPrimaryKey(form.getIid());
		a.setCpaypalemail(form.getCpaypalemail());
		a.setBreceivenotification(form.getBreceivenotification());
		int flag = affiliateInfoMapper.updateByPrimaryKeySelective(a);
		return flag > 0;
	}

	/* (non-Javadoc)
	 * @see service.tracking.IAffiliateService#checkValidAId(forms.tracking.AffiliateForm)
	 */
	@Override
	public String checkValidAId(AffiliateForm form) {
		if (loginService.getLoginData() == null) {
			return "nologin";
		}
		String email = loginService.getLoginData().getEmail();
		List<AffiliateInfo> list1 = affiliateInfoMapper
				.getAffiliateInfoByEmailAndAid(email, form.getCaid());
		List<AffiliateBase> list2 = affiliateBaseMapper
				.getAffiliateBaseByAId(form.getCaid());
		if (list1.size() > 0 || list2.size() > 0) {
			return "exist";
		}
		return "success";
	}

	/* (non-Javadoc)
	 * @see service.tracking.IAffiliateService#getAffiliateInfoByEmail(java.lang.String)
	 */
	@Override
	public AffiliateInfo getAffiliateInfoByEmail(String email) {
		List<AffiliateInfo> list = affiliateInfoMapper
				.getAffiliateInfoByEmail(email);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see service.tracking.IAffiliateService#getAffiliateBannerPage(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page<AffiliateBanner> getAffiliateBannerPage(Integer lang,
			Integer siteid, Integer page, Integer pageSize, Integer type) {
		int pageIndex = (page - 1) * pageSize;
		List<AffiliateBanner> list = affiliateBannerMapper
				.getAffiliateBannerPage(siteid, pageIndex, pageSize, type);
		int count = affiliateBannerMapper.getAffiliateBannerCount(siteid, type);

		if (list.size() > 0) {
			List<Integer> cateIds = Lists.newArrayList();
			for (AffiliateBanner a : list) {
				if (a.getIcategoryid() != null) {
					cateIds.add(a.getIcategoryid());
				}
			}
			if (cateIds.size() > 0) {
				List<CategoryWebsiteWithName> clist = categoryEnquiryService
						.getCategoriesByCategoryIds(cateIds, lang, siteid);
				Map<Integer, CategoryWebsiteWithName> catemap = Maps
						.uniqueIndex(clist, c -> c.getIcategoryid());
				for (AffiliateBanner a : list) {
					if (catemap.get(a.getIcategoryid()) != null) {
						a.setCategoryName(catemap.get(a.getIcategoryid())
								.getCname());
					}
				}
			}
		}
		return new Page<AffiliateBanner>(list, count, page, pageSize);
	}

	/* (non-Javadoc)
	 * @see service.tracking.IAffiliateService#getBannerById(java.lang.Integer)
	 */
	@Override
	public AffiliateBanner getBannerById(Integer id) {
		return this.affiliateBannerMapper.selectByPrimaryKey(id);
	}

	/* (non-Javadoc)
	 * @see service.tracking.IAffiliateService#getAffiliateBannerById(java.lang.Integer)
	 */
	@Override
	public AffiliateBanner getAffiliateBannerById(Integer id) {
		return affiliateBannerMapper.selectByPrimaryKey(id);
	}

	/* (non-Javadoc)
	 * @see service.tracking.IAffiliateService#getAidByEmail(java.lang.String)
	 */
	@Override
	@CacheResult("tracking")
	public String getAidByEmail(String email) {
		return affiliateInfoMapper.getAidByEmail(email);
	}
}
