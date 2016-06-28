package mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.member.BlackUser;
import dto.member.MemberBase;

/**
 * 黑名单用户的接口
 * 
 * @author guozy
 *
 */
public interface BlackUserMapper {

	@Insert("INSERT INTO t_blacklist_user(iwebsiteid,creason,dcreatedate,cemail,istatus)  "
			+ "VALUES(#{iwebsiteid},#{creason},#{dcreatedate},#{cemail},#{istatus})")
	int insertBlackUser(BlackUser blackUser);

	@Update("Update t_blacklist_user  "
			+ "SET istatus=#{istatus},creason=#{creason},dcreatedate=#{dcreatedate},iwebsiteid=#{iwebsiteid} "
			+ "WHERE cemail=#{cemail}")
	int updateBlackUser(BlackUser blackUser);

	@Update("Update t_blacklist_user  SET istatus=1 WHERE cemail=#{0}")
	int removeBlackUser(String cemail);

	@Select({
			"<script>",
			"select t.iid,t.creason,t.cemail,t.dcreatedate,t.istatus,t.iwebsiteid from t_blacklist_user t ",
			"where  t.iwebsiteid=#{iwebsiteId} "
					+ "<if test='list!=null'>and cemail in "
					+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach>"
					+ "</if></script>" })
	List<BlackUser> getBlackUser(@Param("iwebsiteId") Integer iwebsiteId,
			@Param("list") List<String> cemail);

	@Select("select * from t_blacklist_user where cemail=#{0}")
	BlackUser getBlackUserEmail(String cemail);
	
}
