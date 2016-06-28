package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.mobile.AppVersionDto;
import dto.mobile.AppVersionInfo;

public interface AppVersionMapper {

	@Select("select iid,vs,vid,descr,durl,createdate,createuser  "
			+ " from t_app_version where vid=(select max(vid) from t_app_version)")
	AppVersionInfo getMaxAppVersion();

	@Select("select iid,vs,vid,descr,durl,createdate,createuser  "
			+ " from t_app_version where vid=(select max(vid) from t_app_version)")
	AppVersionDto getMaxVersion();

	@Select("select iid,vs,vid,descr,durl,createdate,createuser  "
			+ " from t_app_version where vs=#{vs}")
	AppVersionInfo getAppVersionByVs(String vs);

	@Select("select iid,vs,vid,descr,durl,createdate,createuser  "
			+ " from t_app_version where vs=#{vs}")
	AppVersionDto getVersionByVs(String vs);

	@Select("select * from t_app_version order by iid desc ")
	List<AppVersionDto> getAllAppVersion();

	@Update("update t_app_version set vs = #{vs},vid=#{vid},"
			+ "descr=#{descr},durl=#{durl} where iid = #{iid}")
	Integer updateAppVersion(AppVersionDto appVersionDto);

	@Delete("delete from t_app_version where iid = #{iid}")
	Integer deleteAppVersion(Integer iid);

	@Insert("<script>insert into t_app_version (vs,vid,descr,durl,createdate,createuser) "
			+ "values(#{vs},#{vid},#{descr},#{durl},#{createdate},#{createuser})</script>")
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int addAppVersion(AppVersionDto appVersionDto);

}
