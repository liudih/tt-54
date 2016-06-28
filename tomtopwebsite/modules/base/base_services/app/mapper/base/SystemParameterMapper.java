package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.SystemParameter;

public interface SystemParameterMapper {

	@Select("select * from t_system_parameter where iwebsiteid=#{0} "
			+ "and (ilanguageid=0 or ilanguageid is null) "
			+ "and cparameterkey=#{2} limit 1")
	SystemParameter getParameterValue(Integer iwebsiteid, Integer ilanguageid,
			String cparameterkey);

	@Select({
			"<script>",
			"select * from t_system_parameter where iwebsiteid=#{0} ",
			"and (ilanguageid=0 or ilanguageid is null) ",
			"and cparameterkey in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<SystemParameter> getParameterBykeys(Integer iwebsiteid,
			Integer ilanguageid, @Param("list") List<String> keys);

	@Select("select * from t_system_parameter")
	List<SystemParameter> getAllSysParameter();

	Integer addSysParameter(SystemParameter systemParameter);

	@Update("update t_system_parameter set iwebsiteid = #{iwebsiteid},ilanguageid=0,"
			+ "cparameterkey=#{cparameterkey},cparametervalue=#{cparametervalue} where iid = #{iid}")
	Integer alterSysParameter(SystemParameter systemParameter);

	@Delete("delete from t_system_parameter where iid = #{iid}")
	Integer deleteSysParameter(Integer iid);

	@Select("select * from t_system_parameter a where cparameterkey=#{0} and iwebsiteid = #{1} limit 1")
	SystemParameter validateKey(String key, Integer websiteId);
}
