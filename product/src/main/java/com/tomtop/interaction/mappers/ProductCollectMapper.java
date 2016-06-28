package com.tomtop.interaction.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.CollectCountDto;
import com.tomtop.product.models.dto.ProductCollectDto;

public interface ProductCollectMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(ProductCollectDto record);

	int insertSelective(ProductCollectDto record);

	ProductCollectDto selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(ProductCollectDto record);

	int updateByPrimaryKey(ProductCollectDto record);

	@Delete("delete from t_product_collect where clistingid=#{0} and cemail=#{1}")
	int delCollect(String listingID, String email);

	@Delete({
			"<script>",
			"delete from t_product_collect ",
			"where cemail=#{1} and clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	int delCollectByListingids(@Param("list") List<String> listingIDs,
			String email);

	@Select("select iid,cemail,clistingid,dcreatedate from t_product_collect where clistingid=#{0} and cemail=#{1} LIMIT 1")
	ProductCollectDto getCollectByMember(String listingID, String email);

	@Select("select distinct c.clistingid from t_product_collect c where c.cemail=#{0}")
	List<String> getCollectListingIDByEmail(String email);

	@Select("select count(*) from t_product_collect where clistingid=#{0}")
	int getCountByListingID(String listingID);

	@Select({
			"<script>",
			"select c.* from t_product_collect c where c.cemail=#{0} ",
			"<if test=\"lists!=null and lists!=''\">",
			"and c.clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</if>",
			"<if test=\"sort!=null and sort=='date'\">",
			"order by c.dcreatedate desc ",
			"</if>",
			"<if test=\"sort!=null and sort!='date' and lists!=null and lists!=''\">",
			"order by position(c.clistingid in #{lists}) ", "</if>",
			"<if test=\"pg!=null\">", "limit #{2} offset #{pg}", "</if>",
			"</script>" })
	List<ProductCollectDto> getCollectsPageByEmail(String email,
			@Param("pg") Integer page, Integer pageSize,
			@Param("sort") String sort, @Param("list") List<String> listingids,
			@Param("lists") String listingids2);

	@Select({ "<script>",
			"select c.* from t_product_collect c where c.cemail=#{0} ",
			"<if test=\"sort!=null and sort=='date'\">",
			"order by c.dcreatedate desc ", "</if>", "</script>" })
	List<ProductCollectDto> getCollectsAllByEmail(String email,
			@Param("sort") String sort);

	@Select("select count(*) from t_product_collect where cemail=#{0}")
	int getCollectsCountByEmail(String email);

	@Select({
			"<script>",
			"select c.clistingid listingId,count(iid) collectCount ",
			"from t_product_collect c where c.clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"group by clistingid", "</script>" })
	public List<CollectCountDto> getCollectCountByListingIds(
			@Param("list") List<String> listingIds);
	
	@Select({"select pc.clistingid listingId,count(pc.clistingid) collectCount ",
		"from t_product_collect pc where pc.clistingid=#{listingId} group by pc.clistingid" })
	public CollectCountDto getCollectCountByListingId(
		@Param("listingId") String listingIds);
	
	@Select({
			"<script>",
			"select c.* ",
			"from t_product_collect c where c.clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	public List<ProductCollectDto> getCollectByListingIds(
			@Param("list") List<String> listingIds);

}