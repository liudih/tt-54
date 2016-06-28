package mapper.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.LivechatLeaveMsgStatistics;
import valueobjects.manager.LeaveMsgCount;
import valueobjects.manager.LivechatSessionStatistics;
import entity.manager.LeaveMsgInfo;

public interface LeaveMsgInfoMapper {

	@Insert("insert into t_livechat_leave_msg (cltc, ccontent, cemail, cip, ctopic,ilanguageid, calias,dhandledate,ipretreatmentid) "
			+ "values (#{cltc}, #{ccontent}, #{cemail}, #{cip}, #{ctopic}, #{ilanguageid}, #{calias},#{dhandledate},#{ipretreatmentid})")
	int insert(LeaveMsgInfo info);

	@Select("select * from t_livechat_leave_msg order by bishandle,dcreatedate desc limit #{1} offset (#{0} - 1) * #{1}")
	List<LeaveMsgInfo> getPage(int page, int pageSize);

	@Select("select count(iid) from t_livechat_leave_msg")
	int getCount();

	List<LeaveMsgInfo> searchLeaveMsgInfoPage(Map<String, Object> param);

	Integer searchLeaveMsgInfoCount(Map<String, Object> param);

	@Update("update t_livechat_leave_msg set dhandledate=now(), chandler = #{handler}, bishandle=true where iid=#{iid}")
	int leaveMsgInfoHandle(@Param("handler") String handler,
			@Param("iid") Integer iid);

	@Select("select count(iid) from t_livechat_leave_msg where iid = #{iid} and bishandle=false")
	int getHandleResult(@Param("iid") Integer iid);

	@Select(" select * from t_livechat_leave_msg where iid=#{0} ")
	LeaveMsgInfo getById(Integer id);

	@Update("update t_livechat_leave_msg set ireplyuserid=#{0},creplycontent=#{1},dreplydate=now() where iid=#{2}")
	int updateById(int replyuserid, String replycontent, int id);

	@Select("<script>"
			+ " select ipretreatmentid as userId,count(ipretreatmentid) msgCount "
			+ " from t_livechat_leave_msg where bishandle=#{0} and ipretreatmentid in"
			+ " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ " group by ipretreatmentid " + "</script>")
	List<LeaveMsgCount> getLeaveMsgInfoCount(boolean handled,
			@Param("list") List<Integer> users);

	@Select("<script> select chandler as userName,count(chandler) as leaveCount,to_char(dhandledate,#{2}) as latitude "
			+ " from t_livechat_leave_msg where chandler is not null "
			+ " and to_char(dhandledate,#{2}) between #{0} and #{1} "
			+ "<if test=\"userName != null and userName !=''\" > "
			+ " and #{userName}=chandler "
			+ " </if> "
			+ " group by chandler,latitude </script>")
	List<LivechatLeaveMsgStatistics> getLeaveStatistics(Date beginDate,
			Date endDate, String dateFormat, @Param("userName") String username);

}
