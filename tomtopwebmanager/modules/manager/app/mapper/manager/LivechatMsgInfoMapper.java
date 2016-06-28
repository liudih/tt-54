package mapper.manager;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.LivechatSessionMsgStatistics;
import valueobjects.manager.LivechatSessionStatistics;
import valueobjects.manager.search.HistoryMsgContext;
import entity.manager.LivechatMsgInfo;

public interface LivechatMsgInfoMapper {
	@Insert("insert into t_livechat_msg_info (csessionid, cfromltc, ctoltc, cfromalias, ctoalias, ccontent, ctopic) "
			+ "values (#{csessionid}, #{cfromltc}, #{ctoltc}, #{cfromalias}, #{ctoalias}, #{ccontent}, #{ctopic})")
	int insert(LivechatMsgInfo info);

	List<LivechatMsgInfo> searchHistoryMsgPage(HistoryMsgContext context);

	int searchHistoryMsgCount(HistoryMsgContext context);

	@Select("<script> select a.cfromalias as userName,count(a.cfromalias) as sessoinCount from"
			+ " (select cfromalias,csessionid from t_livechat_msg_info where dcreatedate>#{0} and cfromalias in "
			+ " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ " group by cfromalias,csessionid)a "
			+ " group by a.cfromalias </script>")
	List<LivechatSessionStatistics> getUserSessionCount(Date fromDate,
			@Param("list") List<String> users);

	@Select("<script> select count(csessionid) as sessionCount,cast(date_trunc('second',sum(mindate)) as varchar) as sessionTime,cusername as userName,latitude from( "
			+ " select m.csessionid,a.cusername,age(max(m.dcreatedate),min(m.dcreatedate)) mindate,to_char(m.dcreatedate,#{2}) as latitude "
			+ " from t_livechat_msg_info m "
			+ " inner join t_admin_user a on m.cfromalias=a.cusername or m.ctoalias=a.cusername "
			+ " where to_char(m.dcreatedate,#{2}) between #{0} and #{1}  "
			+ " <if test=\"userName != null and userName !=''\" > "
			+ " and #{userName}=a.cusername "
			+ " </if>"
			+ " group by m.csessionid,a.cusername,latitude)ta group by ta.cusername,latitude order by ta.cusername </script>")
	List<LivechatSessionMsgStatistics> getSessionStatistics(Date beginDate,
			Date endDate, String dateFormat, @Param("userName") String username);

}
