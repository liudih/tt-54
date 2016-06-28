package services;

import java.util.List;

import dto.Vhost;

public interface IVhostService {

	public abstract List<String> getCvhost();

	public abstract String getCdevice(String vhost);
	
	public abstract List<Vhost> getAllDevice();
	
	/**
	 * @author lijun
	 * @param vhost
	 * @return
	 */
	public String getCorderplaceholder(String vhost);
	
	public Integer getIlanguageIdByVhost(String vhost);
	
	public List<String> getHostBySite(int site);
}