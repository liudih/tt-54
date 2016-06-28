package mapper.activitydb.page;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import values.activity.page.PageItemCount;
import values.activity.page.VoteRecordQuery;
import entity.activity.page.VoteRecord;

public interface VoteRecordMapper {
	@Delete({ "delete from t_vote_record",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int deleteByPrimaryKey(Integer iid);

	@Insert({
			"insert into t_vote_record (cmemberemail, ",
			"ipageitemid, iwebsiteid, ",
			"cvhost, dvotedate)",
			"values (#{cmemberemail,jdbcType=VARCHAR}, ",
			"#{ipageitemid,jdbcType=INTEGER}, #{iwebsiteid,jdbcType=INTEGER}, ",
			"#{cvhost,jdbcType=VARCHAR}, #{dvotedate,jdbcType=TIMESTAMP})" })
	int insert(VoteRecord record);

	@Select({ "select",
			"iid, cmemberemail, ipageitemid, iwebsiteid, cvhost, dvotedate",
			"from t_vote_record", "where iid = #{iid,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "cmemberemail", property = "cmemberemail", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ipageitemid", property = "ipageitemid", jdbcType = JdbcType.INTEGER),
			@Result(column = "iwebsiteid", property = "iwebsiteid", jdbcType = JdbcType.INTEGER),
			@Result(column = "cvhost", property = "cvhost", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dvotedate", property = "dvotedate", jdbcType = JdbcType.TIMESTAMP) })
	VoteRecord selectByPrimaryKey(Integer iid);

	@Update({ "update t_vote_record",
			"set cmemberemail = #{cmemberemail,jdbcType=VARCHAR},",
			"ipageitemid = #{ipageitemid,jdbcType=INTEGER},",
			"iwebsiteid = #{iwebsiteid,jdbcType=INTEGER},",
			"cvhost = #{cvhost,jdbcType=VARCHAR},",
			"dvotedate = #{dvotedate,jdbcType=TIMESTAMP}",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int updateByPrimaryKey(VoteRecord record);

	@Select({ "select",
		"iid, cmemberemail, ipageitemid, iwebsiteid, cvhost, dvotedate",
		"from t_vote_record" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "cmemberemail", property = "cmemberemail", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ipageitemid", property = "ipageitemid", jdbcType = JdbcType.INTEGER),
			@Result(column = "iwebsiteid", property = "iwebsiteid", jdbcType = JdbcType.INTEGER),
			@Result(column = "cvhost", property = "cvhost", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dvotedate", property = "dvotedate", jdbcType = JdbcType.TIMESTAMP) })
	List<VoteRecord> selectAll();
	
	@Select("select count(iid) from t_vote_record where ipageitemid=#{0} and iwebsiteid=#{1} ")
	int getPageItemCount(int itemid, int websiteid);
	
	@Select(" select count(a.iid) from t_vote_record a "
			+ " inner join t_page_item b on b.iid=a.ipageitemid "
			+ " where a.cmemberemail=#{0} and  b.ipageid=#{1} and a.iwebsiteid=#{2}  ")
	int getUserPageItemCount(String email, int pageid, int websiteid);

	@Select("select a.ipageid pageId,a.iid pageItemId, count(a.iid) itemCount from t_page_item a "
			+ " left join t_vote_record b on a.iid=b.ipageitemid where a.ipageid=#{0} and b.iwebsiteid=#{1} group by a.iid")
	List<PageItemCount> getPageAllItemCount(int pageId, int websiteid);

	/**
	 * 
	 * @Title: getPageItemCountToday
	 * @Description: TODO(查询投票记录通过站点、用户名、投票项、当前日期、明天日期)
	 * @param @param websiteId
	 * @param @param memberID
	 * @param @param activityPageItemId
	 * @param @param date
	 * @param @param tomorrowDate
	 * @param @return
	 * @return int
	 * @throws 
	 * @author yinfei
	 */
	@Select("select count(iid) from t_vote_record where iwebsiteid = #{0} and cmemberemail = #{1} and ipageitemid = #{2} "
			+ "and dvotedate > #{3} and dvotedate < #{4}")
	int getPageItemCountToday(int websiteId, String memberID,
			int activityPageItemId, Date date, Date tomorrowDate);
	
	@Select("<script>"
			+ "SELECT COUNT(DISTINCT ipageitemid) FROM t_vote_record  where 1=1 "
			+ "<if test=\"ipageitemid != null  \">and ipageitemid=#{ipageitemid} </if>"
			+ "<if test=\"startDate != null and endDate!=null\">and dvotedate  BETWEEN #{startDate} AND #{endDate} </if>  "
			+ "</script>")
	int getCount(@Param("ipageitemid") Integer ipageitemid,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	@Select("<script>"
			+ "select ipageitemid,count(ipageitemid) as votenumber from t_vote_record where 1=1 "
			+ "<if test=\"ipageitemid != null  \">and ipageitemid=#{ipageitemid} </if>"
			+ "<if test=\"startDate != null and endDate!=null\">and dvotedate  BETWEEN #{startDate} AND #{endDate} </if> GROUP BY ipageitemid  limit #{pageSize} offset #{pageSize} * (#{pageNum} - 1)"
			+ "</script>")
	List<VoteRecordQuery> getVoteRecordByPageItemNameAndDate(
			@Param("ipageitemid") Integer ipageitemid,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);
	

	@Select("<script>"
			+ "SELECT COUNT(DISTINCT cmemberemail) FROM t_vote_record  where 1=1 "
			+ "<if test=\"ipageitemid != null  \">and ipageitemid=#{ipageitemid} </if>"
			+ "<if test=\"startDate != null and endDate!=null\">and dvotedate  BETWEEN #{startDate} AND #{endDate} </if>  "
			+ "</script>")
	int getUserCount(@Param("ipageitemid") Integer ipageitemid,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	@Select("<script>"
			+ "select cmemberemail,count(cmemberemail) as votenumber from t_vote_record where 1=1 "
			+ "<if test=\"ipageitemid != null  \">and ipageitemid=#{ipageitemid} </if>"
			+ "<if test=\"startDate != null and endDate!=null\">and dvotedate  BETWEEN #{startDate} AND #{endDate} </if> GROUP BY cmemberemail limit #{pageSize} offset #{pageSize} * (#{pageNum} - 1)"
			+ "</script>")
	List<VoteRecordQuery> getVoteRecordUserByPageItemNameAndDate(
			@Param("ipageitemid") Integer ipageitemid,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

}