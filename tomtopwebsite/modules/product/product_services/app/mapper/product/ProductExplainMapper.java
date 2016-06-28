package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.product.ProductExplain;

public interface ProductExplainMapper {
	@Select("select ccontent from t_all_product_explain where iwebsiteid=#{0} and ilanguageid=#{1} and ctype=#{2} limit 1")
	public String getContentForSiteAndLanAndType(int site, int lan, String type);

	@Select("select * from t_all_product_explain where iwebsiteid=#{0} and ilanguageid=#{1}")
	public List<ProductExplain> getProductExplainsBySiteAndLan(int site, int lan);

	int updateProductExplain(ProductExplain productExplain);

	int addProductExplain(ProductExplain productExplain);

	int deleteByIid(Integer iid);

	@Select("select * from t_all_product_explain where iwebsiteid = #{0} and ilanguageid = #{1} and ctype = #{2}")
	public ProductExplain getProductExplainsBySiteIdAndLanIdAndType(int site,
			int lan, String type);
}
