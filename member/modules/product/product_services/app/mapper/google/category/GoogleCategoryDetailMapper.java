package mapper.google.category;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.image.ImgUseMapping;
import dto.product.google.category.GoogleCategoryDetail;

public interface GoogleCategoryDetailMapper {

	@Select("select * from t_google_product_detail where icategory = #{0}")
	List<GoogleCategoryDetail> getDetailByCid(int cid);
	
	@Insert("insert into t_google_product_detail (csku,ilanguageid,ctitle,cdescription,ccreateuser) values(#{0},#{1},#{2},#{3},#{4})")
	int addDetail(String csku,int ilanguage,String title,String description,String user);

	@Select("select * from t_google_product_detail where csku = #{0}")
	List<GoogleCategoryDetail> getDetails(String sku);
	
	@Select("select * from t_google_product_detail where csku = #{0} and ilanguageid = #{1} ")
	List<GoogleCategoryDetail> selectDetailBySkuAndLanguage(String csku,
			Integer ilanguageid);

	@Update("update t_google_product_detail set ctitle = #{2} , cdescription = #{3},cupdateuser=#{4},dupdatedate=#{5} where csku = #{0} and ilanguageid = #{1} ")
	int updateDetail(String csku, Integer ilanguageid, String ctitle,
			String cdescription, String user,Date date);

	@Delete("delete from t_google_product_detail where csku = #{0} ")
	int deleteBySkuAndCid(String csku);
	
	@Delete({
		"<script>",
		"delete from t_google_product_detail where csku in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
		"</script>" })
	int  deleteBySkuList(@Param("list") List<String> skuList);

}
