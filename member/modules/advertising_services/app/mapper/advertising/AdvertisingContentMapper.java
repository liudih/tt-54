package mapper.advertising;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.mybatis.guice.transactional.Transactional;

import dto.advertising.AdvertisingContent;

public interface AdvertisingContentMapper {
    int deleteByPrimaryKey(Long iid);
    
    int deleteContentById(Long iadvertisingid);

    int insert(AdvertisingContent record);

    @Transactional
	int insertSelective(AdvertisingContent record);

    AdvertisingContent selectByPrimaryKey(Long iid);

    @Transactional
    int updateByPrimaryKeySelective(AdvertisingContent record);

    int updateByPrimaryKey(AdvertisingContent record);
    
    @Select("select a.* "
			+ "  from t_advertising_content a where a.iadvertisingid = #{0} ") 
    List<AdvertisingContent> getAdvertisingContentList(Long advertisingId);

    @Select("select a.* "
			+ "  from t_advertising_content a where a.iadvertisingid = #{0} and a.ilanguageid = #{1} limit 1") 
	AdvertisingContent getAdvertContentByAdvertIdAndLangId(
			Integer iadvertisingid, Integer ilanguageid);
 
}