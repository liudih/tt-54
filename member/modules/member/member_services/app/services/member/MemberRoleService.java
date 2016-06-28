package services.member;

import java.util.List;

import javax.inject.Inject;

import mapper.member.MemberRoleMapper;
import dto.member.MemberRoleInfo;
import dto.member.role.MemberRoleBase;

public class MemberRoleService implements IMemberRoleService {

	@Inject
	MemberRoleMapper memberRoleMapper;

	@Override
	public List<MemberRoleBase> getMemberRole() {
		return memberRoleMapper.getMemberRoleAll();
	}

	public List<MemberRoleInfo> search(String email, Integer siteid,
			Integer roleid, Integer page, Integer pageSize) {
		return memberRoleMapper.searchMemberRoleInfo(email, siteid, roleid,
				page, pageSize);
	}

	public Integer searchCount(String email, Integer siteid, Integer roleid) {
		return memberRoleMapper
				.searchMemberRoleInfoCount(email, siteid, roleid);
	}

	@Override
	public List<Integer> getRoleIdByUserId(Integer iid) {
		return memberRoleMapper.searchRoleMapById(iid);
	}

	@Override
	public Integer insertMemberRoleMap(Integer iuserid, Integer iroleid,
			String ccreateuser) {
		return memberRoleMapper.insertMemberMap(iuserid, iroleid, ccreateuser);
	}

	@Override
	public Integer deleteMemberRoleMap(Integer iuserid) {
		return memberRoleMapper.deleteMemberMapByUser(iuserid);
	}

	@Override
	public Integer checkMemberRoleMap(Integer iuserid, Integer iroleid) {

		return memberRoleMapper.searchMemberMapByRoleId(iuserid, iroleid);
	}

	@Override
	public List<MemberRoleInfo> search(String email, Integer siteid,
			List<Integer> roleids, Integer page, Integer pageSize) {
		return memberRoleMapper.searchMemberRoleInfoByRoleIds(email, siteid,
				roleids, page, pageSize);
	}

	@Override
	public Integer searchCount(String email, Integer siteid,
			List<Integer> roleids) {
		return memberRoleMapper.searchMemberRoleInfoCountByRoleIds(email,
				siteid, roleids);
	}

}
