package mapper.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.interaction.SuperDeal;

public interface SuperDealMapper {
	int deleteSuperDealById(Integer iid);

	int insert(SuperDeal record);

	int insertSelective(SuperDeal record);

	SuperDeal getSuperDealById(Integer iid);

	int updateByPrimaryKeySelective(SuperDeal record);

	int updateByPrimaryKey(SuperDeal record);

	@Delete("delete from t_super_deals where ccreateuser = 'Auto' ")
	void deleteOldAutoGetSuperDealsByCreateUser(String createUserName);

	@Select("<script>"
			+ "select * from t_super_deals where 1=1 and iwebsiteid = #{4}"
			+ "<if test=\"rootCategoryId !=-1 and rootCategoryId !=-2  and rootCategoryId !=-3 \"> and icategoryrootid=#{rootCategoryId} </if>"
			+ "<if test=\"rootCategoryId !=-1 and rootCategoryId ==-2 and rootCategoryId !=-3 \"> and ccreateuser <![CDATA[<>]]> 'Auto' </if>"
			+ "<if test=\"rootCategoryId !=-1 and rootCategoryId !=-2  and rootCategoryId ==-3 \"> and bshow = true </if>"
			+ "<if test=\"sku !=null and sku != '' \"> and csku like '%${sku}%'  </if>"
			+ " order by iid limit #{1} offset (#{0}-1)*#{1}" + "</script>")
	List<SuperDeal> getSuperDealByPageAndRootCategoryIdAndListingId(
			Integer page, Integer perPage,
			@Param("rootCategoryId") Integer rootCategoryId,
			@Param("sku") String sku, Integer websiteId);

	@Select("<script>"
			+ "select count(iid) from t_super_deals where 1=1 and iwebsiteid = #{2}"
			+ "<if test=\"rootCategoryId !=-1 and rootCategoryId !=-2 \"> and icategoryrootid=#{rootCategoryId} </if>"
			+ "<if test=\"rootCategoryId !=-1 and rootCategoryId ==-2 \"> and ccreateuser <![CDATA[<>]]> 'Auto' </if> "
			+ "<if test=\"sku !=null and sku != '' \"> and csku like '%${sku}%' </if>"
			+ "</script>")
	Integer getSuperDealCount(@Param("rootCategoryId") Integer rootCategoryId,
			@Param("sku") String sku, Integer websiteId);

	@Select("<script>"
			+ "select clistingid from t_super_deals where bshow=true and iwebsiteid = #{2}"
			+ "<if test=\"isAuto == true \">  and ccreateuser = 'Auto' </if> "
			+ "<if test=\"isAuto == false \">  and ccreateuser <![CDATA[<>]]> 'Auto' </if> "
			+ " order by dcreatedate desc limit #{1} </script>")
	List<String> getSuperDealListingIds(@Param("isAuto") boolean isAuto,
			Integer limit, Integer websiteId);

	@Update("update t_super_deals set bshow = #{1} where iid=#{0} ")
	Boolean updateSuperDealBshow(Integer id, Boolean bshow);

	@Select("select clistingid from t_super_deals where iwebsiteid = #{0}")
	List<String> getAllSuperDealsListingIds(Integer websiteId);

	@Select("select clistingid from t_super_deals where iwebsiteid = #{0} "
			+ "offset (#{1} - 1)* #{2} ")
	List<String> getListingIds(Integer websiteId, Integer pageNum,
			Integer pageSize);

	@Select("select clistingid from t_super_deals where iwebsiteid = #{siteId} and icategoryrootid = #{rootCategoryId} limit #{pageSize} offset #{startIndex}")
	List<String> getSDPageBySiteId(@Param("siteId")int siteId, @Param("rootCategoryId")Integer rootCategoryId, @Param("pageSize")Integer pageSize, @Param("startIndex")int startIndex);

	@Select("select count(*) from t_super_deals where iwebsiteid = #{siteId} and icategoryrootid = #{rootCategoryId}")
	int getSDCountBySiteId(@Param("siteId")int siteId, @Param("rootCategoryId")Integer rootCategoryId);
}