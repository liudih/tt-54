package mapper.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.product.ProductActivityRelationDetail;

public interface ProductActivityRelationDetailMapper {

	@Insert("insert into t_product_activity_relation_detail "
			+ "(iactivityid,cspu,csku,clistingid,fprice,iqty) "
			+ "values(#{productActivityRelationDetail.iactivityid},#{productActivityRelationDetail.cspu},"
			+ "#{productActivityRelationDetail.csku},#{productActivityRelationDetail.clistingid},#{productActivityRelationDetail.fprice},#{productActivityRelationDetail.iqty})")
	int addProductRelationDetail(Map<String, Object> param);

	@Select("select csku from t_product_activity_relation_detail where cspu = #{0} and iactivityid = #{1}")
	List<String> getProductBySpu(String spu,int aid);
	
	@Select("select * from t_product_activity_relation_detail where cspu = #{0} and iactivityid = #{1} limit 1")
	ProductActivityRelationDetail getPriceBySpuAndIid(String spu, int activityid);
	
	@Select("select * from t_product_activity_relation_detail where cspu =#{0} and iactivityid = #{1}")
	List<ProductActivityRelationDetail> getAllProductBySpuAndIid(String spu, int activityid);

	@Update("update t_product_activity_relation_detail set fprice = #{0} ,iqty = #{1} where iactivityid = #{2} and clistingid = #{3}")
	int updateProductByIidAndListingid(double price,int qty, int aid, String listingid);

	@Update("update t_product_activity_relation_detail set fprice = #{0}  where iactivityid = #{1} and clistingid = #{2}")
	int updatePriceByIidAndListingid(double price, int aid, String listingid);

}
