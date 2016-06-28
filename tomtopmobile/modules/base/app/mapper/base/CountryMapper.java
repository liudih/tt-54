package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.Country;

public interface CountryMapper {

	@Select("select * from t_country where bshow=true order by ishowindex")
	List<Country> getAllCountry();
}
