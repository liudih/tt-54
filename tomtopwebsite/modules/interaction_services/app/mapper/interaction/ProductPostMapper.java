package mapper.interaction;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.interaction.ProductPost;
import dto.interaction.ProductPostWithHeplQty;

public interface ProductPostMapper {
	/**
	 * 已审核（通过）
	 */
	static final int CHECKED = 1;
	/**
	 * 未审核
	 */
	static final int UNCHECKED = 0;
	/**
	 * 审核不通过
	 */
	static final int REFUSE = -1;

	int deleteByPrimaryKey(Integer iid);

	int insert(ProductPost record);

	int insertSelective(ProductPost record);

	ProductPost selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(ProductPost record);

	int updateByPrimaryKey(ProductPost record);

	@Select("select iid, ctitle,itypeid, cquestion, canswer, cmemberemail, dcreatedate, crecoveryuser, drecoverydate "
			+ "from t_interaction_product_post where clistingid = #{0} and istate = #{2} order by dcreatedate desc limit #{1}")
	List<ProductPost> getByListingId(String listingId, Integer limit,
			Integer state);

	@Select("select count(iid) from t_interaction_product_post "
			+ "where clistingid = #{0} and istate = #{1} and ilanguageid=#{2} and iwebsiteid=#{3} and itypeid =#{4}")
	int getByListingIdCount(String listingId, Integer state,
			Integer languageId, Integer siteId, Integer typeId);

	@Select("select count(iid) from t_interaction_product_post where iid = #{0} and istate = #{1}")
	int validateProductPostExist(Integer ipostid, Integer state);

	List<ProductPostWithHeplQty> getByParamMapPage(Map<String, Object> paramMap);

	List<ProductPost> getByListingId(String listing, int status);

	@Select("select q.iid, q.clistingid, q.itypeid, q.csku, q.ctitle, q.cquestion, q.canswer, q.cmemberemail,q.dcreatedate,  q.crecoveryuser, q.drecoverydate, q.istate,"
			+ " h.iid, h.ipostid, h.ihelpfulqty,h.inothelpfulqty from t_interaction_product_post q left join t_interaction_product_post_help_qty h on q.iid=h.ipostid "
			+ " where q.clistingid=#{0} and q.istate=#{1}   order by q.dcreatedate desc limit #{2}  ")
	List<ProductPostWithHeplQty> getByListingIdLimit(String listingid,
			int stgatus, int limit);

	int getCountByparamMap(Map<String, Object> paramMap);

	@Select("select q.iid, q.clistingid, q.itypeid, q.csku, q.ctitle, q.cquestion, q.canswer, q.cmemberemail,q.dcreatedate,  q.crecoveryuser, q.drecoverydate, q.istate,"
			+ "q.breply, h.iid, h.ipostid, h.ihelpfulqty,h.inothelpfulqty from t_interaction_product_post q left join t_interaction_product_post_help_qty h on q.iid=h.ipostid "
			+ " where q.iid=#{0} ")
	ProductPostWithHeplQty getProductPostById(int id);

	@Select("select count(iid) from t_interaction_product_post where cmemberemail = #{0}")
	int getTotalRecordByEamil(String email);

	@Select("select ctitle,min(dcreatedate) createdate  from t_interaction_product_post "
			+ "where clistingid = #{0}  and iwebsiteid= #{1} and ilanguageid =#{2} and istate =#{3} and itypeid=#{4} "
			+ " group by ctitle order by createdate desc limit #{6} offset (#{5}-1)*#{6}")
	List<String> getQuestionsTitleByListingId(String listingId, Integer siteId,
			Integer lanugageId, Integer state, Integer itypeid, Integer page,
			Integer pageSize);

	@Select("select count(*) from (select ctitle from t_interaction_product_post "
			+ "where clistingid = #{0}  and iwebsiteid= #{1} and ilanguageid =#{2} and istate =#{3} and itypeid=#{4} "
			+ " group by ctitle ) tmp")
	Integer getTotalQuestionsByListingId(String listingId, Integer siteId,
			Integer lanugageId, Integer state, Integer itypeid);

	@Select("select iid , ctitle, itypeid, cquestion, canswer, cmemberemail, dcreatedate, crecoveryuser, drecoverydate from t_interaction_product_post "
			+ "where  clistingid =#{0} and ctitle=#{1} and iwebsiteid= #{2} and ilanguageid =#{3} and istate =#{4} and itypeid=#{5} order by dcreatedate ")
	List<ProductPost> getProductPostByListingIdAndTitle(String listingId,
			String ctitle, Integer siteId, Integer lanugageId, Integer state,
			Integer itypeid);

	@Select("select * from t_interaction_product_post "
			+ "where iwebsiteid= #{0} and ilanguageid =#{1} and istate =#{2} and itypeid=#{3} "
			+ " order by dcreatedate desc limit #{5} offset (#{4}-1)*#{5}")
	List<ProductPost> getQuestions(Integer siteId, Integer lanugageId,
			Integer state, Integer itypeid, Integer page, Integer pageSize);

	@Select("select iid , ctitle, itypeid, cquestion, canswer, cmemberemail, dcreatedate, crecoveryuser, drecoverydate "
			+ "from t_interaction_product_post "
			+ "where (lower(csku) like '%${key}%' or lower(ctitle) like '%${key}%' or lower(cquestion) like '%${key}%' or lower(canswer) like '%${key}%') "
			+ "and iwebsiteid = #{siteId} and ilanguageid = #{lanugageId} and istate = #{state} and itypeid = #{itypeid} order by dcreatedate "
			+ "limit #{pageSize} offset (#{page}-1) * #{pageSize}")
	List<ProductPost> getProductPostListBySearch(@Param("key") String key,
			@Param("siteId") Integer siteId,
			@Param("lanugageId") Integer lanugageId,
			@Param("state") Integer state, @Param("itypeid") Integer itypeid,
			@Param("page") Integer page, @Param("pageSize") Integer pageSize);

	@Select("select count(*) from t_interaction_product_post "
			+ "where iwebsiteid= #{0} and ilanguageid =#{1} and istate =#{2} and itypeid=#{3} ")
	Integer getTotalBySearch(Integer siteId, Integer lanugageId, Integer state,
			Integer itypeid);
}