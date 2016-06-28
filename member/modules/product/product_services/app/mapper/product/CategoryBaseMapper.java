package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import dto.product.CategoryBase;

public interface CategoryBaseMapper {
	int insert(CategoryBase record);

	int insertSelective(CategoryBase record);

	CategoryBase selectByPrimaryKey(Integer iid);

	int updateByPrimaryKey(CategoryBase record);

	@Select("select iid from t_category_base where iid in (select icategory from t_product_category_mapper "
			+ "where clistingid=#{clistingid}) and ichildrencount = 0 limit 1")
	Integer getLowestCategoryBase(String clistingid);

	@Select("select iid from t_category_website where cpath=#{0}")
	Integer getCategorieIdByPath(String cpath);

	@Select("with RECURSIVE cte as ( "
			+ " select a.iid,a.iparentid from t_category_base a where a.cpath=#{0} "
			+ " union all "
			+ " select k.iid,k.iparentid   from t_category_base k inner join cte c on k.iid = c.iparentid "
			+ " )select iid from cte ;")
	List<Integer> getFullCategoryIds(String cpath);

	@Select("select * from t_category_base")
	@ResultMap("BaseResultMap")
	List<CategoryBase> getAllCategory();

	@Select("<script>select max(iposition) from t_category_base where "
			+ "<if test='iparentid != null'> iparentid = #{iparentid}</if> "
			+ "<if test='iparentid == null'> iparentid is null</if> </script>")
	Integer getMaxPosition(@Param("iparentid") Integer iparentid);

	@Select("select max(iid) from t_category_base")
	Integer getMaxIid();

	@Select("<script>select count(iid) from t_category_base where "
			+ "<if test='iparentid != null'> iparentid = #{iparentid}</if> "
			+ "<if test='iparentid == null'> iparentid is null</if> </script>")
	Integer getCategoryBaseCount(@Param("iparentid") Integer iparentid);

	@Select("select * from t_category_base where iid=#{0}")
	CategoryBase getCategoryBaseByiid(int iid);
}