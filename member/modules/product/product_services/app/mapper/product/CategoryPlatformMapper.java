package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.product.CategoryPlatform;
import dto.product.CategoryWebsiteWithName;

public interface CategoryPlatformMapper {

	@Select("SELECT p.*, n.cname, n.iid FROM t_category_platform p "
			+ "INNER JOIN t_category_name n ON n.icategoryid = p.icategoryid "
			+ "WHERE iparentid IS NULL "
			+ "AND n.ilanguageid = #{0} AND p.iplatformid = #{1} ORDER BY iposition")
	List<CategoryWebsiteWithName> getRootCategories(int languageid,
			int platformid);

	@Select("SELECT p.*, n.cname, n.iid FROM t_category_platform p "
			+ "INNER JOIN t_category_name n ON n.icategoryid = p.icategoryid "
			+ "WHERE iparentid = #{0} " + "AND n.ilanguageid = #{1} "
			+ "AND p.iplatformid = #{2} ORDER BY iposition")
	List<CategoryWebsiteWithName> getChildCategories(int parentCategoryId,
			int languageid, int platformid);

	@Select("SELECT p.*, n.cname, n.iid FROM t_category_platform p "
			+ "INNER JOIN t_category_name n ON n.icategoryid = p.icategoryid "
			+ "WHERE p.icategoryid = #{0} AND n.ilanguageid = #{1} "
			+ "AND p.iplatformid = #{2} ORDER BY iposition")
	CategoryWebsiteWithName getCategory(int categoryId, int languageid,
			int platformid);

	@Select("SELECT p.*, n.cname, n.iid FROM t_category_platform p "
			+ "INNER JOIN t_category_name n ON n.icategoryid = p.icategoryid "
			+ "WHERE iparentid = (select iid from t_category_platform where cpath = #{0}) "
			+ "AND n.ilanguageid = #{1} "
			+ "AND p.iplatformid = #{2} ORDER BY iposition")
	List<CategoryWebsiteWithName> getChildCategoriesByPath(String cpath,
			int languageid, int platformid);

	@Select("SELECT * from t_category_platform p " + "WHERE p.icategoryid = #{0} "
			+ "AND p.iplatformid = #{1} ORDER BY iposition")
	CategoryPlatform getPlatformCategories(int categoryId, int platformid);
}