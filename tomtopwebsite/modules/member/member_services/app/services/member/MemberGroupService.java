package services.member;

import java.util.List;

import interceptors.CacheResult;

import javax.inject.Inject;

import mapper.member.MemberGroupMapper;
import dto.member.MemberGroup;

public class MemberGroupService {

	@Inject
	MemberGroupMapper mapper;

	public String getMemberGroupNameById(Integer groupId) {
		return mapper.getMemberGroupNameById(groupId);
	}

	@CacheResult
	public MemberGroup getMemberGroupByMemberId(int memberId) {
		return mapper.getMemberGroupByMemberId(memberId);
	}
	
	public List<MemberGroup> getMemberGroupsBySiteId(Integer siteId){
		return mapper.getMemberGroupsBySiteId(siteId);
	}
}
