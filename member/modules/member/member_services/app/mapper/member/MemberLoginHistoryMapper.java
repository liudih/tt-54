package mapper.member;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.member.MemberLoginHistory;

public interface MemberLoginHistoryMapper {

	@Insert("INSERT INTO t_member_login_history "
			+ "(dtimestamp,cemail,iwebsiteid,cltc,cstc,cclientip) "
			+ "VALUES "
			+ "(#{dtimestamp},#{cemail},#{iwebsiteid},#{cltc},#{cstc},#{cclientip})")
	int insert(MemberLoginHistory history);

	@Select("SELECT * FROM t_member_login_history "
			+ "WHERE iwebsiteid = #{0} AND cemail = #{1} "
			+ "AND dtimestamp BETWEEN #{2} AND #{3} "
			+ "ORDER BY dtimestamp DESC")
	List<MemberLoginHistory> findByDateRange(int siteID, String email,
			Date from, Date to);

}
