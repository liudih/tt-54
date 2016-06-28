package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import valueobjects.category.CategoryLabelVo;
import valueobjects.product.CategoryLabelBase;
import dto.product.CategoryLabelName;

public interface CategoryLabelNameMapper {
	int insert(CategoryLabelName record);

	CategoryLabelName selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(CategoryLabelName record);

	@Select("select * from t_category_label_name where icategorylabelid = #{0} and ilanguageid = #{1}")
	CategoryLabelName getCategoryLabelNameByLabelIdAndLanguageId(
			Integer labelId, Integer languageId);

	@Select("select l.iid, l.iwebsiteid, l.ctype, l.icategoryid, t.iid as ilabelid, t.ilanguageid, t.curl, t.cprompt, "
			+ "t.cimages from t_category_label l left outer join t_category_label_name t "
			+ "on l.iid = t.icategorylabelid and t.ilanguageid = #{1} where l.iwebsiteid = #{0} and l.ctype = #{2} order by l.iid desc "
			+ "limit #{3} offset (#{3} * (#{4} - 1))")
	List<CategoryLabelBase> getCategoryLabelBases(Integer websiteId,
			Integer languageid, String type, Integer pageSize, Integer pageNum);

	@Select({
			"<script>",
			"select l.iid, l.iwebsiteid, l.ctype, l.icategoryid, t.iid as ilabelid, t.ilanguageid, t.curl, t.cprompt, ",
			"t.cimages from t_category_label l ",
			"inner join t_category_label_name t on l.iid = t.icategorylabelid and t.ilanguageid = #{1} ",
			"where l.iwebsiteid = #{0} and l.ctype = #{2} ",
			"<if test=\"list!=null and list.size>0\">",
			"and l.icategoryid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</if>",
			"</script>" })
	List<CategoryLabelBase> getCategoryLabelBasesByCategoryIds(
			Integer websiteId, Integer languageid, String type,
			@Param("list") List<Integer> categoryIds);

	@Select("select count(*) from t_category_label l where l.iwebsiteid = #{0} and l.ctype = #{1}")
	Integer getCategoryBaseCount(Integer websiteId, String type);

	@Delete("delete from t_category_label_name where icategorylabelid = #{0}")
	Integer deleteCategoryLabelNameByCategorylabelid(Integer labelId);

	@Select("select count(*) from t_category_label_name where icategorylabelid = #{0}")
	Integer selectCategoryLabelNameCount(Integer labelId);

	@Select("select tcn.cprompt,tcn.curl,tcn.iid from t_category_label_name tcn "
			+ "inner join t_category_label tcl on tcn.icategorylabelid = tcl.iid "
			+ "where tcl.icategoryid=#{0} and tcl.ctype='hot' "
			+ "and tcl.iwebsiteid =#{1} and tcn.ilanguageid =#{2} ")
	CategoryLabelName getByCategoryIdAndSiteIdAndLangId(Integer categoryId,
			Integer websiteId, Integer languageId);
	
	@Select({
		"<script>",
		"select l.icategoryid categoryId,n.iid categoryLabelNameId from t_category_label_name n",
		"inner join t_category_label l on n.icategorylabelid = l.iid and l.icategoryid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
		"and l.iwebsiteid=#{2} and l.ctype=#{1} ",
		"where n.ilanguageid=#{3} ",
		"</script>" })
	List<CategoryLabelVo> getByCategoryIdsAndType(@Param("list") List<Integer> categoryIds,String type,
			Integer websiteId, Integer languageId);
	
}