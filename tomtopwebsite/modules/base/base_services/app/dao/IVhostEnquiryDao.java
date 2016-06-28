package dao;

import java.util.List;

import dao.IBaseEnquiryDao;
import dto.Vhost;

public interface IVhostEnquiryDao extends IBaseEnquiryDao{
	public List<String> getCvhost();
	
	public String getCdevice(String vhost);
	
	Integer getIlanguageIdByVhost(String vhost);
	
	List<Vhost> getAllDevice();
	
	public String getCorderplaceholder(String vhost);
	
	public List<String> getHostBySite(int site);
}
