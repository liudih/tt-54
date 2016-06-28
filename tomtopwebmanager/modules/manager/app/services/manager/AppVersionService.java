package services.manager;

import java.util.List;

import javax.inject.Inject;

import mapper.AppVersionMapper;
import play.Logger;
import dto.mobile.AppVersionDto;

public class AppVersionService {

	@Inject
	AppVersionMapper appVersionMapper;

	/**
	 * 查询app最大版本
	 * 
	 * @return
	 */
	public AppVersionDto getMaxAppVersion() {
		return appVersionMapper.getMaxVersion();
	}

	public AppVersionDto getAppVersionByVs(String vs) {
		return appVersionMapper.getVersionByVs(vs);
	}

	public List<AppVersionDto> getAllAppVersion() {
		return appVersionMapper.getAllAppVersion();
	}

	public AppVersionDto addAppVersion(AppVersionDto appVersionDto) {
		Integer result = appVersionMapper.addAppVersion(appVersionDto);
		Logger.debug("appVersionDto.iid ============== "
				+ appVersionDto.getIid());
		return result > 0 ? appVersionDto : null;
	}

	public boolean updateAppVersion(AppVersionDto appVersionDto) {
		Integer result = appVersionMapper.updateAppVersion(appVersionDto);
		Logger.debug("updateAppVersion ============== "
				+ appVersionDto.getIid());
		return result > 0;
	}

	public boolean deleteAppVersion(Integer iid) {
		Integer result = appVersionMapper.deleteAppVersion(iid);
		Logger.debug("deleteAppVersion ============== " + iid);
		return result > 0;
	}

}
