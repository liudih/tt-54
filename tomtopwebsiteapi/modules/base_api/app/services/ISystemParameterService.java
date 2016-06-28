package services;

import java.util.List;

import dto.SystemParameter;

public interface ISystemParameterService {

	public abstract String getSystemParameter(Integer iwebsiteid,
			Integer ilanguageid, String cparameterkey);

	public abstract String getSystemParameter(Integer iwebsiteid,
			Integer ilanguageid, String cparameterkey, String defaultValue);

	public abstract int getSystemParameterAsInt(Integer iwebsiteid,
			Integer ilanguageid, String cparameterkey, int defaultValue);

	public abstract double getSystemParameterAsDouble(Integer iwebsiteid,
			Integer ilanguageid, String cparameterkey, double defaultValue);

	public abstract List<SystemParameter> getAllSysParameter();

	public abstract SystemParameter addSysParameter(
			SystemParameter systemParameter);

	public abstract boolean alterSysParameter(SystemParameter systemParameter);

	public abstract boolean deleteSysParameter(Integer iid);

	public abstract boolean validateKey(String key, Integer websiteId);

	public abstract SystemParameter getSysParameterByKeyAndSiteIdAndLanugageId(
			Integer siteId, Integer languageId, String cparameterkey);
	
	
	SystemParameter getSystemParameterNoCacheByKey(String key, Integer websiteId);

}