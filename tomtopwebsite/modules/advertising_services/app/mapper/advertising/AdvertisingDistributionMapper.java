package mapper.advertising;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.mybatis.guice.transactional.Transactional;

import dto.advertising.Advertising;
import dto.advertising.AdvertisingDistribution;
import dto.advertising.AdvertisingProductDetailLite;

public interface AdvertisingDistributionMapper {

	@Transactional
	int deleteByPrimaryKey(Long iid);

	@Transactional
	int deleteRelationById(Long iid);

	@Transactional
	int insert(AdvertisingDistribution record);

	@Transactional
	int insertSelective(AdvertisingDistribution record);

	int updateByPrimaryKey(AdvertisingDistribution record);

	@Select("select cbusinessid,iwebsiteid from t_advertising_distribution m "
			+ "inner join t_advertising_base a on m.iadvertisingid=a.iid "
			+ " where cbusinessid=#{businessid} ")
	@ResultMap("productadResultMap")
	List<AdvertisingProductDetailLite> getProductAdvertising(
			String businessid);

	@Select("select a.*,m.iwebsiteid, "
			+ "(select t.cadvertisingname from  t_advertising_type t where a.itype=t.iid) as advertisingtypename, "
			+ "(select p.cpositonname from t_advertising_positon p where a.iposition=p.ipositonid) as positionname "
			+ "  from t_advertising_base a inner join t_advertising_distribution m on m.iadvertisingid=a.iid ")
	List<Advertising> getAdvertisingAll();

	@Select("select * from ("
			+ "select a.*, "
			+ "(select p.cpositonname from t_advertising_positon p where a.iposition=p.ipositonid) as positionname "
			+ "  from t_advertising_base a  "
			+ ") a order by iid desc limit #{1} offset (#{0}-1)*#{1}")
	List<Advertising> getAdvertisingPage(int page, int pageSize);

	List<Advertising> searchAdvertisingPage(Map<String, Object> param);

	Integer searchAdvertisingCount(Map<String, Object> param);

	@Select("select count(iid) from t_advertising_base a ")
	int getAdvertisingCount();

	@Select("select a.*,m.ctitle,m.chrefurl,m.ilanguageid,m.cbgimageurl,m.cbgcolor,m.iindex,m.bstatus,m.bbgimgtile"
			+ "  from t_advertising_base a inner join t_advertising_content m on m.iadvertisingid=a.iid "
			+ "  where a.iid = #{0} LIMIT 1")
	Advertising selectById(Long iid);

	@Select("select * from t_advertising_distribution m "
			+ " where m.iadvertisingid=#{iadvertisingid} ")
	AdvertisingDistribution getProductAdvertisingById(Long iadvertisingid);

	@Transactional
	int updateByPrimaryKeySelective(AdvertisingDistribution record);

	@Select("select m.*, "
			+ "(select t.cadvertisingname from  t_advertising_type t where m.itype=t.iadvertisingid) as advertisingtypename "
			+ "  from t_advertising_distribution m where m.iadvertisingid = #{0} ")
	List<AdvertisingDistribution> getAdvertRelationList(Long advertisingId);

}