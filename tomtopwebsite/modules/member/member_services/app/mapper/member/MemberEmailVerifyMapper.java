package mapper.member;

import org.apache.ibatis.annotations.Select;

import dto.member.MemberEmailVerify;

public interface MemberEmailVerifyMapper {

	int deleteByPrimaryKey(Integer iid);

	int insert(MemberEmailVerify record);

	int insertSelective(MemberEmailVerify record);

	MemberEmailVerify selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(MemberEmailVerify record);

	int updateByPrimaryKey(MemberEmailVerify record);

	@Select("select iid, cmark,cemail, bisending,cactivationcode,idaynumber, "
			+ "idaynumber,dvaliddate,dsenddate, dcreatedate,iresendcount "
			+ " from t_member_email_verify where cemail=#{0} order by iid desc limit 1")
	MemberEmailVerify getByEmail(String email);
	
	@Select("select iid, cmark,cemail, bisending,cactivationcode,idaynumber, "
			+ "idaynumber,dvaliddate,dsenddate, dcreatedate,iresendcount "
			+ " from t_member_email_verify where cactivationcode=#{0} limit 1")
	MemberEmailVerify getActivationCode(String activationcode);
	
	int deleteByEmail(String email);
}
