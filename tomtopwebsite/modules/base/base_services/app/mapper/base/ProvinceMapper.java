package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.Province;

public interface ProvinceMapper {

	@Select("select iid, cname, cshortname from t_province where icountryid = #{id} ")
	public List<Province> getAllProvincesByCountryId(Integer id);

	@Select("select cname, cshortname from t_province where iid = #{id} limit 1")
	public Province getById(@Param("id") Integer id);
	
	@Select("select cshortname from t_province where lower(cname)=lower(trim(#{0})) and icountryid=#{1}")
	public Province getSnByName(String fullname, Integer countryid);
}
