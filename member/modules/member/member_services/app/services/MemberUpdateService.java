package services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.member.MemberAddressMapper;
import mapper.member.MemberBaseMapper;
import mapper.member.MemberByStatisticsMapper;

import org.springframework.beans.BeanUtils;

import play.Logger;
import services.base.CountryService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.member.IMemberUpdateService;
import services.member.login.ILoginService;
import base.util.md5.MD5;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import context.WebContext;
import dto.Country;
import dto.member.MemberBase;
import dto.member.MemberByStatistics;
import forms.member.register.RegisterUpdateForm;
import valueobjects.base.LoginContext;

public class MemberUpdateService implements IMemberUpdateService {
	@Inject
	MemberBaseMapper mmapper;

	@Inject
	MemberAddressMapper maddrMapper;

	@Inject
	EventBus eventBus;

	@Inject
	MemberByStatisticsMapper memberByStatisticsMapper;

	@Inject
	ILoginService loginService;

	@Inject
	CountryService countryService;

	@Inject
	FoundationService foundation;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IMmemberUpdateService#mm(java.lang.String)
	 */
	@Override
	public MemberBase getUserByAccount(String account) {
		MemberBase mem = mmapper.getUserByAccount(account);
		return mem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IMmemberUpdateService#savaMember(forms.member.register.
	 * RegisterUpdateForm, context.WebContext)
	 */
	@Override
	public boolean savaMember(RegisterUpdateForm memberForm, WebContext context) {

		LoginContext loginCtx = foundation.getLoginContext(context);
		String email = loginCtx.getMemberID();
		// 如果在当前上下文找不到登录的用户直接更新失败
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		MemberBase mmbase = mmapper.getUserByEmail(email,
				foundation.getSiteID(context));
		BeanUtils.copyProperties(memberForm, mmbase);
		mmbase.setCemail(email);
		if (memberForm.getCcnewpassword() != null) {
			mmbase.setCpasswd(MD5.md5(memberForm.getCcnewpassword()));
		}
		int result = mmapper.updateByPrimaryKeySelective(mmbase);
		loginService.forceLogin(mmbase);
		Logger.debug("result:{}" + result + "");
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IMmemberUpdateService#saveMember(dto.member.MemberBase)
	 */
	@Override
	public void saveMember(MemberBase base) {
		mmapper.insertSelective(base);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IMmemberUpdateService#saveBatchMember(java.util.List)
	 */
	@Override
	public void saveBatchMember(List<MemberBase> members) {
		mmapper.insertBatch(members);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IMmemberUpdateService#SaveBuyStatistics(dto.member.
	 * MemberByStatistics)
	 */
	@Override
	public void SaveBuyStatistics(MemberByStatistics memberByStatistics) {
		if (memberByStatisticsMapper.select(memberByStatistics.getCemail()) != null) {
			memberByStatisticsMapper.update(memberByStatistics);
		} else {
			memberByStatisticsMapper.insert(memberByStatistics);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IMmemberUpdateService#getMemberByEmail(java.lang.String,
	 * context.WebContext)
	 */
	@Override
	public MemberBase getMemberByEmail(String email, WebContext context) {
		int site = foundation.getSiteID(context);
		return mmapper.getUserByEmail(email, site);
	}

	/*
	 * public String save(List<com.website.dto.member.Member> members) {
	 * List<String> results = Lists.transform(members, obj -> this.save(obj));
	 * String result = ""; for (String re : results) { if (re != null &&
	 * re.trim().length() > 0) { result += re + System.lineSeparator(); } }
	 * return result; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IMmemberUpdateService#save(com.website.dto.member.Member[])
	 */
	@Override
	public String save(com.website.dto.member.Member[] members) {

		List<String> emails = Lists.transform(Arrays.asList(members),
				obj -> obj.getEmail().toLowerCase());
		List<String> exEmails = mmapper.getExistsEmails(emails);

		String result = "";
		List<MemberBase> mlist = Lists.newArrayList();
		List<dto.member.MemberAddress> addrlist = Lists.newArrayList();
		for (com.website.dto.member.Member member : members) {
			if (member.getEmail() == null
					|| member.getEmail().trim().length() == 0) {
				result += "invalid email" + System.lineSeparator();
				continue;
			}
			member.setEmail(member.getEmail().toLowerCase());
			if (exEmails.contains(member.getEmail())) {
				result += "duplicate email : " + member.getEmail()
						+ System.lineSeparator();//
				continue;
			}
			mlist.add(getMember(member));
			List<dto.member.MemberAddress> address = getMemberAddress(member);
			if (null != address && address.size() > 0)
				addrlist.addAll(address);
		}
		try {
			if (mlist.size() > 0)
				mmapper.insertBatch(mlist);
			if (null != addrlist && addrlist.size() > 0)
				maddrMapper.insertBatch(addrlist);
		} catch (Exception ex) {
			ex.printStackTrace();
			List<String> emlist = Lists.transform(mlist, em -> em.getCemail());
			if (emlist != null && emlist.size() > 0) {
				mmapper.deleteByEmail(emlist);
				maddrMapper.deleteByEmail(emlist);
			}
			result += "insert error: " + ex.getMessage()
					+ System.lineSeparator();
		}
		return result;
	}

	private dto.member.MemberBase getMember(com.website.dto.member.Member member) {
		dto.member.MemberBase mbase = new dto.member.MemberBase();

		mbase.setBnewsletter(false);
		mbase.setCaboutme("");
		mbase.setCaccount("");
		mbase.setCcountry(member.getCountryCode());
		mbase.setCemail(member.getEmail());
		mbase.setCfirstname(member.getFirstname());
		mbase.setCforumsnickname("");
		mbase.setClastname(member.getLastname());
		mbase.setCmiddlename(member.getMiddlename());
		mbase.setCpasswd(member.getPassword());
		mbase.setCprefix(member.getPrefix());
		mbase.setCsuffix(member.getSuffix());
		mbase.setCtaxnumber(member.getTaxNumber());
		mbase.setDbirth(member.getBirthday());
		// mbase.setIgender(merber.getGender());
		mbase.setIgroupid(member.getGroupId());
		// mbase.setIid(iid);
		mbase.setIwebsiteid(member.getWebsiteId());
		mbase.setDcreatedate(new Date());

		return mbase;
	}

	private List<dto.member.MemberAddress> getMemberAddress(
			com.website.dto.member.Member member) {

		if (member.getAddresses() == null) {
			return null;
		}
		List<Country> countrys = countryService.getAllCountries();
		final Map<String, Country> cmap = Maps.uniqueIndex(countrys,
				obj -> obj.getCshortname());
		return Lists.transform(member.getAddresses(), msadd -> {
			dto.member.MemberAddress maddr = new dto.member.MemberAddress();
			maddr.setBdefault(msadd.getIsDefault());
			maddr.setCcity(msadd.getCity());
			maddr.setCcompany(msadd.getCompany());
			maddr.setCfax(msadd.getFax());
			maddr.setCfirstname(msadd.getFirstname());

			maddr.setClastname(msadd.getLastname());
			maddr.setCmemberemail(msadd.getMemberEmail());
			maddr.setCmiddlename(msadd.getMiddlename());
			maddr.setCpostalcode(msadd.getPostalcode());
			maddr.setCprovince(msadd.getProvince());

			maddr.setCstreetaddress(msadd.getStreetAddress());
			maddr.setCtelephone(msadd.getTelephone());
			maddr.setCvatnumber(msadd.getVatnumber());
			maddr.setIaddressid(msadd.getAddressType());
			if (cmap.containsKey(msadd.getCountryCode())) {
				maddr.setIcountry(cmap.get(msadd.getCountryCode()).getIid());
			}

			maddr.setIid(msadd.getId());
			return maddr;
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IMmemberUpdateService#SaveMemberAccount(java.lang.String)
	 */
	@Override
	public void SaveMemberAccount(String email) {
		mmapper.update(email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IMmemberUpdateService#SaveMemberPasswd(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean SaveMemberPasswd(String email, String cpasswd,
			WebContext webcontext) {
		Integer websiteId = foundation.getSiteID(webcontext);
		int result = mmapper.UpdatesMemberPassword(email, cpasswd, websiteId);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IMmemberUpdateService#updateMember(dto.member.MemberBase)
	 */
	@Override
	public boolean updateMember(MemberBase mmbase) {
		int result = mmapper.updateByPrimaryKeySelective(mmbase);
		return result > 0 ? true : false;

	}

	public dto.member.MemberBase getMemberDto(String email, WebContext context) {
		int site = foundation.getSiteID(context);
		return mmapper.getUserByEmail(email, site);
	}

	@Override
	public boolean changePassword(String token, String newPassword,
			WebContext webContext) {
		Integer websiteId = foundation.getSiteID(webContext);
		int result = mmapper.changePassword(token, newPassword, websiteId);
		return result > 0 ? true : false;
	}
}
