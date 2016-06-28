package mapper.activitydb.page;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import dto.image.Img;
import dto.member.MemberBase;
import dto.order.Order;
import values.activity.page.PageQuery;
import entity.activity.page.Page;

public interface PageMapper {
 @Delete({
        "delete from t_page",
        "where iid = #{iid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer iid);
    
    @Options(keyProperty="iid",useGeneratedKeys=true)
    @Insert({
        "insert into t_page ( curl, ",
        "itype, cbannerurl, ",
        "iwebsiteid, crecommendvalues, ",
        "ccreateuser, dcreatedate, ",
        "cupdateuser, dupdatedate, ",
        "ienable, denablestartdate, ",
        "denableenddate,itemplateid)",
        "values (#{curl,jdbcType=VARCHAR}, ",
        "#{itype,jdbcType=INTEGER}, #{cbannerurl,jdbcType=VARCHAR}, ",
        "#{iwebsiteid,jdbcType=INTEGER}, #{crecommendvalues,jdbcType=VARCHAR}, ",
        "#{ccreateuser,jdbcType=VARCHAR}, #{dcreatedate,jdbcType=TIMESTAMP}, ",
        "#{cupdateuser,jdbcType=VARCHAR}, #{dupdatedate,jdbcType=TIMESTAMP}, ",
        "#{ienable,jdbcType=INTEGER}, #{denablestartdate,jdbcType=TIMESTAMP}, ",
        "#{denableenddate,jdbcType=TIMESTAMP},#{itemplateid,jdbcType=INTEGER})"
    })
    int insert(Page page);

    @Select({
        "select",
        "iid, curl, itype, cbannerurl, iwebsiteid, crecommendvalues, ccreateuser, dcreatedate, ",
        "cupdateuser, dupdatedate, ienable, denablestartdate, denableenddate,itemplateid",
        "from t_page",
        "where iid = #{iid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="iid", property="iid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="curl", property="curl", jdbcType=JdbcType.VARCHAR),
        @Result(column="itype", property="itype", jdbcType=JdbcType.INTEGER),
        @Result(column="cbannerurl", property="cbannerurl", jdbcType=JdbcType.VARCHAR),
        @Result(column="iwebsiteid", property="iwebsiteid", jdbcType=JdbcType.INTEGER),
        @Result(column="crecommendvalues", property="crecommendvalues", jdbcType=JdbcType.VARCHAR),
        @Result(column="ccreateuser", property="ccreateuser", jdbcType=JdbcType.VARCHAR),
        @Result(column="dcreatedate", property="dcreatedate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="cupdateuser", property="cupdateuser", jdbcType=JdbcType.VARCHAR),
        @Result(column="dupdatedate", property="dupdatedate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="ienable", property="ienable", jdbcType=JdbcType.INTEGER),
        @Result(column="denablestartdate", property="denablestartdate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="denableenddate", property="denableenddate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="itemplateid", property="itemplateid", jdbcType=JdbcType.INTEGER)
    })
    Page selectByPrimaryKey(Integer iid);

    @Select({
        "select",
        "iid, curl, itype, cbannerurl, iwebsiteid, crecommendvalues, ccreateuser, dcreatedate, ",
        "cupdateuser, dupdatedate, ienable, denablestartdate, denableenddate,itemplateid",
        "from t_page"
    })
    @Results({
        @Result(column="iid", property="iid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="curl", property="curl", jdbcType=JdbcType.VARCHAR),
        @Result(column="itype", property="itype", jdbcType=JdbcType.INTEGER),
        @Result(column="cbannerurl", property="cbannerurl", jdbcType=JdbcType.VARCHAR),
        @Result(column="iwebsiteid", property="iwebsiteid", jdbcType=JdbcType.INTEGER),
        @Result(column="crecommendvalues", property="crecommendvalues", jdbcType=JdbcType.VARCHAR),
        @Result(column="ccreateuser", property="ccreateuser", jdbcType=JdbcType.VARCHAR),
        @Result(column="dcreatedate", property="dcreatedate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="cupdateuser", property="cupdateuser", jdbcType=JdbcType.VARCHAR),
        @Result(column="dupdatedate", property="dupdatedate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="ienable", property="ienable", jdbcType=JdbcType.INTEGER),
        @Result(column="denablestartdate", property="denablestartdate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="denableenddate", property="denableenddate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="itemplateid", property="itemplateid", jdbcType=JdbcType.INTEGER)
    })
    List<Page> selectAll();
    
    @Update({
        "update t_page",
        "set curl = #{curl,jdbcType=VARCHAR},",
          "itype = #{itype,jdbcType=INTEGER},",
          "cbannerurl = #{cbannerurl,jdbcType=VARCHAR},",
          "iwebsiteid = #{iwebsiteid,jdbcType=INTEGER},",
          "crecommendvalues = #{crecommendvalues,jdbcType=VARCHAR},",
          "cupdateuser = #{cupdateuser,jdbcType=VARCHAR},",
          "dupdatedate = #{dupdatedate,jdbcType=TIMESTAMP},",
          "ienable = #{ienable,jdbcType=INTEGER},",
          "denablestartdate = #{denablestartdate,jdbcType=TIMESTAMP},",
          "denableenddate = #{denableenddate,jdbcType=TIMESTAMP},",
          "itemplateid = #{itemplateid,jdbcType=INTEGER} ",
        "where iid = #{iid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Page page);
    
    /**
	 * 获取总数，配合getPage用的
	 * @param ienable
	 * @param url
	 * @param type
	 * @return
	 */
    @Select({
		"<script> ",
		" select count(*) from t_page where 1=1",
		" <if test=\"url != null and url != '' \"> and	curl = #{url} </if>",
		" <if test=\"type != null\">and itype = #{type}</if>",
		" <if test=\"ienable != null\"> and ienable = #{ienable}	</if>",
		"</script>" })
	int getCount(@Param("ienable")Integer ienable,@Param("url")String url,@Param("type")Integer type);

	/**
	 * 获取分页数据
	 * @param page 页码
	 * @param pagesize 页面大小
	 * @param ienable  查询条件是否启用
	 * @param url 查询条件主题url
	 * @param iid 页面类型
	 * @return
	 */
	@Select({
		"<script> ",
		" select * from t_page where 1=1",
		" <if test=\"url != null and url != '' \"> and	curl = #{url} </if>",
		" <if test=\"type != null\">and itype = #{type}</if>",
		" <if test=\"ienable != null\"> and ienable = #{ienable}	</if>",
		" ORDER BY iid limit #{pagesize} offset (#{page}-1)*#{pagesize} ",
		"</script>" })
	List<PageQuery> getPage(@Param("page")int page,@Param("pagesize")int pagesize,@Param("ienable")Integer ienable,@Param("url")String url,@Param("type")Integer type);
    @Select("select count(*) from t_page where curl=#{0}")
	public int validateUrl(String url);

    /**
     * 
     * @Title: getPageByUrl
     * @Description: TODO(通过url查询活动页面)
     * @param @param title
     * @param @param websiteId
     * @param @return
     * @return Page
     * @throws 
     * @author yinfei
     */
    @Select("select * from t_page where curl = #{title} and iwebsiteid = #{websiteId}")
	Page getPageByUrl(@Param("title")String title,@Param("websiteId")int websiteId);

}