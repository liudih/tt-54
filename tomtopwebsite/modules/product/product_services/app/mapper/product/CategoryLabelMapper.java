package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import dto.product.CategoryLabel;

public interface CategoryLabelMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(CategoryLabel record);

	@Select("select icategoryid from t_category_label where iwebsiteid = #{0} and ctype = #{1}")
	List<Integer> getCategoryIdByWebsiteIdAndType(Integer siteId, String type);

	@Select("select * from t_category_label where iwebsiteid = #{0} and ctype = #{1}")
	List<CategoryLabel> getCategoryLabelByWebsiteIdAndType(Integer siteId,
			String type);

	int batchInsert(List<CategoryLabel> categoryLabels);

	@Delete("delete from t_category_label where iwebsiteid = #{0} and ctype = #{1}")
	void deleteCategoryLabelByType(Integer website, String type);
}