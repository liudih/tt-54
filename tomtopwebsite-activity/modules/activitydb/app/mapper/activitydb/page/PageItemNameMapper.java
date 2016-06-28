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

import entity.activity.page.PageItemName;

public interface PageItemNameMapper {
    @Delete({
        "delete from t_page_item_name",
        "where iid = #{iid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer iid);

    @Insert({
        "insert into t_page_item_name (ipageitemid, ",
        "cname, ilanguageid)",
        "values (#{ipageitemid,jdbcType=INTEGER}, ",
        "#{cname,jdbcType=VARCHAR}, #{ilanguageid,jdbcType=INTEGER})"
    })
    int insert(PageItemName record);

    @Select({
        "select",
        "iid, ipageitemid, cname, ilanguageid",
        "from t_page_item_name",
        "where iid = #{iid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="iid", property="iid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="ipageitemid", property="ipageitemid", jdbcType=JdbcType.INTEGER),
        @Result(column="cname", property="cname", jdbcType=JdbcType.VARCHAR),
        @Result(column="ilanguageid", property="ilanguageid", jdbcType=JdbcType.INTEGER)
    })
    PageItemName selectByPrimaryKey(Integer iid);

    @Select({
        "select",
        "iid, ipageitemid, cname, ilanguageid",
        "from t_page_item_name"
    })
    @Results({
        @Result(column="iid", property="iid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="ipageitemid", property="ipageitemid", jdbcType=JdbcType.INTEGER),
        @Result(column="cname", property="cname", jdbcType=JdbcType.VARCHAR),
        @Result(column="ilanguageid", property="ilanguageid", jdbcType=JdbcType.INTEGER)
    })
    List<PageItemName> selectAll();

    @Update({
        "update t_page_item_name",
        "set ipageitemid = #{ipageitemid,jdbcType=INTEGER},",
          "cname = #{cname,jdbcType=VARCHAR},",
          "ilanguageid = #{ilanguageid,jdbcType=INTEGER}",
        "where iid = #{iid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(PageItemName record);	
    @Select("select * from t_page_item_name where ipageitemid=#{0}")
    public List<PageItemName> getListByPageItemid(Integer pageid);

    /**
     * 
     * @Title: getPINameByPIIdAndLId
     * @Description: TODO(通过活动项目id和语言id查询活动项目名称)
     * @param @param iid
     * @param @param languageId
     * @param @return
     * @return String
     * @throws 
     * @author yinfei
     */
    @Select("select cname from t_page_item_name where ipageitemid = #{pageItemId} and ilanguageid = #{languageId}")
	String getPINameByPIIdAndLId(@Param("pageItemId")Integer iid, @Param("languageId")int languageId);
}