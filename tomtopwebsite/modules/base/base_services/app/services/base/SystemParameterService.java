package services.base;

import interceptors.CacheResult;

import java.util.List;

import services.ISystemParameterService;
import mapper.base.SystemParameterMapper;

import com.google.inject.Inject;

import dto.SystemParameter;

public class SystemParameterService implements ISystemParameterService {

	@Inject
	SystemParameterMapper systemParameterMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ISystemParameterService#getSystemParameter(java.lang.Integer,
	 * java.lang.Integer, java.lang.String)
	 */
	@Override
	@CacheResult("system.parameter")
	public String getSystemParameter(Integer iwebsiteid, Integer ilanguageid,
			String cparameterkey) {
		SystemParameter sysparameter = systemParameterMapper.getParameterValue(
				iwebsiteid, ilanguageid, cparameterkey);
		return sysparameter == null ? null : sysparameter.getCparametervalue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ISystemParameterService#getSystemParameter(java.lang.Integer,
	 * java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	@CacheResult("system.parameter")
	public String getSystemParameter(Integer iwebsiteid, Integer ilanguageid,
			String cparameterkey, String defaultValue) {
		String value = getSystemParameter(iwebsiteid, ilanguageid,
				cparameterkey);
		return value == null ? defaultValue : value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ISystemParameterService#getSystemParameterAsInt(java.lang.Integer
	 * , java.lang.Integer, java.lang.String, int)
	 */
	@Override
	@CacheResult("system.parameter")
	public int getSystemParameterAsInt(Integer iwebsiteid, Integer ilanguageid,
			String cparameterkey, int defaultValue) {
		String value = getSystemParameter(iwebsiteid, ilanguageid,
				cparameterkey);
		return value == null ? defaultValue : Integer.parseInt(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ISystemParameterService#getSystemParameterAsDouble(java.lang
	 * .Integer, java.lang.Integer, java.lang.String, double)
	 */
	@Override
	@CacheResult("system.parameter")
	public double getSystemParameterAsDouble(Integer iwebsiteid,
			Integer ilanguageid, String cparameterkey, double defaultValue) {
		String value = getSystemParameter(iwebsiteid, ilanguageid,
				cparameterkey);
		return value == null ? defaultValue : Double.parseDouble(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ISystemParameterService#getAllSysParameter()
	 */
	@Override
	public List<SystemParameter> getAllSysParameter() {

		return systemParameterMapper.getAllSysParameter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ISystemParameterService#addSysParameter(entity.SystemParameter)
	 */
	@Override
	public SystemParameter addSysParameter(SystemParameter systemParameter) {

		Integer result = systemParameterMapper.addSysParameter(systemParameter);

		return result > 0 ? systemParameter : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ISystemParameterService#alterSysParameter(entity.SystemParameter
	 * )
	 */
	@Override
	public boolean alterSysParameter(SystemParameter systemParameter) {

		Integer result = systemParameterMapper
				.alterSysParameter(systemParameter);
		return result > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ISystemParameterService#deleteSysParameter(java.lang.Integer)
	 */
	@Override
	public boolean deleteSysParameter(Integer iid) {

		Integer result = systemParameterMapper.deleteSysParameter(iid);

		return result > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ISystemParameterService#validateKey(java.lang.String)
	 */
	@Override
	@CacheResult("system.parameter")
	public boolean validateKey(String key, Integer websiteId) {
		SystemParameter param = systemParameterMapper.validateKey(key, websiteId);

		return param == null ? true : false;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ISystemParameterService#getSysParameterByKeyAndSiteIdAndLanugageId
	 * (java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	@CacheResult("system.parameter")
	public SystemParameter getSysParameterByKeyAndSiteIdAndLanugageId(
			Integer siteId, Integer languageId, String cparameterkey) {
		return systemParameterMapper.getParameterValue(siteId, languageId,
				cparameterkey);
	}
	@Override
	public SystemParameter  getSystemParameterNoCacheByKey(String key,
			Integer websiteId) {
		return systemParameterMapper.validateKey(key, websiteId);
	}
}
