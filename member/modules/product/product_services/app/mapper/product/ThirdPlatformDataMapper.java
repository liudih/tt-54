package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.product.ThirdPlatformData;

public interface ThirdPlatformDataMapper {
	int addThirdPlatformData(ThirdPlatformData thirdPlatformData);

	ThirdPlatformData getThirdPlatformDataByIid(Integer iid);

	int deleteThirdPlatformDataByIid(Integer iid);

	int updateThirdPlatformDataByIid(ThirdPlatformData thirdPlatformData);

	int batchAddThirdPlatformData(List<ThirdPlatformData> list);

	@Select("select * from t_third_platform_data where cproductid = #{0} and cwebsite = #{1} and csku = #{2}")
	ThirdPlatformData getByProductIdAndWebsiteAndSku(String productId,
			String website, String sku);

	@Select("select cwebsite, cproductid, cdomain from t_third_platform_data "
			+ "where cplatform=#{0} and  csku=#{1} and istatus=1  order by isalesvolume desc")
	List<ThirdPlatformData> getDatasByPlatformAndSku(String tplatform,
			String sku);

	@Select("select * from t_third_platform_data "
			+ "where cplatform = #{0} and csku = #{1} and cwebsite = #{2}  order by isalesvolume desc")
	List<ThirdPlatformData> getDatasByPlatformAndSkuAndWebsite(
			String tplatform, String sku, String website);

}