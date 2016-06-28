package mapper.advertising;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.mybatis.guice.transactional.Transactional;

import dto.advertising.AdvertisingPosition;

public interface AdvertisingPositionMapper {

	@Transactional
	int deleteByPrimaryKey(Long iid);

	@Transactional
	int insert(AdvertisingPosition record);

	AdvertisingPosition selectByPrimaryKey(Long iid);

	@Select("select * from t_advertising_positon a order by ipositonid asc")
	List<AdvertisingPosition> getAdvertisingPositionPage();

	@Select("select * from t_advertising_positon a where ipositonid=#{0} limit 1")
	AdvertisingPosition validateKey(Long iadvertisingid);

	@Transactional
	int updateByPrimaryKey(AdvertisingPosition record);

}