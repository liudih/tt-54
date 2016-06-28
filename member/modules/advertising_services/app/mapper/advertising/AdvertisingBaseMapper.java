package mapper.advertising;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.mybatis.guice.transactional.Transactional;

import valueobjects.product.ProductAdertisingContext;
import dto.advertising.Advertising;
import dto.advertising.AdvertisingBase;
import dto.advertising.AdvertisingType;
import dto.advertising.AdvertisingTypeForSelect;
import dto.advertising.Position;

public interface AdvertisingBaseMapper {
	int deleteByPrimaryKey(Long iid);

	int insert(AdvertisingBase record);

	@Transactional
	int insertSelective(AdvertisingBase record);

	AdvertisingBase selectByPrimaryKey(Long iid);

	@Transactional
	int updateByPrimaryKeySelective(AdvertisingBase record);

	int updateByPrimaryKey(AdvertisingBase record);

	/**
	 * 传入获取广告时需要的参数对象，输出广告对象集合
	 * 
	 * @param pac
	 *            参数对象
	 * @return 广告
	 */
	List<Advertising> getAdvertisingByContext(ProductAdertisingContext pac);

	@Select("SELECT ipositonid as iid, cpositonname as cname FROM t_advertising_positon")
	List<Position> getAllPositions();

	@Select("SELECT iadvertisingid as iid, cadvertisingname as cname FROM t_advertising_type")
	List<AdvertisingTypeForSelect> getAllAdvertisingTypes();
}