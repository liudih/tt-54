package mapper.wholesale;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import entity.wholesale.WholeSaleProduct;

public interface WholeSaleProductMapper {
	@Delete("delete from t_wholesale_product where iid = #{0} and cemail = #{1}")
	int deleteByIid(Integer iid, String email);

	@Insert("insert into t_wholesale_product(iwebsiteid, csku, cemail, iqty) values(#{iwebsiteid}, #{csku}, #{cemail}, #{iqty})")
	int addWholeSaleProduct(WholeSaleProduct record);

	@Select("select * from t_wholesale_product where cemail = #{0} and iwebsiteid = #{1}")
	List<WholeSaleProduct> getWholeSaleProductsByEmail(String email,
			Integer websiteId);

	@Update("update t_wholesale_product set iqty = #{1} where iid = #{0}")
	int updateQtyByIid(Integer iid, Integer qty);

	@Select("select * from t_wholesale_product where cemail = #{0} and iwebsiteid = #{1} and csku=#{2} limit 1")
	WholeSaleProduct getWholeSaleProductsByEmailAndSkuAndWebsite(String email,
			Integer websiteId, String sku);

	@Select("<script>select * from t_wholesale_product where iid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>"
			+ "#{item}</foreach></script>")
	List<WholeSaleProduct> getByIDs(List<Integer> ids);

	@Delete({
			"<script>",
			"delete from t_wholesale_product ",
			"where cemail = #{cemail} and iid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	Integer batchDeleteByIids(@Param("list") List<Integer> ids,
			@Param("cemail") String cemail);
}