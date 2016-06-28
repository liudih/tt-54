package services.member;

import java.util.List;

import dto.member.MemberRoleInfo;
import dto.member.role.MemberRoleBase;

public interface IMemberRoleService {

	public List<MemberRoleBase> getMemberRole();

	public List<MemberRoleInfo> search(String email, Integer siteid,
			Integer roleid, Integer page, Integer pageSize);

	public Integer searchCount(String email, Integer siteid, Integer roleid);

	public List<MemberRoleInfo> search(String email, Integer siteid,
			List<Integer> roleids, Integer page, Integer pageSize);

	public Integer searchCount(String email, Integer siteid,
			List<Integer> roleids);

	public List<Integer> getRoleIdByUserId(Integer iid);

	public Integer insertMemberRoleMap(Integer iuserid, Integer iroleid,
			String ccreateuser);

	public Integer deleteMemberRoleMap(Integer iuserid);

	public Integer checkMemberRoleMap(Integer iuserid, Integer iroldeid);
}
