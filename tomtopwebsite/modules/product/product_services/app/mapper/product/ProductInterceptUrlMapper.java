package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.product.ProductInterceptUrl;

public interface ProductInterceptUrlMapper {

	@Insert("insert into t_product_intercept_url(iwebsiteid,clistingid,csku,ilanguageid,curl) values (#{iwebsiteid},#{clistingid},#{csku},#{ilanguageid},#{curl})")
	int addProduct(ProductInterceptUrl interceptUrl);
	
	@Select("select clistingid, csku from t_product_intercept_url where curl = #{url} and ilanguageid=#{languageid} limit 1")
	ProductInterceptUrl getProductInterceptUrlByUrl(String url,int languageid);
	
	@Select("select * from t_product_intercept_url ")
	List<ProductInterceptUrl> getAllListingid();
	
	@Select({
		"<script>",
		"select url.* from t_product_intercept_url url ",
		"where url.clistingid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
		"</script>" })
	List<ProductInterceptUrl> getUrlByListingids(@Param("list") List<String> clistingid);
	
	@Select("select curl from t_product_intercept_url where ilanguageid = #{0} and clistingid = #{1} limit 1")
	ProductInterceptUrl getUrlByLanuageidAndListingid(int language,String listingid);
	
	@Select("select * from t_product_intercept_url where csku = #{0} and ilanguageid = #{1} limit 1")
	ProductInterceptUrl getProductBySkuAndLanguage(String sku, int languageid);

}
