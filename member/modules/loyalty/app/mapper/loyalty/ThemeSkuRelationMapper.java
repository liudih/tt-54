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

import entity.loyalty.ThemeSkuRelation;
import forms.loyalty.theme.template.ThemeSkuRelationForm;

public interface ThemeSkuRelationMapper {
	@Delete({
			"delete from t_theme_sku_relation",
			" where iid = #{iid,jdbcType=INTEGER} and EXISTS(select t2.iid from t_theme t2 where t2.iid=ithemeid and t2.ienable!=1)" })
	int deleteByPrimaryKey(Integer iid);

	@Insert({ "insert into t_theme_sku_relation (ithemeid, ",
			" igroupid, csku, ", "isort)",
			" values (#{ithemeid,jdbcType=INTEGER}, ",
			" #{igroupid,jdbcType=INTEGER}, #{csku,jdbcType=VARCHAR}, ",
			" #{isort,jdbcType=INTEGER})" })
	int insert(ThemeSkuRelation record);

	@Select({ "select",
			"iid, ithemeid, igroupid, csku, isort from t_theme_sku_relation",
			"where iid = #{iid,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ithemeid", property = "ithemeid", jdbcType = JdbcType.INTEGER),
			@Result(column = "igroupid", property = "igroupid", jdbcType = JdbcType.INTEGER),
			@Result(column = "csku", property = "csku", jdbcType = JdbcType.VARCHAR),
			@Result(column = "isort", property = "isort", jdbcType = JdbcType.INTEGER) })
	ThemeSkuRelation selectByPrimaryKey(Integer iid);

	@Select({ "select iid, ithemeid, igroupid, csku, isort from t_theme_sku_relation" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ithemeid", property = "ithemeid", jdbcType = JdbcType.INTEGER),
			@Result(column = "igroupid", property = "igroupid", jdbcType = JdbcType.INTEGER),
			@Result(column = "csku", property = "csku", jdbcType = JdbcType.VARCHAR),
			@Result(column = "isort", property = "isort", jdbcType = JdbcType.INTEGER) })
	List<ThemeSkuRelation> selectAll();

	@Update({ "update t_theme_sku_relation",
			" set ithemeid = #{ithemeid,jdbcType=INTEGER},",
			" igroupid = #{igroupid,jdbcType=INTEGER},",
			" csku = #{csku,jdbcType=VARCHAR},",
			" isort = #{isort,jdbcType=INTEGER}",
			" where iid = #{iid,jdbcType=INTEGER}" })
	int updateByPrimaryKey(ThemeSkuRelation record);

	@Select("select * from t_theme_sku_relation where igroupid=#{0}")
	List<ThemeSkuRelation> getThemeSkuRelationsByThemeGroupId(int themeGroupId);

	@Select({
			"<script> ",
			" select t1.iid,t1.ithemeid,t1.igroupid,t1.csku,t1.isort,t4.cname as groupname,t5.ctitle as themetitle,t2.curl as themeurl,t2.ienable ",
			" from t_theme_sku_relation t1 inner join t_theme t2 on t2.iid=t1.ithemeid",
			" inner join t_theme_group t3 on t1.igroupid=t3.iid inner join t_theme_group_name t4 on t3.iid=t4.igroupid and t4.ilanguageid=1 ",
			" inner join t_theme_title t5 on t5.ithemeid=t1.ithemeid and t5.ilanguageid=1",
			"  where 1=1 ",
			" <if test=\"sku != null and sku != '' \"> and t1.csku = #{sku} </if>",
			" ORDER BY iid limit #{pageSize} offset (#{page}-1)*#{pageSize} ",
			"</script>" })
	List<ThemeSkuRelationForm> getPage(@Param("page") int page,
			@Param("pageSize") int pageSize, @Param("sku") String sku);

	@Select({
			"<script>",
			"select count(*) from t_theme_sku_relation",
			" where 1=1",
			" <if test=\"sku != null and sku != '' \"> and csku = #{sku} </if>",
			"</script>" })
	int getCount(@Param("sku") String sku);

	@Select("select * from t_theme_sku_relation where igroupid = #{0} order by isort")
	List<ThemeSkuRelation> selectByGroupId(Integer groupId);
}