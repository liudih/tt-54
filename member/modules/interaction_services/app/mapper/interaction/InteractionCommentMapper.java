package mapper.interaction;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import valueobjects.interaction.ReviewCountAndScore;
import valueobjects.interaction.SimpleComment;
import dto.interaction.Foverallrating;
import dto.interaction.InteractionComment;

public interface InteractionCommentMapper {

	int insertSelective(InteractionComment record);

	@Select("SELECT iid,clistingid,csku, cmemberemail,istate,ccomment, foverallrating, iprice, iquality,"
			+ "ishipping,iusefulness, dcreatedate,cplatform,iwebsiteid, ctitle FROM t_interaction_comment where iid=#{0}")
	InteractionComment selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(InteractionComment record);

	@Select("SELECT iid, cmemberemail ,ccomment, foverallrating, iprice, iquality,"
			+ "ishipping,iusefulness, dcreatedate, ctitle FROM t_interaction_comment"
			+ " WHERE clistingid =#{0}"
			+ " AND istate = 1 order by dcreatedate desc limit 5 ")
	@ResultMap("BaseResultMap")
	List<InteractionComment> getInteractionCommentsByListingId(String listingId);

	@Select("select iid, cmemberemail ,ccomment,iprice, iquality, ishipping, iusefulness, foverallrating, dcreatedate, ctitle  "
			+ "from t_interaction_comment"
			+ " where  clistingid =#{0}"
			+ " and istate = 1  order by dcreatedate desc limit #{2}  offset  (#{1}-1)*#{2}")
	List<InteractionComment> getProductReviewsPage(String listingId,
			Integer pageNow, Integer pageSize);

	@Select("SELECT count(iid) FROM t_interaction_comment WHERE clistingid = #{0} and istate = 1")
	Integer getCountByListingId(String listingId);

	@Select("select round(cast(avg(round(foverallrating)) as numeric),1) from t_interaction_comment where clistingid= #{0}")
	Double getScoreByListingId(String listingId);

	List<ReviewCountAndScore> getScoreByListingIds(
			@Param("list") Set<String> listingId);

	@Select("SELECT count(iid) FROM t_interaction_comment WHERE cmemberemail = #{0} and iwebsiteid=#{1}")
	Integer getTotalReviewsCountByMemberEmailAndSiteId(String cmemberemail,
			Integer siteId);

	@Select("SELECT count(iid) FROM t_interaction_comment WHERE cmemberemail = #{0} and iwebsiteid=#{1} and istate = 0")
	Integer getPendingReviewsCountByMemberEmailAndSiteId(String email,
			Integer siteId);

	@Select("SELECT count(iid) FROM t_interaction_comment WHERE cmemberemail = #{0} and iwebsiteid=#{1}  and istate = 1")
	Integer getApprovedReviewsCountByMemberEmailAndSiteId(String email,
			Integer siteId);

	@Select("SELECT count(iid) FROM t_interaction_comment WHERE cmemberemail = #{0} and iwebsiteid=#{1} and istate = 2")
	Integer getFailedReviewsCountByMemberEmailAndSiteId(String email,
			Integer siteId);

	@Select("<script>"
			+ "select iid, clistingid, ccomment, iprice, iquality, ishipping, iusefulness, foverallrating, dcreatedate,istate, ctitle  "
			+ "from t_interaction_comment where cmemberemail = #{0} and iwebsiteid=#{6} "
			+ "<if test=\"status ==0 \"> and istate=0 </if>"
			+ "<if test=\"status ==1 \"> and istate=1 </if>"
			+ "<if test=\"status ==2 \"> and istate=2 </if> "
			+ "<if test=\"start !=null and end !=null \">  and dcreatedate between #{start} and #{end} </if> "
			+ " order by dcreatedate desc limit #{2} offset (#{1}-1)*#{2}"
			+ "</script>")
	List<InteractionComment> getMyReviewsPageByEmail(String email,
			Integer pageIndex, Integer pageSize,
			@Param("status") Integer status, @Param("start") Date start,
			@Param("end") Date end, Integer siteId);

	@Select("select iid, clistingid, ccomment, iprice, iquality, ishipping, iusefulness, foverallrating, dcreatedate, ctitle  "
			+ "from  t_interaction_comment where clistingid = #{0} and istate = 1 order by foverallrating")
	List<InteractionComment> getFoverallratingsByListingId(String listingId);

	@Insert("INSERT INTO t_interaction_comment (clistingid, csku, cmemberemail, ccomment, iprice, iquality, ishipping, iusefulness, "
			+ "foverallrating, dcreatedate, dauditdate, istate, ccountry, iwebsiteid ,iorderid, ctitle)"
			+ "VALUES (#{clistingid}, #{csku}, #{cmemberemail}, #{ccomment}, #{iprice}, #{iquality}, #{ishipping}, "
			+ "#{iusefulness}, #{foverallrating}, #{dcreatedate}, #{dauditdate}, #{istate}, #{ccountry}, #{iwebsiteid} , #{iorderid},  #{ctitle})")
	Integer insertComment(InteractionComment comment);

	@Select("select * "
			+ "from t_interaction_comment where csku = #{0} and cmemberemail= #{1} and ccomment= #{2}")
	InteractionComment getCommentByParams(String sku, String email,
			String comment);

	@Select("select clistingid, round(foverallrating) as type, count(iid) as num "
			+ "from  t_interaction_comment where clistingid = #{listingId} and istate = 1 "
			+ "group by clistingid, type order by type desc;")
	List<Foverallrating> countRatings(String listingId);

	@Select("<script>"
			+ "select iid,clistingid, csku, cmemberemail, ccomment, iprice, iquality, ishipping, iusefulness, "
			+ "foverallrating, dcreatedate, dauditdate, istate, ccountry,cplatform , iwebsiteid, ctitle from t_interaction_comment where "
			+ " iwebsiteid=#{siteId} and dcreatedate between #{startDate} and #{endDate} "
			+ "<if test=\"status != null and status !=3 \"> and istate=#{status} </if>"
			+ "<if test=\"sku !=null and sku != '' \"> and csku like '%${sku}%'  </if>"
			+ "<if test=\"email !=null and email != '' \"> and cmemberemail like '%${email}%' </if>"
			+ "<if test=\"content !=null and content != '' \"> and ccomment like '%${content}%' </if>"
			+ " order by dcreatedate desc limit #{1} offset (#{0}-1)*#{1}"
			+ "</script>")
	List<InteractionComment> getReviewsByPageAndListingIdAndStatus(
			Integer page, Integer pageSize, @Param("sku") String sku,
			@Param("status") Integer status, @Param("email") String email,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("content") String content, @Param("siteId") Integer siteId);

	@Select("<script>"
			+ "select count(iid) from t_interaction_comment "
			+ "where iwebsiteid=#{siteId} and dcreatedate between #{startDate} and #{endDate} "
			+ "<if test=\"status != null and status !=3 \"> and istate=#{status} </if>"
			+ "<if test=\"sku !=null and sku != '' \"> and csku like '%${sku}%'  </if>"
			+ "<if test=\"email !=null and email != '' \"> and cmemberemail like '%${email}%' </if> "
			+ "<if test=\"content !=null and content != '' \"> and ccomment like '%${content}%' </if>"
			+ "</script>")
	Integer getReviewsCount(@Param("sku") String sku,
			@Param("status") Integer status, @Param("email") String email,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("content") String content, @Param("siteId") Integer siteId);

	int batchVerify(@Param("list") List<Integer> list,
			@Param("status") Integer status);

	@Select({
			"<script>",
			"select * from t_interaction_comment ",
			" where (csku, cmemberemail) in ",
			" <foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >(#{item.sku}, #{item.email})</foreach> ",
			"</script>" })
	List<InteractionComment> checkSkuCommentExits(
			List<SimpleComment> simpleComments);

	@Select({
			"<script>",
			"select * from t_interaction_comment ",
			"where iid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	List<InteractionComment> getReviewsByIds(@Param("list") List<Integer> idList);

	List<InteractionComment> getCommentHelpCount();

	List<InteractionComment> getCommentHelpCountByCliId(String clistingid);

	List<InteractionComment> getCommentUsAudit();

	int updateStateByIds(List<Integer> list);

	@Select("select max(iid+1) from t_interaction_comment")
	Integer getReviewMaxId();
}