package mapper.loyalty;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import valueobjects.loyalty.UsedPoint;
import entity.loyalty.MemberIntegralHistory;
import entity.loyalty.OrderPoints;
import entity.loyalty.MemberSign;
import entity.loyalty.MemberSignRule;

public interface MemberIntegralHistoryMapper {

	@Insert("<script>INSERT INTO t_member_integral_history(iwebsiteid,cemail,cdotype,iintegral,cremark<if test=\"istatus != null\">,istatus</if>, csource) "
			+ "VALUES(#{iwebsiteid},#{cemail},#{cdotype},#{iintegral},#{cremark}<if test=\"istatus != null\">,#{istatus}</if>, #{csource})</script>")
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insert(MemberIntegralHistory memberIntegralHistory);

	@Select("select sum(iintegral) from t_member_integral_history where cemail = #{0} and iwebsiteid = #{1} and istatus = 1")
	Integer getSumByEmail(String email, Integer siteId);

	@Select("select iintegral from t_member_integral_history where iid = #{0}")
	Integer getById(Integer id);

	@Select("select sum(iintegral) from t_member_integral_history where cemail = #{0} and cdotype = #{1} and iwebsiteid = #{2}")
	Integer getByEmailAndBehaviorName(String email, String name, Integer siteId);

	@Update("update t_member_integral_history set cdotype = #{0} where iid = #{1}")
	Integer updateStatus(String status, Integer pointsId);

	@Insert("<script>INSERT INTO t_member_integral_history(iwebsiteid,cemail,cdotype,iintegral,cremark) VALUES"
			+ "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">  "
			+ "(#{item.iwebsiteid},#{item.cemail},#{item.cdotype},#{item.iintegral},#{item.cremark}) </foreach></script>")
	int batchInsert(List<MemberIntegralHistory> list);

	@Select({ "<script> select iid from t_member_integral_history where iid in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>" })
	List<Integer> getIds(List<Integer> pointIds);

	@Select("<script>select * from t_member_integral_history where "
			+ "<if test=\"email != null\">cemail = #{email} and </if>"
			+ "<if test=\"siteId != null\">iwebsiteid = #{siteId} and </if>"
			+ "<if test=\"cdotype != null\">cdotype = #{cdotype} and </if>"
			+ "<if test=\"cremark != null\">cremark like '%${cremark}%' and </if>"
			+ "<if test=\"status != null\">istatus = #{status} and </if>"
			+ "iintegral IS NOT NULL "
			+ "order by iid desc limit #{pageSize} offset (#{pageSize} * (#{pageNum} - 1))</script>")
	List<MemberIntegralHistory> getMemberIntegralHistoryList(
			@Param("email") String email, @Param("siteId") Integer siteId,
			@Param("cdotype") String cdotype, @Param("cremark") String cremark,
			@Param("status") Integer status,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

	@Select("<script>select count(iid) from t_member_integral_history where "
			+ "<if test=\"email != null\">cemail = #{email} and </if>"
			+ "<if test=\"siteId != null\">iwebsiteid = #{siteId} and </if>"
			+ "<if test=\"cdotype != null\">cdotype = #{cdotype} and </if>"
			+ "<if test=\"status != null\">istatus = #{status} and </if>"
			+ "iintegral IS NOT NULL </script>")
	Integer getMemberIntegralHistoryCount(@Param("email") String email,
			@Param("siteId") Integer siteId, @Param("cdotype") String cdotype,
			@Param("status") Integer istatus,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

	@Update("update t_member_integral_history set cdotype = #{cdotype}, iintegral = #{iintegral}, cremark = #{cremark}, istatus = #{istatus} where iid = #{iid}")
	int updateMemberIntegralHistory(@Param("iid") Integer iid,
			@Param("cdotype") String cdotype,
			@Param("iintegral") Double iintegral,
			@Param("cremark") String cremark, @Param("istatus") Integer istatus);

	@Update("update t_member_integral_history set cremark = #{0} where iid = #{1}")
	int updateRemark(String remark, Integer id);
	

	@Select("select * from t_member_integral_history where iwebsiteid = #{siteId} and cemail=#{email} and istatus=#{status} and cdotype != #{doType} order by dcreatedate desc limit #{pageSize} offset (#{pageSize} * (#{pageNum} - 1))")
	List<MemberIntegralHistory> getIntegralHistoriesByEmail(
			@Param("siteId") Integer siteId, 
			@Param("email")String email, 
			@Param("status")Integer status, 
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum,
			@Param("doType")String doType);
		
	
	@Select("select op.dusedate as useDate, op.cemail as email, op.istatus as status, op.iorderid as orderid, op.fparvalue as parvalue, "
			+ " mih.iintegral as point, mih.csource as source from t_order_points op left join t_member_integral_history mih on op.ipointsid = mih.iid "
			+ " where mih.iwebsiteid = #{0} and op.cemail=#{1} and op.istatus=#{2}")
	List<UsedPoint> getOrderPointsByEmail(Integer siteId, String email, Integer status);
	
	@Select("SELECT count(iid) FROM t_member_integral_history WHERE iwebsiteid = #{0} and cemail = #{1}")
	Integer getTotalIntegralCountByEmail(Integer siteId, String cmemberemail);

	@Select("SELECT count(iid)  from t_member_integral_history  where iwebsiteid = #{0} and cemail=#{1} and cdotype != #{2} and iintegral < 0")
	Integer getTotalUsedCountByEmail(Integer siteId, String cmemberemail, String doType);
	
	@Select("select op.dusedate as useDate, op.cemail as email, op.istatus as status, op.iorderid as orderid, op.fparvalue as parvalue, "
			+ " mih.iintegral as point, mih.csource as source from t_order_points op left join t_member_integral_history mih on op.ipointsid = mih.iid "
			+ " where mih.iwebsiteid = #{siteId} and op.cemail=#{email} and op.istatus=#{status} order by mih.dcreatedate desc limit #{pageSize} offset (#{pageSize} * (#{pageNum} - 1))")
	List<UsedPoint> getUsedPointsByEmail(
			@Param("siteId") Integer siteId, 
			@Param("email")String email, 
			@Param("status")Integer status, 
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);
	
	@Select("SELECT *  from t_member_integral_history  where iwebsiteid = #{siteId} and cemail=#{email} and cdotype != #{doType} and iintegral < 0 order by dcreatedate desc limit #{pageSize} offset (#{pageSize} * (#{pageNum} - 1))")
	List<MemberIntegralHistory> getUsedPointHistory(
			@Param("siteId") Integer siteId, 
			@Param("email")String email, 
			@Param("doType")String doType, 
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);
	
	@Select("SELECT *  from t_member_sign where cemail=#{0} and iwebsiteid = #{1}")
	MemberSign getMemberSign(String cmemberemail, Integer siteId);
	
	@Insert("INSERT INTO t_member_sign (iwebsiteid, cemail, isigncount, dlastsigndate) "
			+ "VALUES (#{iwebsiteid}, #{cemail}, #{isigncount}, #{dlastsigndate})")
	int saveMemberSign(MemberSign memberSign);
	
	@Select("SELECT *  from t_member_sign_rule")
	List<MemberSignRule> getMemberSignRule();
	
	@Update("update t_member_sign set dlastsigndate = #{lastSignDate}, isigncount = #{signCount} where iwebsiteid = #{siteId} and cemail = #{email}")
	Integer updateMemberSign(
			@Param("siteId") Integer siteId,
			@Param("email") String email, 
			@Param("lastSignDate") Date lastSignDate,
			@Param("signCount") Integer signCount
	);		
	
	@Select("SELECT * FROM t_member_integral_history WHERE iwebsiteid = #{0} and cemail = #{1} and cdotype=#{2}")
	MemberIntegralHistory checkSubscriberPoint(Integer siteId, String cmemberemail, String doType);

	@Select("SELECT sum(iintegral) from t_member_integral_history  where iwebsiteid = #{0} and cemail=#{1} and cdotype!=#{2} and iintegral < 0")
	Integer getTotalUsePointByEmail(Integer siteId, String cmemberemail, String doType);
	
	/**
	 * @author lijun
	 * @param paras
	 * @return
	 */
	List<MemberIntegralHistory> getValidPointsByEmail(Map paras);
	
	/**
	 * @author lijun
	 * @param paras
	 * @return
	 */
	int getTotal(Map paras);
	
	
	/**
	 * 验证此订单是否已送出积分
	 */
	@Select("SELECT count(iid) FROM t_member_integral_history WHERE iwebsiteid = #{0} and cemail = #{1} and cdotype=#{2} and cremark =#{3} ")
	int validateIsSendPoint(Integer siteId, String cmemberemail, String doType,
			String orderNumber);

	/**
	 * 验证用户是否已经签到，如果签到已经送出了积分，则不再送出
	 */
	@Select("SELECT count(iid) FROM t_member_integral_history WHERE iwebsiteid = #{0} and cemail = #{1} and cdotype=#{2} and cremark =#{3} and date(dcreatedate) = date(now()) ")
	int validateSignInSendPoint(Integer siteId, String cmemberemail,
			String doType, String remark);
}
