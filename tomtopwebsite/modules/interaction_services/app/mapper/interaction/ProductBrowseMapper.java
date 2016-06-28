package mapper.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.TopBrowseAndSaleCount;
import dto.interaction.ListingCount;
import dto.interaction.ProductBrowse;

public interface ProductBrowseMapper {

	@Insert("INSERT INTO t_interaction_product_browse "
			+ "(iplatformid, iwebsiteid, imemberid, cltc, cstc, clistingid, csku, dcreatedate) "
			+ "VALUES "
			+ "(#{iplatformid}, #{iwebsiteid}, #{imemberid}, #{cltc}, #{cstc}, #{clistingid}, #{csku}, #{dcreatedate})")
	int insert(ProductBrowse record);

	@Select("SELECT MAX(dcreatedate) dcreatedate, iplatformid, iwebsiteid, imemberid, clistingid, csku "
			+ "FROM t_interaction_product_browse b "
			+ "WHERE imemberid = #{0} AND iwebsiteid = #{1} "
			+ "GROUP BY iplatformid, iwebsiteid, imemberid, clistingid, csku "
			+ "ORDER BY dcreatedate DESC LIMIT #{2}")
	List<ProductBrowse> findByMemberID(int memberID, int siteID, int limit);

	@Select("SELECT MAX(dcreatedate) dcreatedate, iplatformid, iwebsiteid, clistingid, csku, cltc, cstc "
			+ "FROM t_interaction_product_browse b "
			+ "WHERE cltc = #{0} AND iwebsiteid = #{1} AND imemberid IS NULL "
			+ "GROUP BY iplatformid, iwebsiteid, clistingid, csku, cltc, cstc "
			+ "ORDER BY dcreatedate DESC LIMIT #{2}")
	List<ProductBrowse> findByLTC(String ltc, int siteID, int limit);

	@Select({
			"<script>",
			"select clistingid,count(clistingid) from t_interaction_product_browse ",
			"where clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"group by clistingid order by count(1) desc limit 100 ",
			"</script>" })
	List<ListingCount> getTop100Browse(@Param("list") List<String> listingIds);

	@Select("select clistingid from t_interaction_product_browse where clistingid!=#{0} and cltc in (select cltc from t_interaction_product_browse where clistingid=#{0}) group by clistingid order by count(1) desc limit 10")
	List<String> getBrowseListingsByListing(String listind);

	@Select("select clistingid from t_interaction_product_browse where iwebsiteid =#{0} and cltc=#{1} order by dcreatedate desc limit 1 ")
	String getLastBrowseListingIdBySiteIdAndLtc(int siteId, String ltc);

	@Select("select distinct cltc from t_interaction_product_browse where clistingid=#{0}")
	List<String> getShowCltcByListing(String listing);

	@Select({
			"<script>",
			"select clistingid,count(1) from t_interaction_product_browse where cltc in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"group by clistingid order by count(1) desc limit #{1} ",
			"</script>" })
	List<ListingCount> getListingAndCountByCltcs(
			@Param("list") List<String> cltcs, int n);

	@Select({
			"<script> select csku as sku,clistingid as listingid,count(clistingid) as browsecount from t_interaction_product_browse ",
			" where clistingid in ",
			" <foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			" and now() between dcreatedate and dcreatedate + interval '${timeRange} day' ",
			" group by clistingid,csku order by count(clistingid) desc ",
			"</script>" })
	List<TopBrowseAndSaleCount> getTopBrowseByTimeRange(
			@Param("timeRange") Integer timeRange,
			@Param("list") List<String> listingIds);

}