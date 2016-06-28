package mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.member.MemberGroup;

public interface MemberGroupMapper {
	@Select("select cgroupname from t_member_group where iid = #{groupId} ")
	String getMemberGroupNameById(Integer groupId);

	@Select("select g.* from t_member_group g where g.iid = ("
			+ "select m.igroupid from t_member_base m where m.iid = #{0}) limit 1")
	MemberGroup getMemberGroupByMemberId(Integer memberId);
	
	@Select("select * from t_member_group where iwebsiteid=#{0}")
	List<MemberGroup> getMemberGroupsBySiteId(Integer siteId);
}