package mapper.google.category;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.product.google.category.GoogleFeedsBase;
import dto.product.google.category.MerchantsProductDto;
import dto.product.google.category.SearchMerchantsProductDto;

public interface GoogleFeedsMapper {

	@Select("select * from t_google_feeds_base limit #{1} offset (#{0}-1)*#{1}")
	List<GoogleFeedsBase> getAllFeeds(int p, int pageSize,
			GoogleFeedsBase feedsBase);

	@Select("select count(*) from t_google_feeds_base")
	int getCount();

	@Insert("insert into t_google_feeds_base (country,clanguage,ccurrency,ilanguageid,ccreateuser) values (#{country,jdbcType=VARCHAR},#{clanguage,jdbcType=VARCHAR},#{ccurrency,jdbcType=VARCHAR},#{ilanguageid,jdbcType=INTEGER},#{ccreateuser,jdbcType=VARCHAR})")
	int addFeeds(GoogleFeedsBase feedsBase);

	@Delete("delete from t_google_feeds_base where iid = #{0}")
	int delFeedsById(int id);

	@Select("SELECT m.csku csku,b.icategory igooglecategoryid,d.ctitle ctitle,d.cdescription cdescription,d.ilanguageid ilanguage,  "+
			"m.iwebsiteid iwebsiteid,f.country ctargetcountry,f.ccurrency ccountrycurrency,  "+
			"f.clanguage clanguage,b.cpath googlecategory,b.cname cname "+
			" from t_google_category_base b ,t_google_product_category_mapper m , "+
			"t_google_product_detail d ,t_google_feeds_base f "+
			"where b.iid=m.icategory  "+
			"and m.csku=d.csku  "+
			"and d.ilanguageid=f.ilanguageid")
			List<MerchantsProductDto> pushProductMerchants();
			
			
			
	List<MerchantsProductDto> searchProductMerchants(@Param("page") int page, @Param("pageSize") int pageSize,
					@Param("merchantsProductDto") SearchMerchantsProductDto merchantsProductDto);

	int searchCountProductMerchants(@Param("merchantsProductDto") SearchMerchantsProductDto merchantsProductDto);
			
	List<GoogleFeedsBase> queryFeedData(@Param("ctargetcountry") String ctargetcountry, @Param("clanguage") String clanguage);
			
	List<SearchMerchantsProductDto> queryNoSkuData(@Param("searchMerchantsProductDto")  SearchMerchantsProductDto searchMerchantsProductDto);
}
