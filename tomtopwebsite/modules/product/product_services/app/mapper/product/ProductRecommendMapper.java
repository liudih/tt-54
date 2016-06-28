package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.product.ProductRecommend;

public interface ProductRecommendMapper {

	@Insert("insert into t_product_recommend(clistingid,crecommendlisting,ccreateuser) values(#{0},#{1},#{2})")
	int add(String clistinid, String crecommendlisting, String ccreateuser);

	@Select("SELECT crecommendlisting , crecommendsku FROM t_product_recommend WHERE clistingid = #{clistingid}")
	List<ProductRecommend> getProductRecommendsByListingId(String clistingid);

	@Delete("delete from t_product_recommend ")
	int deleteAllRecommendProduct();

}