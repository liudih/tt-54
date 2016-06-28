package mappers.tracking;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.tracking.AffiliateInfo;

public interface AffiliateInfoMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(AffiliateInfo record);

	int insertSelective(AffiliateInfo record);

	AffiliateInfo selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(AffiliateInfo record);

	int updateByPrimaryKey(AffiliateInfo record);

	@Select({ "<script>", "select * from t_affiliate_info where 1 = 1",
			"<if test=\"pp!=null and pp!='' \">", " and cpaypalemail = #{pp}",
			"</if>", "<if test=\"web!=null and web!='' \">",
			" and iwebsiteid = #{web}", "</if>",
			"<if test=\"saler!=null and saler!=''\">", " and cemail =#{saler}",
			"</if>", "<if test=\"aid!=null and aid!=''\">",
			" and caid = #{aid}", "</if>", "<if test=\"no!=null\">",
			" and breceivenotification = #{no}", "</if>",
			"<if test=\"st!=null\">", " and bstatus = #{st}", "</if>",
			"<if test=\"website!=null and website != 0\">", " and iwebsiteid = #{website}", "</if>",
			" order by dcreatedate desc", "<if test=\"pg!=-1\">",
			" limit #{1} offset #{pg}", "</if>", "</script>" })
	List<AffiliateInfo> getAffiliateInfoPage(@Param("pg") int page,
			int pageSize, @Param("saler") String saler,
			@Param("aid") String aid, @Param("pp") String pp,
			@Param("web") Integer web, @Param("no") Boolean notice,
			@Param("st") Boolean status, @Param("website") Integer website);

	@Select({ "<script>", "select count(*) from t_affiliate_info where 1 = 1",
			"<if test=\"pp!=null and pp!='' \">", " and cpaypalemail = #{pp}",
			"</if>", "<if test=\"web!=null and web!='' \">",
			" and iwebsiteid = #{web}", "</if>",
			"<if test=\"saler!='' and saler!=null\">", " and cemail =#{saler}",
			"</if>", "<if test=\"aid!=null and aid!=''\">",
			" and caid = #{aid}", "</if>", "<if test=\"no!=null\">",
			" and breceivenotification = #{no}", "</if>",
			"<if test=\"st!=null\">", " and bstatus = #{st}", "</if>",
			"</script>" })
	int getAffiliateInfoCount(@Param("saler") String saler,
			@Param("aid") String aid, @Param("pp") String pp,
			@Param("web") Integer web, @Param("no") Boolean notice,
			@Param("st") Boolean status);

	@Select("select * from t_affiliate_info where cemail=#{0} or caid=#{1}")
	List<AffiliateInfo> getAffiliateInfoByEmailAndAid(String email, String aid);

	@Select("select * from t_affiliate_info where cemail=#{0}")
	List<AffiliateInfo> getAffiliateInfoByEmail(String email);

	int deleteByAid(String aid);

	@Select("select * from t_affiliate_info where caid =#{0} limit 1")
	AffiliateInfo getAffiliateInfoByAid(String aid);
	
	@Select("select * from t_affiliate_info where caid =#{0} and iwebsiteid = #{1} limit 1")
	AffiliateInfo getAffiliateInfoByAidAndSite(String aid, Integer website);

	@Select("select * from t_affiliate_info where cemail =#{0} limit 1")
	AffiliateInfo isNotExist(String email);
	
	@Select("select * from t_affiliate_info where cemail =#{0} and iwebsiteid = #{1} limit 1")
	AffiliateInfo getAffiliateByEmailAndSite(String email, Integer website);

	@Select("select cemail from t_affiliate_info where caid =#{0} ")
	String getEmail(String aid);

	@Select({
			"<script>",
			"select * from t_affiliate_info where caid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<AffiliateInfo> getAffiliateInfoByAids(@Param("list") List<String> aids);

	@Select("select caid from t_affiliate_info where cemail =#{0} limit 1")
	String getAidByEmail(String email);

	@Select({ "<script>",
			"select * from t_affiliate_info where isalerid is not null",
			"</script>" })
	List<AffiliateInfo> getAffiliateInfoForAdmin();

	@Select({
			"<script>",
			"select * from t_affiliate_info where isalerid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<AffiliateInfo> getInfoBySalerIds(@Param("list") List<Integer> salerids);

	@Select("select * from t_affiliate_info where isalerid =#{0} and caid=#{1} ")
	List<AffiliateInfo> getInfoBySalerIdAndAid(int salerid, String aid);

	@Select({
		"<script>","select * from t_affiliate_info where isalerid =#{0} ",
		"<if test=\"website!=null and website != 0\">", " and iwebsiteid = #{website}", "</if>",
		"</script>"})
	List<AffiliateInfo> getInfoBySalerId(int salerid,@Param("website")Integer website);

}