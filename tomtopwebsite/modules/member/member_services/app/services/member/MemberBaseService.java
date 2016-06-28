package services.member;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.member.MemberBaseMapper;

import org.springframework.beans.BeanUtils;

import context.WebContext;
import services.base.FoundationService;
import dto.Member;
import dto.member.MemberBase;

public class MemberBaseService {
	public static final Integer PER_DATE = 86400;
	@Inject
	MemberBaseMapper memberBaseMapper;
	@Inject
	FoundationService foundation;

	public Member getMember(String email,WebContext context) {
		int site = foundation.getSiteID(context);
		MemberBase memberBase = memberBaseMapper.getUserByEmail(email,site);
		if (memberBase != null) {
			dto.Member member = new dto.Member();
			BeanUtils.copyProperties(memberBase, member);
			return member;
		}
		return null;
	}

	public List<MemberBase> getAllMembers(Date startDate, Date endDate, Integer pageNum, Integer pageSize) {
		return memberBaseMapper.getAllMembers(startDate, endDate, pageNum, pageSize);
	}
}
