package mapper.product;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.product.ProductParentUrl;

public interface ProductParentUrlMapper {

	@Insert("insert into t_product_url(cparentsku, ilanguageid, curl, ccreateuser, dcreatedate) "
			+ "values ( #{cparentsku,jdbcType=VARCHAR}, #{ilanguage,jdbcType=INTEGER}, "
			+ "#{curl,jdbcType=VARCHAR}, #{ccreateuser,jdbcType=VARCHAR}, #{dcreatedate,jdbcType=DATE})")
	int addProductParentUrl(ProductParentUrl record);

	@Select("select * from t_product_parent_url where curl = #{0} and ilanguageid = #{1} limit 1")
	ProductParentUrl getProductParentUrlByUrlAndLanguageId(String url,
			Integer languageId);
}