package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import valueobjects.product.ProductBaseTranslate;

public interface ProductBaseTranslateMapper {
	@Select("select tbase.clistingid,tbase.bvisible,tbase.cparentsku,tbase.iwebsiteid,translate.ilanguageid,tbase.csku,tbase.dnewtodate "
			+ ",tbase.fprice,tbase.fweight,tbase.dcreatedate,tbase.istatus,translate.ctitle "
			+ " FROM t_product_base tbase inner join t_product_translate translate on tbase.clistingid=translate.clistingid where tbase.bactivity=false")
	List<ProductBaseTranslate> getAllTranslate();

	@Select("select tbase.clistingid,tbase.bvisible,tbase.cparentsku,tbase.iwebsiteid,translate.ilanguageid,tbase.csku,tbase.dnewtodate "
			+ ",tbase.fprice,tbase.fweight,tbase.dcreatedate,tbase.istatus,translate.ctitle "
			+ " FROM t_product_base tbase inner join t_product_translate translate on tbase.clistingid=translate.clistingid "
			+ " where tbase.bactivity=false and tbase.iwebsiteid=#{2} "
			+ "order by tbase.clistingid,tbase.iwebsiteid,translate.ilanguageid "
			+ "limit #{1} offset #{0}")
	List<ProductBaseTranslate> getPagedAllTranslate(int offset, int limit,int websiteId);

	@Select("select count(*)"
			+ " FROM t_product_base tbase inner join t_product_translate translate on tbase.clistingid=translate.clistingid where tbase.bactivity=false and iwebsiteid=#{0} ")
	int getAllTranslateCount(Integer websiteId);

	@Select("<script>select tbase.clistingid,tbase.bvisible,tbase.cparentsku,tbase.iwebsiteid,translate.ilanguageid,tbase.csku,tbase.dnewtodate "
			+ ",tbase.fprice,tbase.fweight,tbase.dcreatedate,tbase.istatus,translate.ctitle "
			+ " FROM t_product_base tbase inner join t_product_translate translate on tbase.clistingid=translate.clistingid "
			+ "WHERE tbase.bactivity=false and tbase.clistingid IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "order by tbase.clistingid,tbase.iwebsiteid,translate.ilanguageid "
			+ "limit #{2} offset #{1}</script>")
	List<ProductBaseTranslate> getPagedTranslate(
			@Param("list") List<String> listingIds, int offset, int pageSize);

	@Select("<script>SELECT count(*)"
			+ " FROM t_product_base tbase inner join t_product_translate translate on tbase.clistingid=translate.clistingid "
			+ "WHERE tbase.bactivity=false and tbase.clistingid IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach>"
			+ "</script>")
	int getTranslateCount(@Param("list") List<String> listingIds);

	@Select("select tbase.clistingid,tbase.bvisible,tbase.cparentsku,tbase.iwebsiteid,translate.ilanguageid,tbase.csku,tbase.dnewtodate "
			+ ",tbase.fprice,tbase.fweight,tbase.dcreatedate,tbase.istatus,translate.ctitle "
			+ " FROM t_product_base tbase inner join t_product_translate"
			+ " translate on tbase.clistingid=translate.clistingid "
			+ " where tbase.bactivity=false and tbase.clistingid=#{0} ")
	List<ProductBaseTranslate> getTranslateByListingid(String clistingid);

	@Select("<script>select tbase.bvisible,tbase.bmultiattribute,tbase.iqty,tbase.istatus,tbase.clistingid,tbase.cparentsku,tbase.iwebsiteid,translate.ilanguageid,tbase.csku,tbase.dnewtodate "
			+ ",tbase.fprice,tbase.fweight,tbase.dcreatedate,translate.ctitle,translate.cdescription,translate.cshortdescription,translate.ckeyword "
			+ ",cate.icategory "
			+ " FROM t_product_base tbase inner join t_product_translate"
			+ " translate on tbase.clistingid=translate.clistingid "
			+ " inner join (select clistingid,max(icategory) as icategory from t_product_category_mapper where icategory in (select iid from t_category_website where ilevel=3) group by clistingid) cate on cate.clistingid=tbase.clistingid "
			+ " where translate.ilanguageid = #{languageId} and tbase.bactivity=false and tbase.clistingid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> </script>")
	List<ProductBaseTranslate> getTranslateByListingIds(
			@Param("list") List<String> list, @Param("languageId") Integer lang);

	@Select("<script>select b.cparentsku,b.bvisible, b.csku, b.iqty, b.istatus, b.clistingid, b.fprice, b.dcreatedate, "
			+ "b.iwebsiteid, t.ilanguageid, t.ctitle from t_product_base b inner join t_product_translate t on b.clistingid = t.clistingid "
			+ "where t.ilanguageid = #{languageId} and b.bactivity=false and b.clistingid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach></script>")
	List<ProductBaseTranslate> getTranslateLiteByListingIds(
			@Param("list") List<String> list, @Param("languageId") Integer lang);
}
