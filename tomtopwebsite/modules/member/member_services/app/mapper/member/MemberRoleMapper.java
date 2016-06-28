package mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.member.MemberRoleInfo;
import dto.member.role.MemberRoleBase;

public interface MemberRoleMapper {

	@Select("select iid,crolename,ccreateuser,dcreatedate,cremark from t_member_role")
	List<MemberRoleBase> getMemberRoleAll();

	@Select("<script> "
			+ "select distinct mb.iid iid,mb.cemail email,mb.iwebsiteid siteid from t_member_base mb "
			+ "<if test='roleid != null '> ,t_member_map mm where mb.iid=mm.iuserid </if> <if test='roleid == null'> where 1=1 </if>"
			+ "<if test='email != null'> and mb.cemail=#{email} </if>"
			+ "<if test='siteid != null'> and mb.iwebsiteid=#{siteid} </if> <if test='roleid != null'> and mm.iroleid=#{roleid} </if> "
			+ "order by mb.iid limit #{pageSize} offset (#{page}-1)*#{pageSize} </script>")
	List<MemberRoleInfo> searchMemberRoleInfo(@Param("email") String email,
			@Param("siteid") Integer siteid, @Param("roleid") Integer roleid,
			@Param("page") Integer page, @Param("pageSize") Integer pageSize);

	@Select("<script> "
			+ "select count(distinct mb.iid) from t_member_base mb "
			+ "<if test='roleid != null'> ,t_member_map mm where mb.iid=mm.iuserid </if> <if test='roleid == null'> where 1=1 </if>"
			+ "<if test='email != null'> and mb.cemail=#{email} </if>"
			+ "<if test='siteid != null'> and mb.iwebsiteid=#{siteid} </if> <if test='roleid != null'> and mm.iroleid=#{roleid} </if> </script> ")
	Integer searchMemberRoleInfoCount(@Param("email") String email,
			@Param("siteid") Integer siteid, @Param("roleid") Integer roleid);

	@Select("<script> "
			+ "select distinct mb.iid iid,mb.cemail email,mb.iwebsiteid siteid from t_member_base mb "
			+ "<if test='roleids != null '>,t_member_map mm where mb.iid=mm.iuserid </if> <if test='roleids == null'> where 1=1 </if>"
			+ "<if test='email != null'> and mb.cemail=#{email} </if>"
			+ "<if test='siteid != null'> and mb.iwebsiteid=#{siteid} </if> <if test='roleids != null'> "
			+ "and mm.iroleid in <foreach item='item' index='index' collection='roleids' open='(' separator=',' close=')' >#{item}</foreach></if> "
			+ " limit #{pageSize} offset (#{page}-1)*#{pageSize} </script>")
	List<MemberRoleInfo> searchMemberRoleInfoByRoleIds(
			@Param("email") String email, @Param("siteid") Integer siteid,
			@Param("roleids") List<Integer> roleids,
			@Param("page") Integer page, @Param("pageSize") Integer pageSize);

	@Select("<script> "
			+ "select count(distinct mb.iid) from t_member_base mb "
			+ "<if test='roleids != null'> ,t_member_map mm where mb.iid=mm.iuserid </if> <if test='roleids == null'> where 1=1 </if>"
			+ "<if test='email != null'> and mb.cemail=#{email} </if>"
			+ "<if test='siteid != null'> and mb.iwebsiteid=#{siteid} </if> <if test='roleids != null'> "
			+ "and mm.iroleid in <foreach item='item' index='index' collection='roleids' open='(' separator=',' close=')' >#{item}</foreach></if> "
			+ " </script> ")
	Integer searchMemberRoleInfoCountByRoleIds(@Param("email") String email,
			@Param("siteid") Integer siteid,
			@Param("roleids") List<Integer> roleids);

	@Select("select iroleid from t_member_map where iuserid=#{0}")
	List<Integer> searchRoleMapById(Integer iid);

	@Select("select iroleid from t_member_map where iuserid=#{iuserid} and iroleid=#{iroleid}")
	Integer searchMemberMapByRoleId(@Param("iuserid") Integer iuserid,
			@Param("iroleid") Integer iroleid);

	@Insert("insert into t_member_map(iuserid,iroleid,ccreateuser,dcreatedate) values(#{iuserid},#{iroleid},#{ccreateuser},now())")
	Integer insertMemberMap(@Param("iuserid") Integer iuserid,
			@Param("iroleid") Integer iroleid,
			@Param("ccreateuser") String ccreateuser);

	@Delete("delete from t_member_map where iuserid=#{iuserid}")
	Integer deleteMemberMapByUser(@Param("iuserid") Integer iuserid);

	@Select("select mb.cemail from t_member_base mb "
			+ "where EXISTS(select mm.iuserid from t_member_map mm where mb.iid=mm.iuserid and iroleid=#{iroleid}); ")
	List<String> getEmailByRoleId(@Param("iroleid") Integer iroleid);
}