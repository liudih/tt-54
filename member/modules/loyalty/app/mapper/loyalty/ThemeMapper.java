package mapper.loyalty;

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

import entity.loyalty.Theme;
import forms.loyalty.theme.template.ThemeForm;

public interface ThemeMapper {
	@Delete({ "delete from t_theme", "where iid = #{iid,jdbcType=INTEGER}" })
	int deleteByPrimaryKey(Integer iid);

	@Select({
			"<script>",
			"select count(*) from t_theme where 1=1",
			" <if test=\"curl != null and curl != '' \"> and curl = #{curl} </if>",
			" <if test=\"iid != null\">and iid = #{iid}</if>",
			" <if test=\"ienable != null\">  and ienable = #{ienable}</if>",
			"</script>" })
	int getCount(@Param("iid") Integer iid, @Param("ienable") Integer ienable,
			@Param("curl") String url);

	@Select({
			"<script> ",
			" select t2.cname as ccssname,t1.iid, t1.curl,t1. cbannerurl, t1.icssid, t1.ccreateuser, t1.dcreatedate, t1.cupdateuser, ",
			"t1.dupdatedate,t1.ienable,t1.denablestartdate,t1.denableenddate,t1.iwebsiteid",
			" from t_theme t1 left join t_theme_css t2 on t2.iid=t1.icssid where 1=1 ",
			" <if test=\"url != null and url != '' \"> and	t1.curl = #{url} </if>",
			" <if test=\"iid != null\">and t1.iid = #{iid}</if>",
			" <if test=\"ienable != null\">  and	t1.ienable = #{ienable}	</if>",
			" ORDER BY iid limit #{pageSize} offset (#{page}-1)*#{pageSize} ",
			"</script>" })
	List<ThemeForm> getPage(@Param("page") int page,
			@Param("pageSize") int pageSize, @Param("iid") Integer iid,
			@Param("ienable") Integer ienable, @Param("url") String url);

	@Options(keyProperty = "iid", useGeneratedKeys = true)
	@Insert({
			"insert into t_theme (curl, ",
			"cbannerurl, icssid,iwebsiteid, ",
			"ccreateuser,cupdateuser,",
			"ienable,denablestartdate,denableenddate)",
			"values (#{curl,jdbcType=VARCHAR}, ",
			"#{cbannerurl,jdbcType=VARCHAR}, #{icssid,jdbcType=INTEGER},#{iwebsiteid}, ",
			"#{ccreateuser},#{cupdateuser},",
			"#{ienable},#{denablestartdate},#{denableenddate})" })
	int insert(Theme record);

	@Select({
			"select",
			"iid, curl, cbannerurl, icssid, ccreateuser, dcreatedate, cupdateuser, dupdatedate,ienable,denablestartdate,denableenddate,iwebsiteid",
			"from t_theme", "where iid = #{iid,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "curl", property = "curl", jdbcType = JdbcType.VARCHAR),
			@Result(column = "cbannerurl", property = "cbannerurl", jdbcType = JdbcType.VARCHAR),
			@Result(column = "icssid", property = "icssid", jdbcType = JdbcType.INTEGER),
			@Result(column = "ccreateuser", property = "ccreateuser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dcreatedate", property = "dcreatedate", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "cupdateuser", property = "cupdateuser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dupdatedate", property = "dupdatedate", jdbcType = JdbcType.TIMESTAMP) })
	Theme selectByPrimaryKey(Integer iid);

	@Select({
			"select",
			"iid, curl, cbannerurl, icssid, ccreateuser, dcreatedate, cupdateuser, dupdatedate,ienable,denablestartdate,denableenddate,iwebsiteid",
			"from t_theme" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "curl", property = "curl", jdbcType = JdbcType.VARCHAR),
			@Result(column = "cbannerurl", property = "cbannerurl", jdbcType = JdbcType.VARCHAR),
			@Result(column = "icssid", property = "icssid", jdbcType = JdbcType.INTEGER),
			@Result(column = "ccreateuser", property = "ccreateuser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dcreatedate", property = "dcreatedate", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "cupdateuser", property = "cupdateuser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dupdatedate", property = "dupdatedate", jdbcType = JdbcType.TIMESTAMP) })
	List<Theme> selectAll();

	@Update({ "update t_theme", "set curl = #{curl,jdbcType=VARCHAR},",
			"cbannerurl = #{cbannerurl,jdbcType=VARCHAR},",
			"icssid = #{icssid,jdbcType=INTEGER},", "ienable = #{ienable},",
			"denablestartdate = #{denablestartdate},",
			"denableenddate = #{denableenddate},",
			"cupdateuser = #{cupdateuser,jdbcType=VARCHAR},",
			"dupdatedate = #{dupdatedate,jdbcType=TIMESTAMP},",
			"iwebsiteid = #{iwebsiteid}", "where iid = #{iid,jdbcType=INTEGER}" })
	int updateByPrimaryKey(Theme record);

	@Select("select COUNT(iid) from t_theme where icssid=#{0}")
	int getThemesCountByIcssId(int icssid);

	@Select("select * from t_theme where iid=#{0}")
	Theme getThemeByThemeId(int iid);

	@Select("select * from t_theme where curl=#{0}")
	Theme getThemeIidByCurl(String curl);

	/**
	 * 
	 * @param websiteId 
	 * @Title: getThemeByUrl
	 * @Description: TODO(通过url查询专题)
	 * @param @param themeName
	 * @param @param date
	 * @param @return
	 * @return Theme
	 * @throws 
	 * @author yinfei
	 */
	@Select("select * from t_theme where curl = #{themeName} "
			+ "and denablestartdate <= #{date} and denableenddate >= #{date} "
			+ "and ienable = 1 and iwebsiteid = #{websiteId}")
	Theme getThemeByUrl(@Param("themeName") String themeName, @Param("date") Date date, @Param("websiteId") int websiteId);
	/**
	 * 验证主题url是存在
	 * @param url
	 * @return  0 表示不存，大于1表示存在
	 */
	@Select("select count(*) from t_theme where curl=#{0}")
	public int validateUrl(String url);
}