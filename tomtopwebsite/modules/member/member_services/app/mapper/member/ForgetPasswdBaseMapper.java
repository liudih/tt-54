package mapper.member;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.member.ForgetPasswdBase;

public interface ForgetPasswdBaseMapper {

	int deleteByPrimaryKey(Integer cid);

	@Insert("insert into t_forget_passwd_base(cid,cmemberemail,iwebsiteid,crandomcode)  values (#{cid},#{cmemberemail},#{iwebsiteid},#{crandomcode})")
	int insert(ForgetPasswdBase record);

	int insertSelective(ForgetPasswdBase record);

	ForgetPasswdBase selectByPrimaryKey(Integer cid);

	int updateByPrimaryKeySelective(ForgetPasswdBase record);

	int updateByPrimaryKey(ForgetPasswdBase record);

	@Select("select * from t_forget_passwd_base where cmemberemail=#{0} and iwebsiteid = #{1} order by cid desc limit 1")
	ForgetPasswdBase getForgetPasswdBaseByCmembermail(String cmemberemail, Integer iwebsiteId);

	@Update("update t_forget_passwd_base set buse=#{0} where  cid = #{1}")
	int update(Boolean buse, String cid);

	@Select("select count(*) from t_forget_passwd_base where cmemberemail=#{0} "
			+ "and dcreatedate >=#{1} and dcreatedate < #{2} and iwebsiteid = #{3}")
	int getCountByCmembermailAndDcreatedate(String email, Date limit, Date end, Integer iwebsiteId);

	@Select("select cmemberemail from t_forget_passwd_base where cid=#{0} order by cid desc limit 1")
	String getEmailByCid(String cid);

	@Select("select dcreatedate from t_forget_passwd_base where cid=#{0} order by dcreatedate desc limit 1")
	Date getDcreatedateByCid(String cid);

	@Select("select buse from t_forget_passwd_base where cid=#{0} and dcreatedate=(select dcreatedate from t_forget_passwd_base where cmemberemail=#{1} order by dcreatedate desc limit 1)")
	Boolean getBuseByCidAndEmail(String cid, String email);

	@Select("select * from t_forget_passwd_base where cmemberemail=#{0} and crandomcode=#{1} and buse=#{2} ")
	ForgetPasswdBase getForgetPwdByCode(String email, String code, boolean buse);

	@Delete("delete from t_forget_passwd_base where cmemberemail=#{0} and iwebsiteid = #{1}")
	int deleteByEmail(String email, Integer websiteId);
	
	/**
	 * 检查token是否失效
	 * @author lijun
	 * @param token
	 * @return
	 */
	@Select("select buse from t_forget_passwd_base where cid=#{0} ")
	boolean isFail(String token);

}
