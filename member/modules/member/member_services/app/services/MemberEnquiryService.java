package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.member.MemberBaseMapper;
import mapper.member.MemberOtherIdMapper;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.member.IMemberEnquiryService;
import valueobjects.member.MemberOtherIdentity;
import context.WebContext;
import dto.member.MemberBase;
import dto.member.MemberOtherId;
import forms.member.memberSearch.MemberSearchForm;

public class MemberEnquiryService implements IMemberEnquiryService {

	@Inject
	MemberBaseMapper memberBaseMapper;

	@Inject
	MemberOtherIdMapper otherIdMapper;

	@Inject
	FoundationService foundation;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#getUserNameByMemberEmail(java.lang.String)
	 */
	public String getUserNameByMemberEmail(String email) {
		return memberBaseMapper.getUserName(email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#getUserNamesByMemberEmail(java.util.List)
	 */
	public List<MemberBase> getUserNamesByMemberEmail(List<String> list) {

		return memberBaseMapper.getUserNames(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#getMemberByMemberEmail(java.lang.String,
	 * context.WebContext)
	 */
	public MemberBase getMemberByMemberEmail(String email, WebContext context) {
		int site = foundation.getSiteID(context);
		return memberBaseMapper.getUserByEmail(email, site);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#getMemberByOtherIdentity(valueobjects.
	 * member.MemberOtherIdentity, context.WebContext)
	 */
	public MemberBase getMemberByOtherIdentity(MemberOtherIdentity other,
			WebContext context) {
		MemberOtherId otherId = otherIdMapper.getBySource(other.getSource(),
				other.getId());
		if (otherId != null) {
			int site = foundation.getSiteID(context);
			return memberBaseMapper.getUserByEmail(other.getEmail(), site);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#searchMemberMessage(forms.member.memberSearch
	 * .MemberSearchForm)
	 */
	public List<MemberBase> searchMemberMessage(
			MemberSearchForm memberSearchForm) {
		return memberBaseMapper.getMemberMessage(memberSearchForm.getEmail(),
				memberSearchForm.getSiteId(), memberSearchForm.getCaccount(),
				memberSearchForm.getPageSize(), memberSearchForm.getPageNum(),
				memberSearchForm.getBlackUserStatus());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#searchMemberMessageForDate(forms.member
	 * .memberSearch .MemberSearchForm)
	 */
	public List<MemberBase> searchMemberMessageForDate(
			MemberSearchForm memberSearchForm) {
		Date start = null;
		Date end = null;
		if (!StringUtils.isEmpty(memberSearchForm.getStart())) {
			String start1 = memberSearchForm.getStart();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				start = sdf.parse(start1);
			} catch (ParseException e) {
				e.printStackTrace();
				start = null;
			}
		}
		if (!StringUtils.isEmpty(memberSearchForm.getEnd())) {
			String end1 = memberSearchForm.getEnd();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				e.printStackTrace();
				end = null;
			}
		}
		return memberBaseMapper.getMemberMessageForDate(
				memberSearchForm.getEmail(), memberSearchForm.getSiteId(),
				memberSearchForm.getCaccount(), memberSearchForm.getPageSize(),
				memberSearchForm.getPageNum(),
				memberSearchForm.getBlackUserStatus(),
				memberSearchForm.getContry(), memberSearchForm.getVhost(),
				memberSearchForm.getBactivated(),
				memberSearchForm.getBnewsletter(), start, end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#searchMemberCount(forms.member.memberSearch
	 * .MemberSearchForm)
	 */
	public Integer searchMemberCount(MemberSearchForm memberSearchForm) {
		Date start = null;
		Date end = null;
		if (!StringUtils.isEmpty(memberSearchForm.getStart())) {
			String start1 = memberSearchForm.getStart();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				start = sdf.parse(start1);
			} catch (ParseException e) {
				e.printStackTrace();
				start = null;
			}
		}
		if (!StringUtils.isEmpty(memberSearchForm.getEnd())) {
			String end1 = memberSearchForm.getEnd();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				e.printStackTrace();
				end = null;
			}
		}
		return memberBaseMapper.getMemberCount(memberSearchForm.getEmail(),
				memberSearchForm.getSiteId(), memberSearchForm.getCaccount(),
				memberSearchForm.getBlackUserStatus(),
				memberSearchForm.getContry(), memberSearchForm.getVhost(),
				memberSearchForm.getBactivated(),
				memberSearchForm.getBnewsletter(), start, end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.IMemberEnquiryService#searchMemberCountByDate(java.util.Date,
	 * java.util.Date)
	 */
	public Integer searchMemberCountByDate(Date start, Date end) {
		return memberBaseMapper.getMemberCountByDate(start, end);
	}
	
	/**
	 * 通过uuid获取用户
	 * 
	 * @author lijun
	 * @return
	 */
	public MemberBase getMemberByUuid(String uuid){
		if(uuid == null || uuid.length() == 0){
			return null;
		}
		return this.memberBaseMapper.getUserByUuid(uuid);
	}
}
