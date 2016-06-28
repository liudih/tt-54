package mapper.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.interaction.ProductCollect;
import valueobjects.interaction.CollectCount;

public interface ProductCollectMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(ProductCollect record);

    int insertSelective(ProductCollect record);

    ProductCollect selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(ProductCollect record);

    int updateByPrimaryKey(ProductCollect record);
    
    @Delete("delete from t_product_collect where clistingid=#{0} and cemail=#{1}")
    int delCollect(String listingID, String email);
    
    @Delete({
		"<script>",
		"delete from t_product_collect ",
		"where cemail=#{1} and clistingid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
		"</script>" })
    int delCollectByListingids(@Param("list") List<String> listingIDs, String email);
    
    @Select("select c.* from t_product_collect c where c.clistingid=#{0} and c.cemail=#{1}")
    List<ProductCollect> getCollectByMember(String listingID, String email);

    @Select("select distinct c.clistingid from t_product_collect c where c.cemail=#{1}")
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
	  	"order by position(c.clistingid in #{lists}) ",
	  	"</if>","<if test=\"pg!=null\">",
	  	"limit #{2} offset #{pg}","</if>",
		"</script>" })
    List<ProductCollect> getCollectsPageByEmail(String email,@Param("pg")Integer page,
    		Integer pageSize,@Param("sort") String sort,
    		@Param("list") List<String> listingids,@Param("lists") String listingids2);
	
	@Select({
		"<script>",
		"select c.* from t_product_collect c where c.cemail=#{0} ",
		"<if test=\"sort!=null and sort=='date'\">",
	  	"order by c.dcreatedate desc ",
	  	"</if>",
		"</script>" })
	List<ProductCollect> getCollectsAllByEmail(String email,@Param("sort") String sort);
    
    @Select("select count(*) from t_product_collect where cemail=#{0}")
    int getCollectsCountByEmail(String email);
    
    @Select({
		"<script>",
		"select c.clistingid listingId,count(iid) collectCount ",
		"from t_product_collect c where c.clistingid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
	  	"group by clistingid",
		"</script>" })
    public List<CollectCount> getCollectCountByListingIds(@Param("list") List<String> listingIds);
    
    @Select({
		"<script>",
		"select c.* ",
		"from t_product_collect c where c.clistingid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
		"</script>" })
    public List<ProductCollect> getCollectByListingIds(@Param("list") List<String> listingIds);
    
}