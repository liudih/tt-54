package mapper.advertising;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.mybatis.guice.transactional.Transactional;

import dto.advertising.AdvertisingType;

public interface AdvertisingTypeMapper {
	
	@Transactional
	int deleteByPrimaryKey(Long iid);

	@Transactional
	int insert(AdvertisingType record);

	AdvertisingType selectByPrimaryKey(Long iid);
	
    @Select("select * from t_advertising_type a order by iadvertisingid asc")
	List<AdvertisingType> getAdvertisingTypePage();
    
    @Select("select * from t_advertising_type a where iadvertisingid=#{0} limit 1")
    AdvertisingType validateKey(Long iadvertisingid);

	@Transactional
	int updateByPrimaryKey(AdvertisingType record);

 
}