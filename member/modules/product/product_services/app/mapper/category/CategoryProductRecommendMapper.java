package mapper.category;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.category.CategoryProductRecommend;

public interface CategoryProductRecommendMapper {

	@Insert("insert into t_category_product_recommend (categoryid, categoryname, categoryfullname, parentid, parentname,level,sku,status,createdate,sequence,createby,iwebsiteid,cdevice,upddate) "
			+ "values (#{categoryid}, #{categoryname}, #{categoryfullname}, #{parentid}, #{parentname},#{level},#{sku},#{status},now(),#{sequence},#{createby},#{iwebsiteid},#{cdevice},now())")
	int insert(CategoryProductRecommend cpr);

	@Select("<script>select iid,categoryid,categoryname,categoryfullname,parentid,parentname,"
			+ "level,sku,status,createdate,sequence,iwebsiteid,cdevice from t_category_product_recommend "
			+ "where status=#{status} <if test=\"parentid != null\"> and parentid = #{parentid} and categoryid=#{categoryid} </if>"
			+ " <if test=\"sku != null\"> and sku = #{sku} </if> "
			+ " <if test=\"createdate != null\"> and to_char(createdate,'YYYYMMDD') = #{createdate} </if> "
			+ " <if test=\"siteid != null\"> and iwebsiteid = #{siteid} </if> <if test=\"device != null\"> and cdevice = #{device} </if>"
			+ "order by sequence asc</script>")
	List<CategoryProductRecommend> search(@Param("status") Integer status,
			@Param("parentid") Integer parentid,
			@Param("categoryid") Integer categoryid, @Param("sku") String sku,
			@Param("createdate") String createdate,
			@Param("siteid") Integer siteid, @Param("device") String device);

	@Update("update t_category_product_recommend set status=#{status},updateby=#{updateby},upddate=now() where iid=#{id}")
	int updateStatus(@Param("id") Integer id, @Param("status") Integer status,
			@Param("updateby") String updateby);

	@Update("update t_category_product_recommend set sequence=sequence+1,upddate=now() "
			+ "where categoryid=#{categoryid} and sequence>=#{sequence} and "
			+ "iwebsiteid=#{siteid} and cdevice=#{device} and status=1")
	int updateSequence(@Param("categoryid") Integer categoryid,
			@Param("sequence") Integer sequence,
			@Param("siteid") Integer siteid, @Param("device") String device);

	@Update("update t_category_product_recommend set sequence=sequence-1,upddate=now() "
			+ "where categoryid=#{categoryid} and sequence>#{sequence} and "
			+ "iwebsiteid=#{siteid} and cdevice=#{device} and status=1")
	int updateDeleteSequence(@Param("categoryid") Integer categoryid,
			@Param("sequence") Integer sequence,
			@Param("siteid") Integer siteid, @Param("device") String device);

	@Update("update t_category_product_recommend set sequence=sequence+1,upddate=now() "
			+ "where categoryid=#{categoryid} and sequence<=#{sequence} and "
			+ "iwebsiteid=#{siteid} and cdevice=#{device} and status=1")
	int updateSequenceOnTop(@Param("categoryid") Integer categoryid,
			@Param("sequence") Integer sequence,
			@Param("siteid") Integer siteid, @Param("device") String device);

	@Update("update t_category_product_recommend set sequence=1,updateby=#{updateby},upddate=now() where iid=#{id} and status=1")
	int updateOnTop(@Param("id") Integer id, @Param("updateby") String updateby);

	@Select("select max(sequence) from t_category_product_recommend where categoryid=#{categoryid} and iwebsiteid=#{siteid} and cdevice=#{device} and status=1")
	Integer searchParentSequence(@Param("categoryid") Integer categoryid,
			@Param("siteid") Integer siteid, @Param("device") String device);

	@Select("select clistingid from t_product_category_mapper where csku=#{sku} and icategory=#{categoryid} limit 1")
	String searchProductCategoryBySku(@Param("sku") String sku,
			@Param("categoryid") Integer categoryid);

	@Select("select count(sku) from t_category_product_recommend where sku=#{sku} and categoryid=#{categoryid} "
			+ "and iwebsiteid=#{siteid} and cdevice=#{device} and status=1")
	int searchCategoryRecommendBySku(@Param("sku") String sku,
			@Param("categoryid") Integer categoryid,
			@Param("siteid") Integer siteid, @Param("device") String device);

	@Select({
			"<script>",
			"select * from t_category_product_recommend ",
			"where sku in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"and status=1 ", "</script>" })
	List<CategoryProductRecommend> getProductRecommendBySkus(
			@Param("list") List<String> skus);

	@Select("select iid,categoryid,categoryname,categoryfullname,parentid,parentname,"
			+ "level,sku,status,createdate,sequence,iwebsiteid,cdevice from t_category_product_recommend "
			+ "where iid=#{id}")
	CategoryProductRecommend searchCategoryRecommendById(@Param("id") Integer id);

	@Select("select pb.clistingid from t_product_base pb where pb.istatus=1 and "
			+ "exists(select cpr.sku from t_category_product_recommend cpr "
			+ "where cpr.sku=pb.csku and cpr.iwebsiteid=pb.iwebsiteid )")
	List<String> searchRecommendListingidAll();

	@Select("select pb.clistingid from t_product_base pb "
			+ "where pb.istatus=1 and exists(select cpr.sku from t_category_product_recommend cpr "
			+ "where cpr.sku=pb.csku and cpr.iwebsiteid=pb.iwebsiteid "
			+ "and to_char(cpr.upddate,'YYYYMMDD')>=to_char(CURRENT_DATE-1,'YYYYMMDD'))")
	List<String> searchRecommendListingidByUpddate();
}
