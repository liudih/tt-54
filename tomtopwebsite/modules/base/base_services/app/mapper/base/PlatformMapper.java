package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.Platform;

public interface PlatformMapper {

	@Select("select * from t_platform where ccode = #{0} limit 1")
	Platform findByDomain(String domain);
	
	
	/**
	 * 获取所有平台数据
	 * @return List<Platform>
	 */
	@Select("select * from t_platform")
	List<Platform> getAllPlatform();

}
