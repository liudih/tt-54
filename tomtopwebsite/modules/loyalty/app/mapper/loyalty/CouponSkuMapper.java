package mapper.loyalty;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import entity.loyalty.CouponSku;

public interface CouponSkuMapper {
	@Select({
			"<script> ",
			" select * from t_coupon_sku_relation where 1=1  ",
			" <if test=\"csku != null and csku != '' \"> and csku= #{csku} </if>",
			" order by dupdatedate desc limit #{pageSize} offset (#{page}-1)*#{pageSize} ",
			"</script>" })
	List<CouponSku> getAll(@Param("page") int page,
			@Param("pageSize") int pageSize, @Param("csku") String csku);

	@Select({
			"<script>",
			"select count(*) from t_coupon_sku_relation where 1=1",
			" <if test=\"csku != null and csku != '' \"> and csku = #{csku} </if>",
			"</script>" })
	int getTotal(@Param("csku") String csku);

	@Insert("insert into t_coupon_sku_relation (iruleid,csku,isEnabled,ccreateuser) values(#{iruleid,jdbcType=INTEGER},#{csku,jdbcType=VARCHAR},#{isEnabled,jdbcType=BOOLEAN},#{ccreateuser,jdbcType=VARCHAR})")
	int addRelation(CouponSku couponSku);

	@Update("update t_coupon_sku_relation set isenabled = #{1},cupdateuser=#{2},dupdatedate=#{3} where iid = #{0}")
	int updateByStatus(Integer id, Boolean status, String user, Date date);

	@Delete("delete from t_coupon_sku_relation where iid = #{0}")
	int delById(Integer id);

	@Select("select * from t_coupon_sku_relation where csku = #{0} and isenabled = true")
	CouponSku getBySku(String sku);
	
	@Select("select iruleid from t_coupon_sku_relation where csku = #{0} and isenabled = true")
	Integer getRuleIdBySku(String sku);

	@Select({
		"<script>",
		"select * from t_coupon_sku_relation where ",
		"csku in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
		"and isenabled=true ",
		"</script>" })
	List<CouponSku> getCouponskuBySkus(@Param("list") List<String> skus); 

}
