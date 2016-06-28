package mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import entity.mobile.Setting;

public interface SettingMapper {

	@Insert({ "<script>insert into  t_setting (memberemail, uuid, modifydate, device, currentversion, imei, country, countryid, "
			+ "currency, currencyid, language, languageid) "
			+ "values (#{memberemail}, #{uuid}, #{modifydate},  #{device}, #{currentversion},  #{imei}, #{country}, "
			+ "#{countryid}, #{currency}, #{currencyid}, #{language}, #{languageid})</script>" })
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insert(Setting setting);

	@Select("select iid,memberemail,uuid,modifydate,device,currentversion,imei,country,countryid,currency,currencyid,language,languageid "
			+ " from t_setting where uuid=#{0}")
	Setting getSettingBymember(String uuid);

	@Update({ "<script>update t_setting set countryid = #{countryid},country = #{country},currencyid = #{currencyid},currency = #{currency},"
			+ "languageid = #{languageid},language = #{language}where uuid = #{uuid}</script>" })
	int updateByMember(Setting setting);

}
