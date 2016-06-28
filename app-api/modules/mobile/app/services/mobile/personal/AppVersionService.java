package services.mobile.personal;

import javax.inject.Inject;

import mapper.AppVersionMapper;
import dto.mobile.AppVersionInfo;

public class AppVersionService {

	@Inject
	AppVersionMapper appVersionMapper;

	/**
	 * 查询app最大版本
	 * 
	 * @return
	 */
	public AppVersionInfo getMaxAppVersion() {
		return appVersionMapper.getMaxAppVersion();
	}

	public AppVersionInfo getAppVersionByVs(String vs) {
		return appVersionMapper.getAppVersionByVs(vs);
	}

}
