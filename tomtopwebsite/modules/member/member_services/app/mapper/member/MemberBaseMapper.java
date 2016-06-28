package mapper.member;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.member.MemberBase;
import forms.member.register.RegisterForm;

public interface MemberBaseMapper {

	@Insert("insert into t_member_base(caccount,cemail,cpasswd) values(#{caccount},#{cemail},#{cpasswd})")
	int register(RegisterForm rf);

	@Insert("insert into t_member_base(cemail,cpasswd,igroupid,ccountry,bnewsletter,bactivated,cvhost,iwebsiteid,cfirstname,clastname) "
			+ "values(#{cemail},#{cpasswd},#{igroupid},#{ccountry},#{bnewsletter},#{bactivated},#{cvhost},#{iwebsiteid},#{cfirstname},#{clastname})")
	int insertSelective(MemberBase record);

	int insertBatch(List<MemberBase> list);

	int updateByPrimaryKeySelective(MemberBase record);

	int updateByPrimaryKey(MemberBase record);

	@Select("select caccount from t_member_base where cemail = #{0} limit 1")
	String getUserName(String email);

	@Select("SELECT * FROM t_member_base WHERE caccount = #{0} limit 1")
	MemberBase getUserByAccount(String acc);

	List<MemberBase> getUserNames(List<String> list);

	@Select("select * from t_member_base where cemail = #{0} and iwebsiteid=#{1} limit 1")
	MemberBase getUserByEmail(String email, int siteid);

	@Update("update t_member_base set cpasswd=#{1} where cemail=#{0} and iwebsiteid = #{2}")
	int UpdatesMemberPassword(String email, String cpasswd, Integer websiteId);

	@Select("SELECT * FROM t_member_base WHERE bactivated=false AND cactivationcode=#{0} limit 1")
	MemberBase getNonActiveUserByActivationCode(String activationCode);

	// @Delete("delete from t_member_base where cemail = #{email} ")
	@Delete({ "<script> delete from t_member_base where cemail  in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>" })
	int deleteByEmail(List<String> email);

	@Select({ "<script> select cemail from t_member_base where cemail in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>" })
	List<String> getExistsEmails(List<String> emails);

	@Update("update t_member_base set caccount=#{nickname} where cemail=#{email}")
	int update(String email);

	List<MemberBase> getMemberMessage(@Param("email") String email,
			@Param("siteId") Integer siteId,
			@Param("caccount") String caccount,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum,
			@Param("blackUserStatus") Integer blackUserStatus);

	List<MemberBase> getMemberMessageForDate(@Param("email") String email,
			@Param("siteId") Integer siteId,
			@Param("caccount") String caccount,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum,
			@Param("blackUserStatus") Integer blackUserStatus,
			@Param("contry") String contry, @Param("vhost") String vhost,
			@Param("bactivated") Boolean bactivated,
			@Param("bnewsletter") Boolean bnewsletter,
			@Param("start") Date start, @Param("end") Date end);

	Integer getMemberCount(@Param("email") String email,
			@Param("siteId") Integer siteId,
			@Param("caccount") String caccount,
			@Param("blackUserStatus") Integer blackUserStatus,
			@Param("contry") String contry, @Param("vhost") String vhost,
			@Param("bactivated") Boolean bactivated,
			@Param("bnewsletter") Boolean bnewsletter,
			@Param("start") Date start, @Param("end") Date end);

	@Select("select count(iid) from t_member_base where dcreatedate between #{0} and #{1}")
	Integer getMemberCountByDate(Date start, Date end);

	@Select("select * from t_member_base where dcreatedate between #{startDate} and  #{endDate} limit #{pageSize} offset (#{pageSize} * (#{pageNum} - 1))")
	List<MemberBase> getAllMembers(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	/**
	 * 查询所有激活用户邮箱
	 */
	@Select("select cemail from t_member_base where iwebsiteid = 1 and bactivated=true and dcreatedate >=#{0} and dcreatedate <=#{1}")
	List<String> gerAllActivedMembers(Date start, Date end);

	@Update("update t_member_base set cpasswd=#{1} where cemail=(select cmemberemail from t_forget_passwd_base where cid=#{0}) and iwebsiteid = #{2}")
	int changePassword(String token, String newPassword, Integer websiteId);

	/**
	 * @author lijun
	 * @param id
	 * @param token
	 * @return
	 */
	@Update("update t_member_base set cuuid=#{1} where iid=#{0}")
	int updateUuidById(Integer id, String uuid);
	
	
	@Select("SELECT * FROM t_member_base WHERE cuuid = #{0} limit 1")
	public MemberBase getUserByUuid(String uuid);

	@Select("select * from t_member_base where iid = #{0}")
	MemberBase getMemberById(Integer id);
	
	
}