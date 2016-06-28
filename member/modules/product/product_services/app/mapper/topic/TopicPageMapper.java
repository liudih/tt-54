package mapper.topic;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.topic.TopicPage;

public interface TopicPageMapper {

	@Select("select distinct ilanguageid from t_product_topic_page where iwebsiteid = #{0}")
	List<Integer> getLanguageIdsBySiteId(Integer siteId);

	@Select("select distinct ctype from t_product_topic_page where iwebsiteid = #{0}")
	List<String> getTypeBySiteId(Integer siteId);

	@Delete("delete from t_product_topic_page where iid = #{0}")
	Integer deleteById(Integer siteId);

	@Select("select distinct date_part('year',dcreatedate) as year from t_product_topic_page where "
			+ "iwebsiteid = #{0} order by year desc")
	List<Integer> getYearBySiteId(Integer siteId);

	@Select("<script>select iid, iwebsiteid, ilanguageid, ctype, ctitle, chtmlurl, bshow, "
			+ "icreateuserid, dcreatedate from t_product_topic_page where iwebsiteid in "
			+ "<foreach item=\"item\" collection=\"siteIds\" open=\"(\" separator=\",\" close=\")\">"
			+ "#{item}</foreach> order by iwebsiteid, ctype, ilanguageid, dcreatedate desc "
			+ "limit #{size} offset (#{size} * (#{page} - 1))</script>")
	List<TopicPage> getAll(@Param("siteIds") List<Integer> siteIds,
			@Param("page") Integer page, @Param("size") Integer size);

	@Insert("insert into t_product_topic_page (iwebsiteid, ilanguageid, ctype, ctitle, cimage, "
			+ "chtmlurl, bshow, icreateuserid, dcreatedate) values (#{iwebsiteid}, #{ilanguageid}, #{ctype}, "
			+ "#{ctitle}, #{cimage}, #{chtmlurl}, #{bshow}, #{icreateuserid}, #{dcreatedate})")
	Integer insert(TopicPage page);

	@Update("<script>update t_product_topic_page set iwebsiteid = #{iwebsiteid}, ilanguageid = #{ilanguageid}, "
			+ "ctype = #{ctype}, ctitle = #{ctitle}, <if test=\"cimage != null\">cimage = #{cimage}, </if>"
			+ "<if test=\"chtmlurl != null\">chtmlurl = #{chtmlurl}, </if>"
			+ "<if test=\"dcreatedate != null\">dcreatedate = #{dcreatedate}, </if>"
			+ "bshow = #{bshow}, icreateuserid = #{icreateuserid} where iid = #{iid}</script>")
	Integer update(TopicPage page);

	@Select("select iid, iwebsiteid, ilanguageid, ctype, ctitle, chtmlurl, bshow, "
			+ "icreateuserid, dcreatedate from t_product_topic_page where iid = #{0}")
	TopicPage getById(Integer id);

	@Select("select count(iid) from t_product_topic_page")
	Integer count();

	@Select("select cimage from t_product_topic_page where iid = #{0}")
	TopicPage getImage(Integer id);

	List<TopicPage> getTopicPage(@Param("type") String type,
			@Param("languageId") Integer languageId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("siteId") Integer siteId, @Param("size") Integer size);
}
