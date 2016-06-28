package mapper.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.product.ProductActivityRelation;
import dto.product.ProductBase;

public interface ProductActivityRelationMapper {
	@Insert("insert into t_product_activity_relation "
			+ "(cparentspu,csubspu,iwebsiteid,dfromdate,dtodate,cparentlistingid,csublistingid) "
			+ "values(#{productActivityRelation.cparentspu},#{productActivityRelation.csubspu},#{productActivityRelation.iwebsiteid},"
			+ "#{productActivityRelation.dfromdate},#{productActivityRelation.dtodate},#{productActivityRelation.cparentlistingid},#{productActivityRelation.csublistingid})")
	int addProductRelation(Map<String, Object> param);

	@Select("select * from t_product_activity_relation order by iid desc")
	List<ProductActivityRelation> getAllProduct();

	@Select("select * from t_product_activity_relation where bvisible = 'true' and dfromdate<#{0} order by iid desc limit #{1} offset #{2} ")
	List<ProductActivityRelation> getProductByDate(Date date,int pageSize, int page);

	@Select("select count(*) from t_product_activity_relation where bvisible = 'true' and dfromdate<#{0} ")
	int getCount(Date date);

	@Select("select b.* from t_product_base b where b.clistingid in "
			+ "(select clistingid from t_product_activity_relation_detail "
			+ "where cspu in (select cspu from t_product_activity_relation_detail "
			+ "where clistingid = #{0}) and b.iwebsiteid = #{1})")
	List<ProductBase> getProductsWithSameParentSkuByListingId(String listingId,
			Integer websiteId);

	@Select("select * from t_product_activity_relation where cparentspu = #{0} and iid = #{1} limit 1")
	ProductActivityRelation getProductBySpu(String cparentspu,int iid);

	@Update("update t_product_activity_relation set bvisible = #{1} where iid = #{0}")
	int updateProductByIid(int iid, boolean status);

	@Update("update t_product_activity_relation set dfromdate = #{0},dtodate= #{1} where cparentspu = #{2} and iid = #{3}")
	int updateProductBySpuAndIid(Date dfromdate, Date dtodate,
			String cparentspu, int iid);
	
	@Select("select iid from t_product_activity_relation where cparentlistingid = #{0} and cparentspu = #{1}")
	int getIidByListingidAndSpu(String listingid,String spu);
}
