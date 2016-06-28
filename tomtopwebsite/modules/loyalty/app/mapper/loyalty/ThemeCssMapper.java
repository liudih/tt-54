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

import entity.loyalty.ThemeCss;
import forms.loyalty.theme.template.ThemeCssForm;

public interface ThemeCssMapper {
	@Delete({ "delete from t_theme_css", "where iid = #{iid,jdbcType=INTEGER}" })
	int deleteByPrimaryKey(Integer iid);

	@Insert({
			"insert into t_theme_css (cvalue, ",
			"cname, ccreateuser, ",
			"dcreatedate, cupdateuser, ",
			"dupdatedate)",
			"values (#{cvalue,jdbcType=VARCHAR}, ",
			"#{cname,jdbcType=VARCHAR}, #{ccreateuser,jdbcType=VARCHAR}, ",
			"#{dcreatedate,jdbcType=TIMESTAMP}, #{cupdateuser,jdbcType=VARCHAR}, ",
			"#{dupdatedate,jdbcType=TIMESTAMP})" })
	int insert(ThemeCss record);


	@Select("select count(iid) from t_theme_css where cname = #{0}")
	int getThemeCssCountByCname(String cname);

	@Select("<script>"
			+ "select count(iid) from t_theme_css where 1=1 "
			+ "<if test=\"iid != null  \">and iid=#{iid} </if>"
			+ "<if test=\"cname != null  \">and cname=#{cname} </if>"
			+ "<if test=\"ccreateuser !=null \">and ccreateuser=#{ccreateuser}</if> "
			+ "</script>")
	Integer getCount(@Param("iid") Integer iid,
			@Param("cname") String cname,
			@Param("ccreateuser") String ccreateuser);


	@Select("<script>"
			+ "select * from t_theme_css where 1=1 "
			+ "<if test=\"iid != null  \">and iid=#{iid} </if>"
			+ "<if test=\"cname != null  \">and cname=#{cname} </if>"
			+ "<if test=\"ccreateuser !=null \">and ccreateuser=#{ccreateuser}</if> ORDER BY Iid desc limit #{pageSize} offset #{pageSize} * (#{pageNum} - 1)"
			+ "</script>")
	List<ThemeCss> getList(@Param("iid") Integer iid,
			@Param("cname") String cname,
			@Param("ccreateuser") String ccreateuser,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

	/**
	 * 获取所有的数据
	 * 
	 * @return
	 */
	@Select("select * from t_theme_css")
	public List<ThemeCss> getAll();

	@Select({
			"select",
			"iid, cvalue, cname, ccreateuser, dcreatedate, cupdateuser, dupdatedate",
			"from t_theme_css", "where iid = #{iid,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "cvalue", property = "cvalue", jdbcType = JdbcType.VARCHAR),
			@Result(column = "cname", property = "cname", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ccreateuser", property = "ccreateuser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dcreatedate", property = "dcreatedate", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "cupdateuser", property = "cupdateuser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dupdatedate", property = "dupdatedate", jdbcType = JdbcType.TIMESTAMP) })
	ThemeCss selectByPrimaryKey(Integer iid);

	@Update({ "update t_theme_css", "set cvalue = #{cvalue,jdbcType=VARCHAR},",
			"cname = #{cname,jdbcType=VARCHAR},",
			"cupdateuser = #{cupdateuser,jdbcType=VARCHAR},",
			"dupdatedate = #{dupdatedate,jdbcType=TIMESTAMP}",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int updateByPrimaryKey(ThemeCssForm record);
}