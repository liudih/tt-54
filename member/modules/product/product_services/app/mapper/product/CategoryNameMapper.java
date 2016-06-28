package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.mybatis.guice.transactional.Transactional;

import valueobjects.product.category.CategoryItem;
import valueobjects.product.category.CategoryMessage;
import dto.product.CategoryName;

public interface CategoryNameMapper {
	int insert(CategoryName record);

	@Transactional
	int insertSelective(CategoryName record);

	CategoryName selectByPrimaryKey(Integer iid);

	int updateByPrimaryKey(CategoryName record);

	CategoryName getNameByCategoryId(Integer categoryId);

	@Select("select * from t_category_name where icategoryid = #{0} and ilanguageid = #{1}")
	@ResultMap("BaseResultMap")
	CategoryName getCategoryNameByCategoryIdAndLanguage(Integer categoryId,
			int language);

	@Select("SELECT cn.* FROM t_category_name cn "
			+ "INNER JOIN t_category_website b ON b.icategoryid = cn.icategoryid "
			+ "WHERE b.cpath = #{0} AND ilanguageid = #{1} and b.iwebsiteid=#{2}")
	CategoryName getCategoryNameByPathAndLanguage(String path, int language,
			int siteId);

	@Select("select cn.icategoryid as id,cn.cname as name,cw.cpath as path "
			+ "from t_category_website cw inner "
			+ "join t_category_name cn on cn.icategoryid = cw.icategoryid and cw.iparentid is null "
			+ "inner join t_category_label tcl on cw.iwebsiteid = tcl.iwebsiteid and cw.icategoryid=tcl.icategoryid "
			+ "where tcl.ctype='hot' and cn.ilanguageid=#{0} and cw.iwebsiteid=#{1} limit 4")
	List<CategoryItem> getHotFirstCategoryNames(int language, int siteID);

	@Select("select tcn.cname as name ,cw.cpath as path from t_category_name cn "
			+ "inner join t_category_website cw on cn.icategoryid = cw.iparentid "
			+ "inner join t_category_name tcn on cw.icategoryid = tcn.icategoryid and cn.ilanguageid=tcn.ilanguageid "
			+ "inner join t_category_label tcl on cn.icategoryid=tcl.icategoryid  "
			+ "where cn.ilanguageid =#{2} and cw.iwebsiteid=#{1} and cn.icategoryid =#{0} "
			+ "and tcl.iwebsiteid=#{1} and tcl.ctype ='hot' limit 5")
	List<CategoryItem> getHotSecondCategoryNames(Integer icategoryid,
			Integer site, Integer language);

	@Select("select cn.iid,cn.icategoryid,cn.cname from t_category_name cn "
			+ "inner join t_category_website cw on cn.icategoryid = cw.icategoryid "
			+ "where cw.ilevel=1 and cn.ilanguageid =#{1} and cw.iwebsiteid=#{0} "
			+ "order by cn.iid asc limit #{3} offset #{2}")
	List<CategoryName> getSecondCategoryNamesPage(Integer site,
			Integer language, Integer page, Integer size);

	@Select("select count(cn.*) from t_category_name cn "
			+ "inner join t_category_website cw on cn.icategoryid = cw.icategoryid "
			+ "where cw.ilevel=1 and cn.ilanguageid =#{1} and cw.iwebsiteid=#{0} ")
	int getSecondCategoryNamesPageCount(Integer site, Integer language);

	@Select({
			"<script>",
			"select distinct * from t_category_name n ",
			"INNER JOIN t_category_website w ON w.icategoryid = n.icategoryid and w.iwebsiteid = #{2} ",
			"WHERE n.ilanguageid = #{1} and ",
			"n.icategoryid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<CategoryName> getCategoryNameByCategoryIds(
			@Param("list") List<Integer> categoryids, int languageid,
			int websiteid);

	@Select("SELECT cn.*, b.bshow, b.iposition, b.iid icatetorywebsiteiid,b.cpath, g.cbackgroundimages, "
			+ " g.ibottom, g.iright, g.iid ibackgroundid, g.curl curl ,g.iwebsiteid"
			+ " FROM t_category_name cn "
			+ " INNER JOIN t_category_website b ON b.icategoryid = cn.icategoryid "
			+ " LEFT OUTER JOIN t_category_backgroundimages g ON cn.iid = g.icategorynameid"
			+ " where b.iid = #{0} and cn.ilanguageid = #{1} and b.iwebsiteid=#{2}")
	CategoryMessage getCategoryMessageByCategoryIdAndLanguage(
			Integer categorywebsiteid, Integer ilanguageid, Integer websiteId);

	@Select("SELECT n.* FROM t_category_name n "
			+ "INNER JOIN t_category_website w ON w.icategoryid = n.icategoryid and w.iwebsiteid = #{1} "
			+ "WHERE n.ilanguageid = #{0} ")
	List<CategoryName> getCategoryNameByLanguageIdAndWebsiteId(int languageid,
			int websiteid);

	@Select("select tn.icategoryid,tn.cname from  t_category_name tn "
			+ "inner join t_category_label tl on tn.icategoryid = tl.icategoryid "
			+ "inner join t_category_website tw on tw.icategoryid = tn.icategoryid "
			+ "where tn.ilanguageid = #{0} and tl.iwebsiteid = #{1} and tw.iwebsiteid = #{1} "
			+ "and tw.ilevel = 2 and  tw.bshow=true and tl.ctype='new' order by random() limit 50")
	List<CategoryName> getNewArrivalsCategoryNames(Integer language,
			Integer site);

	@Select("select max(iid) from t_category_name")
	Integer getMaxIid();

	@Select({"<script>",
			 "select * from t_category_name where icategoryid in "
			 + " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach>"
			 + " and ilanguageid = 1"
			 + " </script>"
		   })
	List<CategoryName> getAllByCid(@Param("list") List<Integer> categoryids);

	@Select("select * from t_category_name where cname = #{0} and ilanguageid = 1")
	CategoryName getCategoryByCname(String cname);
}