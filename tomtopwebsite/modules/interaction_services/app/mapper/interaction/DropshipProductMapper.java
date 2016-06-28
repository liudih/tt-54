package mapper.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.interaction.DropshipProduct;

public interface DropshipProductMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(DropshipProduct record);

	int insertSelective(DropshipProduct record);

	DropshipProduct selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(DropshipProduct record);

	int updateByPrimaryKey(DropshipProduct record);

	@Select({
			"<script>",
			"select csku from t_dropship_product where cemail =#{email} and iwebsiteid=#{websiteid} ",
			"<if test=\"state !=null\">and bstate=#{state}</if>",
			"<if test=\"limit !=null\">limit #{limit}</if></script>" })
	List<String> getDropshipProductSkusByEmailAndState(
			@Param("email") String email, @Param("state") Boolean state,
			@Param("limit") Integer limit, @Param("websiteid") Integer websiteid);

	@Update("update t_dropship_product set bstate=#{2} where  csku=#{0} and cemail = #{1} ")
	Integer setDropshipProductState(String sku, String email, Boolean state);

	@Update({
			"<script>",
			"udpate t_dropship_product set bstate=#{state} , dupdatestatedate=now() ",
			"where cemail = #{email} and iwebsiteid=#{siteId} and csku in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	void batchSetDropshipProductState(@Param("list") List<String> skus,
			@Param("email") String email, @Param("state") Boolean state,
			@Param("siteId") Integer siteId);

	@Select("select count(iid) from t_dropship_product where cemail =#{0} and bstate=true and iwebsiteid=#{1} ")
	int getCountDropShipSkuByEmail(String email, Integer siteId);

	@Select("select iid,cemail,csku,bstate,dcreatedate,dupdatestatedate from t_dropship_product where cemail =#{0} and csku=#{1} and iwebsiteid=#{2}")
	DropshipProduct getDropshipProductByEmailAndSku(String email, String sku,
			Integer siteId);

	@Select({
			"<script>",
			"select * from t_dropship_product ",
			"where cemail = #{email}  and iwebsiteid=#{siteId} and csku in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	List<DropshipProduct> getDropshipProductsByEmailAndSkus(
			@Param("email") String email, @Param("list") List<String> skus,
			@Param("siteId") Integer siteId);

	@Delete({
			"<script>",
			"delete from t_dropship_product ",
			"where iid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	Integer batchDeleteDropshipProduct(@Param("list") List<Integer> ids);

	@Update({
			"<script>",
			"update t_dropship_product set bstate=#{state}, dupdatestatedate=now()",
			"where cemail = #{email} and iid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	int batchSetDropshipProductStatus(@Param("email") String email,
			@Param("list") List<Integer> ids, @Param("state") Boolean state);

	@Update("update t_dropship_product set bstate=#{2} , dupdatestatedate=now() where cemail = #{1} and iid =#{0} ")
	int setDropShipStatus(Integer id, String email, Boolean status);

}