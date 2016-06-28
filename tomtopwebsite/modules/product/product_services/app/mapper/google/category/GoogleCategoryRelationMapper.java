package mapper.google.category;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.product.google.category.GoogleCategoryRelation;

public interface GoogleCategoryRelationMapper {
	
	@Select("select icategory from t_google_category_relation where igooglecategory = #{0}")
	List<Integer> getCidByGid(int gid);

	@Insert("insert into t_google_category_relation (igooglecategory,icategory) values (#{0},#{1})")
	int add(Integer icategory, Integer wcategory);

	@Select("select * from t_google_category_relation where igooglecategory = #{0} and icategory = #{1} ")
	GoogleCategoryRelation getRelationByGidAndWid(int gid, int wid);
	
	@Select("select * from t_google_category_relation where igooglecategory = #{0}")
	List<GoogleCategoryRelation> getRelationByGid(int gid);
	
	@Delete("delete from t_google_category_relation where igooglecategory = #{0} and icategory = #{1}")
	int deleteRelationByGidAndWid(int gid, int wid);
	
}
