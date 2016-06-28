package mapper.activitydb.page;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import entity.activity.page.PageTitle;


public interface PageTitleMapper {
    @Delete({
        "delete from t_page_title",
        "where iid = #{iid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer iid);

    @Insert({
        "insert into t_page_title (ipageid, ",
        "ctitle, ilanguageid)",
        "values (#{ipageid,jdbcType=INTEGER}, ",
        "#{ctitle,jdbcType=VARCHAR}, #{ilanguageid,jdbcType=INTEGER})"
    })
    int insert(PageTitle record);

    @Select({
        "select",
        "iid, ipageid, ctitle, ilanguageid",
        "from t_page_title",
        "where iid = #{iid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="iid", property="iid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="ipageid", property="ipageid", jdbcType=JdbcType.INTEGER),
        @Result(column="ctitle", property="ctitle", jdbcType=JdbcType.VARCHAR),
        @Result(column="ilanguageid", property="ilanguageid", jdbcType=JdbcType.INTEGER)
    })
    PageTitle selectByPrimaryKey(Integer iid);

    @Select({
        "select",
        "iid, ipageid, ctitle, ilanguageid",
        "from t_page_title"
    })
    @Results({
        @Result(column="iid", property="iid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="ipageid", property="ipageid", jdbcType=JdbcType.INTEGER),
        @Result(column="ctitle", property="ctitle", jdbcType=JdbcType.VARCHAR),
        @Result(column="ilanguageid", property="ilanguageid", jdbcType=JdbcType.INTEGER)
    })
    List<PageTitle> selectAll();

    @Update({
        "update t_page_title",
        "set ipageid = #{ipageid,jdbcType=INTEGER},",
          "ctitle = #{ctitle,jdbcType=VARCHAR},",
          "ilanguageid = #{ilanguageid,jdbcType=INTEGER}",
        "where iid = #{iid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(PageTitle record);
    /**
	 * 根具页面Id获取标题
	 * @param pageid
	 * @return
	 */
    @Select("select * from t_page_title where ipageid=#{0}")
	List<PageTitle> getListByPageid(Integer pageid);

    /**
     * 
     * @Title: getPTByPageIdAndLId
     * @Description: TODO(通过活动id和语言id查询活动标题)
     * @param @param iid
     * @param @param languageId
     * @param @return
     * @return PageTitle
     * @throws 
     * @author yinfei
     */
    @Select("select * from t_page_title where ipageid = #{pageId} and ilanguageid = #{languageId}")
	PageTitle getPTByPageIdAndLId(@Param("pageId")Integer iid, @Param("languageId")int languageId);
}