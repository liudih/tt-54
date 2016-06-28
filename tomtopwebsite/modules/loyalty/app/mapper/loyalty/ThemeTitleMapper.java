package mapper.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import entity.loyalty.ThemeTitle;

public interface ThemeTitleMapper {
	@Delete({ "delete from t_theme_title",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int deleteByPrimaryKey(Integer iid);

	@Select({ "select * from t_theme_title where ithemeid=#{themeid}" })
	List<ThemeTitle> getListByThemeId(@Param("themeid") Integer themeid);

	@Insert({ "insert into t_theme_title (ithemeid, ", "ctitle, ilanguageid) ",
			"values (#{ithemeid,jdbcType=INTEGER}, ",
			"#{ctitle,jdbcType=VARCHAR}, #{ilanguageid,jdbcType=INTEGER}) ", })
	int insert(ThemeTitle record);

	@Select({ "select", "iid, ithemeid, ctitle, ilanguageid",
			"from t_theme_title", "where iid = #{iid,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ithemeid", property = "ithemeid", jdbcType = JdbcType.INTEGER),
			@Result(column = "ctitle", property = "ctitle", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ilanguageid", property = "ilanguageid", jdbcType = JdbcType.INTEGER) })
	ThemeTitle selectByPrimaryKey(Integer iid);

	@Select({ "select", "iid, ithemeid, ctitle, ilanguageid, iwebsiteid",
			"from t_theme_title" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ithemeid", property = "ithemeid", jdbcType = JdbcType.INTEGER),
			@Result(column = "ctitle", property = "ctitle", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ilanguageid", property = "ilanguageid", jdbcType = JdbcType.INTEGER) })
	List<ThemeTitle> selectAll();

	@Update({ "update t_theme_title",
			"set ctitle = #{ctitle,jdbcType=VARCHAR}",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int updateByPrimaryKey(ThemeTitle record);

	/**
	 * 
	 * @Title: getThemeIdByTitle
	 * @Description: TODO(通过标题获取专题id)
	 * @param @param themeName
	 * @param @return
	 * @return Integer
	 * @throws 
	 * @author yinfei
	 */
	@Select("select ithemeid from t_theme_title where ctitle = #{themeName}")
	Integer getThemeIdByTitle(@Param("themeName") String themeName);

	/**
	 * 
	 * @Title: getTTByThemeIdAndLanguageId
	 * @Description: TODO(通过主题id和语言id查询主题标题)
	 * @param @param iid
	 * @param @param languageId
	 * @param @return
	 * @return ThemeTitle
	 * @throws 
	 * @author yinfei
	 */
	@Select("select * from t_theme_title where ithemeid = #{themeId} and ilanguageid = #{languageId}")
	ThemeTitle getTTByThemeIdAndLanguageId(@Param("themeId")Integer iid, @Param("languageId")int languageId);
}