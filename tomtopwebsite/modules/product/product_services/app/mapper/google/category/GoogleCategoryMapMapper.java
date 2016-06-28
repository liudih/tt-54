package mapper.google.category;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.product.google.category.GoogleCategoryMapper;

public interface GoogleCategoryMapMapper {

	@Insert("insert into t_google_product_category_mapper (csku,iwebsiteid,icategory,ccreateuser) values (#{0},#{1},#{2},#{3})")
	int add(String sku, Integer websiteid, Integer icategory, String createuser);

	@Select("select * from t_google_product_category_mapper where icategory=#{0}")
	List<GoogleCategoryMapper> getSkuByCid(int cid);

	@Delete("delete from t_google_product_category_mapper where iid=#{0}")
	int deleteByIid(Integer iid);

	@Select({
			"<script> ",
			" select * from t_google_product_category_mapper where icategory = #{icategory}  ",
			" <if test=\"csku != null and csku != '' \"> and csku= #{csku} </if>",
			" limit #{pageSize} offset (#{page}-1)*#{pageSize} ", "</script>" })
	List<GoogleCategoryMapper> getSkuFilter(@Param("page") int page, @Param("pageSize")int pageSize, @Param("icategory") int icategory,
			@Param("csku") String csku);
	
	@Select({"<script>",
			"select count(*) from t_google_product_category_mapper where icategory = #{icategory}",
			" <if test=\"csku != null and csku != '' \"> and csku = #{csku} </if>",
			"</script>"} )
	int getTotal(@Param("icategory")int icategory,@Param("csku") String csku);
	
	
	@Delete({
		"<script>",
		"delete from t_google_product_category_mapper where csku in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
		"</script>" })
	int deleteBySkuList(@Param("list") List<String> cskuList);

	@Select("select csku from t_google_product_category_mapper where icategory=#{0}")
	List<String> getSkusByCid(int cid);
}
