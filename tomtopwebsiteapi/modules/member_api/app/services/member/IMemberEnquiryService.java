package services.member;

import java.util.Date;
import java.util.List;

import valueobjects.member.MemberOtherIdentity;
import context.WebContext;
import dto.member.MemberBase;
import forms.member.memberSearch.MemberSearchForm;

public interface IMemberEnquiryService {

	public String getUserNameByMemberEmail(String email);

	public List<MemberBase> getUserNamesByMemberEmail(List<String> list);

	public MemberBase getMemberByMemberEmail(String email, WebContext context);

	public MemberBase getMemberByOtherIdentity(MemberOtherIdentity other,
			WebContext context);

	public List<MemberBase> searchMemberMessage(
			MemberSearchForm memberSearchForm);

	public List<MemberBase> searchMemberMessageForDate(
			MemberSearchForm memberSearchForm);

	public Integer searchMemberCount(MemberSearchForm memberSearchForm);

	public Integer searchMemberCountByDate(Date start, Date end);

	/**
	 * 
	 * @author lijun
	 * @return
	 */
	public MemberBase getMemberByUuid(String uuid);
}