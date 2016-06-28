package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.product.ProductBase;
import dto.product.google.category.MerchantsProductDto;
import dto.product.google.category.SearchMerchantsProductDto;

public interface MerchantsProductMapper {
	
	void addMerchantsProductListing(@Param("list") List<MerchantsProductDto> List);

	void updateMerchantsProductListing(@Param("list") List<MerchantsProductDto> List);
	
	List<MerchantsProductDto> existMerchantsProductListing(@Param("list") List<String> list);
	
	List<MerchantsProductDto> existMerchantsProductByListingIdList(@Param("list") List<String> list);
	
	List<MerchantsProductDto> getMerchantsProductListing(@Param("page") int page, @Param("pageSize")int pageSize);
	
	@Select("SELECT  count(1)from t_product_category_mapper m  "+
	"INNER JOIN t_product_base b on m.clistingid =b.clistingid and b.istatus=1 and b.iwebsiteid=1 "+
	"and m.icategory=#{0}")
	int autoPublishGoogleProductCount(int categoryId);
	
	@Select("SELECT  DISTINCT b.clistingid,b.csku,b.iqty,b.fprice,b.ffreight,b.istatus,b.iwebsiteid from t_product_category_mapper m "
	+ "INNER JOIN t_product_base b on m.clistingid =b.clistingid and b.istatus=1 and b.iwebsiteid=1 "+
	"and m.icategory=#{0} "+
	"and not EXISTS(SELECT p.clistingid from  t_merchants_product p where  p.clistingid=b.clistingid"+
						"	and p.cstate   in('1','2','3')	"+
						"	and p.cresult ='success' ) "+
						"ORDER BY b.clistingid  LIMIT #{2} offset (#{1}-1)*#{2} ")
	
	List<ProductBase> autoPublishGoogleProductByCategorys(int categoryId,int page,int pageSize);
	
	
	@Select(" SELECT p.cnodeid,p.cnodedata,p.cstate,p.cresult,p.cfaultreason,p.ccountrycurrency,p.productprice,p.icount,"+
			"p.dpulldate ,p.dpushdate,p.dcreatedate,p.dupdatedate from t_merchants_product p "+
			" where p.cnodeid= #{0}")
	MerchantsProductDto queryProductMerchants(String nodeId);
	
	List<SearchMerchantsProductDto> queryExistMerchantProduct(@Param("list") List<String> List);
	
	List<SearchMerchantsProductDto> searchMerchantProductConfigData(@Param("searchDto") SearchMerchantsProductDto searchMerchantsProductDto);
	
	int countMerchantProductConfigData(@Param("searchDto") SearchMerchantsProductDto searchMerchantsProductDto);
	
	List<MerchantsProductDto> pushMerchantProductConfigData(@Param("list") List<String> list);
	
	List<MerchantsProductDto> queryMerchantProductConfigData(@Param("list") List<String> list);

	
	List<SearchMerchantsProductDto> queryNoMerchantProductLimit(@Param("csku")  String csku,
			@Param("clanguage")  String clanguage,@Param("ccountrycurrency")  String ccountrycurrency);
	
	List<SearchMerchantsProductDto> queryNoSkuData(@Param("clanguage") String clanguage,@Param("ctargetcountry") String ctargetcountry,
			@Param("pageSize") int pageSize,@Param("page") int page);
	
	@Delete({
		"<script>",
		"delete from t_merchants_product where iid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
		"</script>" })
	int deleteGoogleBackDataByIds(@Param("list") List<Integer> ids);
	
	List<MerchantsProductDto> getMerchantsProductByIds(@Param("list") List<Integer> ids);

	List<MerchantsProductDto> getMerchantsByLangeAndCountry(@Param("clanguage") String clanguage,@Param("ctargetcountry") String ctargetcountry,
			@Param("page") int page, @Param("pageSize")int pageSize);
	
	int getMerchantsCountByLangeAndCountry(@Param("clanguage") String clanguage,@Param("ctargetcountry") String ctargetcountry);

	List<MerchantsProductDto> pushMerchantsByLangeAndCountry( @Param("clanguage") String clanguage,@Param("ctargetcountry") String ctargetcountry,
			@Param("page") int page, @Param("pageSize")int pageSize);
	
	int  pushMerchantsCountByLangeAndCountry(@Param("clanguage") String clanguage,@Param("ctargetcountry") String ctargetcountry);

	@Delete({
		"<script>",
		"delete from t_merchants_product where cnodeid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
		"</script>" })
	int deleteGoogleBackDataByNodeIdList(@Param("list") List<String> ids);
	
	@Delete("delete from t_merchants_product" )
	int deleteGoogleBack();
	
	int deleteCountBackAndGoogleProduct(@Param("bactivity") Boolean bactivity,@Param("iwebsiteid") Integer iwebsiteid,
			@Param("istatus") Integer istatus);
	
	List<MerchantsProductDto> deleteBackAndGoogleProduct(@Param("bactivity") Boolean bactivity,@Param("iwebsiteid") Integer iwebsiteid,
			@Param("istatus") Integer istatus,@Param("page") int page, @Param("pageSize")int pageSize);
}
