package services.manager;

import interceptors.CacheResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.inject.Inject;

import mapper.manager.AdminUserMapper;
import mappers.tracking.AffiliateBaseMapper;
import mappers.tracking.AffiliateInfoMapper;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.mvc.Security.Authenticated;
import services.base.WebsiteService;
import session.ISessionService;
import valueobjects.base.Page;
import authenticators.member.MemberLoginAuthenticator;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import dto.Website;
import entity.manager.AdminUser;
import entity.tracking.AffiliateBase;
import entity.tracking.AffiliateInfo;
import forms.AffiliateInfoForm;
import forms.AffiliateUserForm;

@Authenticated(MemberLoginAuthenticator.class)
public class AffiliateService {
	@Inject
	private AffiliateInfoMapper mapper;

	@Inject
	private AffiliateBaseMapper affiliateBaseMapper;

	@Inject
	ISessionService sessionService;

	@Inject
	WebsiteService websiteService;

	@Inject
	AdminUserMapper adminUserMapper;

	@Inject
	AdminUserService adminUserService;

	// 0 ＝ 渠道 （如shareasales 是一个网站，导向到我们流量，我们给他一个 aid ＝ shareasales）
	// 1 ＝ 个体 （如会员申请的 AID）
	// 2 ＝ 营销人员（内部员工）
	int itype = 2;

	public boolean addAffilateInfo(AffiliateUserForm form) {
		AffiliateInfo info = new AffiliateInfo();
		info.setIwebsiteid(form.getIwebsiteid());
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		int isalerid = 0;
		String ccreateuser = null;
		if (null != user) {
			isalerid = user.getIid();
			ccreateuser = user.getCusername();
		}
		info.setCemail(form.getCemail().toLowerCase());
		info.setCaid(form.getCaid().replaceAll(" ", ""));
		info.setIsalerid(isalerid);
		info.setCpaypalemail(form.getCpaypalemail());
		info.setBreceivenotification(form.isBreceivenotification());
		info.setBstatus(form.isBstatus());
		info.setBexternal(form.isBexternal());
		info.setDcreatedate(new Date());
		int flag = mapper.insertSelective(info);

		AffiliateBase base = new AffiliateBase();
		base.setCaid(form.getCaid());
		base.setIwebsiteid(form.getIwebsiteid());
		base.setItype(itype);
		base.setCcreateuser(ccreateuser);
		base.setDcreatedate(new Date());
		int flag2 = affiliateBaseMapper.insertSelective(base);

		return (flag > 0 && flag2 > 0);
	}

	public Page<dto.AffiliateInfo> getAffiliateUserPage(
			AffiliateInfoForm affiliateInfoForm) {
		int pageSize = 10;
		int pageIndex = (affiliateInfoForm.getPage() - 1) * pageSize;
		Boolean st = null;
		Boolean no = null;
		if (affiliateInfoForm.getStatus() != null) {
			if (affiliateInfoForm.getStatus() == 1) {
				st = true;
			}
			if (affiliateInfoForm.getStatus() == 0) {
				st = false;
			}
		}
		if (affiliateInfoForm.getNotice() != null) {
			if (affiliateInfoForm.getNotice() == 1) {
				no = true;
			}
			if (affiliateInfoForm.getNotice() == 0) {
				no = false;
			}
		}
		List<AffiliateInfo> list = mapper.getAffiliateInfoPage(pageIndex,
				pageSize, affiliateInfoForm.getSaler(),
				affiliateInfoForm.getAid(), affiliateInfoForm.getPaypalemail(),
				affiliateInfoForm.getWeb(), no, st, 0);
		List<Website> websites = websiteService.getAll();
		List<dto.AdminUser> salers = adminUserService.getAllAdminUser();
		List<dto.AffiliateInfo> result = Lists.newArrayList();
		for (AffiliateInfo aff : list) {
			dto.AffiliateInfo info = new dto.AffiliateInfo();
			BeanUtils.copyProperties(aff, info);
			info.setWebsitename(findWebsiteName(websites, info.getIwebsiteid()));
			info.setDcreatedate(aff.getDcreatedate());
			info.setSalername(findSalerName(salers, info.getIsalerid()));
			result.add(info);
		}

		int count = mapper.getAffiliateInfoCount(affiliateInfoForm.getSaler(),
				affiliateInfoForm.getAid(), affiliateInfoForm.getPaypalemail(),
				affiliateInfoForm.getWeb(), no, st);
		return new Page<dto.AffiliateInfo>(result, count,
				affiliateInfoForm.getPage(), pageSize);
	}

	private String findWebsiteName(List<Website> list, Integer iid) {
		if (list != null) {
			for (Website site : list) {
				if (site != null && site.getIid() == iid) {
					return site.getCcode();
				}
			}
		}
		return null;
	}

	private String findSalerName(List<dto.AdminUser> list, Integer iid) {
		if (list != null) {
			for (dto.AdminUser l : list) {
				if (l != null && iid != null && l.getIid() == iid) {
					return l.getCusername();
				}
			}
		}
		return null;
	}

	public boolean delUser(String aid) {
		int flag = mapper.deleteByAid(aid);
		int flag2 = affiliateBaseMapper.deleteByAid(aid);
		return (flag > 0 && flag2 > 0);
	}

	public boolean isHasCaid(String aid, Integer website) {
		AffiliateInfo info = mapper.getAffiliateInfoByAidAndSite(aid, website);
		if (info == null) {
			return true;
		}
		return false;
	}

	public dto.AffiliateInfo getInfo(String aid) {
		AffiliateInfo info = mapper.getAffiliateInfoByAid(aid);
		if (info != null) {
			dto.AffiliateInfo infoVo = new dto.AffiliateInfo();
			BeanUtils.copyProperties(info, infoVo);
			return infoVo;
		}
		return null;
	}
	
	public List<dto.AffiliateInfo> getInfos(List<String> aids) {
		List<AffiliateInfo> infos = mapper.getAffiliateInfoByAids(aids);
		List<dto.AffiliateInfo> infoList = new ArrayList<dto.AffiliateInfo>();
		dto.AffiliateInfo infoVo = null;
		for(AffiliateInfo info : infos){
			if (info != null) {
				infoVo = new dto.AffiliateInfo();
				BeanUtils.copyProperties(info, infoVo);
				infoList.add(infoVo);
			}
		}
		return infoList;
	}

	public boolean updateAffiliateInfo(AffiliateUserForm form) {
		AffiliateInfo info = new AffiliateInfo();
		info.setIid(form.getIid());
		info.setIwebsiteid(form.getIwebsiteid());
		info.setCpaypalemail(form.getCpaypalemail());
		info.setBexternal(form.isBexternal());
		info.setBreceivenotification(form.isBreceivenotification());
		info.setBstatus(form.isBstatus());
		info.setCemail(form.getCemail());
		int flag = mapper.updateByPrimaryKeySelective(info);
		return (flag > 0);
	}

	public boolean isNotExist(String email) {
		if (mapper.isNotExist(email) == null) {
			return true;
		}
		return false;
	}
	
	public boolean emaiIsNotExist(String email, Integer website) {
		if (mapper.getAffiliateByEmailAndSite(email, website) == null) {
			return true;
		}
		return false;
	}

	public String getEmail(String aid) {
		return mapper.getEmail(aid);
	}

	/**
	 * 获取有aid的后台管理员列表
	 * 
	 * @return
	 */
	public List<AdminUser> getAidAdminUser() {
		List<AffiliateInfo> alist = mapper.getAffiliateInfoForAdmin();
		List<Integer> salerIds = Lists.transform(alist, a -> a.getIsalerid());
		if (salerIds == null || salerIds.size() == 0) {
			return Lists.newArrayList();
		}
		List<AdminUser> ulist = adminUserMapper.getAdminUserList(salerIds);
		return ulist;
	}

	/**
	 * 通过userid得到aid
	 * 
	 * @return
	 */
	public String getAidsByUserid(Integer userid) {
		if (userid == null || userid == 0) {
			return null;
		}
		List<AffiliateInfo> alist = mapper.getInfoBySalerId(userid, 0);
		if (alist.size() == 0) {
			return "-1";
		}
		String aids = String
				.join(",", Lists.transform(alist, a -> a.getCaid()));
		return aids;
	}

	public List<AffiliateInfo> getAffiliateInfoByAids(List<String> aids) {

		return mapper.getAffiliateInfoByAids(aids);
	}

	public List<AffiliateInfo> getInfoBySalerIds(List<Integer> salerids) {

		return mapper.getInfoBySalerIds(salerids);
	}

	public List<AffiliateInfo> getInfoBySalerIdAndAid(int salerid, String aid) {

		return mapper.getInfoBySalerIdAndAid(salerid, aid);
	}

	@CacheResult("tracking")
	public List<AffiliateInfo> getInfoBySalerId(int salerid) {

		return mapper.getInfoBySalerId(salerid, 0);
	}

	public Map<String, Object> saveAidFromMayi(JsonNode jnode) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		Iterator<JsonNode> jlist = jnode.iterator();
		int updateNum = 0;
		while (jlist.hasNext()) {
			JsonNode next = jlist.next();
			String jobnumber = next.get("jobnumber").asText();
			dto.AdminUser admin = adminUserService
					.getAdminUserByJobnumber(jobnumber);
			if (admin != null) {
				JsonNode data = next.get("data");
				Iterator<JsonNode> jlist2 = data.iterator();
				while (jlist2.hasNext()) {
					JsonNode next2 = jlist2.next();
					String aid = next2.get("aid").asText();
					String email = next2.get("email").asText();
					if (aid != null && !"".equals(aid)) {
						AffiliateInfo a = mapper.getAffiliateInfoByAid(aid);
						if (a != null) {
							a.setCemail(email);
							a.setIsalerid(admin.getIid());
							mapper.updateByPrimaryKeySelective(a);
							updateNum++;
						} else {
							AffiliateInfo info = new AffiliateInfo();
							info.setBexternal(false);
							info.setCaid(aid);
							info.setBreceivenotification(true);
							info.setBstatus(true);
							info.setCemail(email);
							info.setCpaypalemail(email);
							info.setIsalerid(admin.getIid());
							info.setDcreatedate(new Date());
							info.setIwebsiteid(1);
							mapper.insertSelective(info);
							updateNum++;
						}
						AffiliateBase base = affiliateBaseMapper.getByAId(aid);
						if (base == null) {
							AffiliateBase b = new AffiliateBase();
							b.setCcreateuser("mayi-api");
							b.setCaid(aid);
							b.setDcreatedate(new Date());
							b.setItype(2);
							b.setIwebsiteid(1);
							affiliateBaseMapper.insertSelective(b);
						}
					}
				}
			}
		}
		mjson.put("result", "success");
		mjson.put("updateNum", updateNum);
		Logger.debug("mayi api update aid:{}", updateNum);
		return mjson;
	}
}
