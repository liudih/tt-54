package services.base;

import interceptors.CacheResult;

import java.util.List;

import javax.inject.Inject;

import services.IVhostService;
import dao.IVhostEnquiryDao;
import dao.impl.VhostEnquiryDao;
import dto.Vhost;

public class VhostService implements IVhostService {
	@Inject
	IVhostEnquiryDao vhostEnquiryDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.IVhostService#getCvhost()
	 */
	@Override
	public List<String> getCvhost() {
		return vhostEnquiryDao.getCvhost();
	}

	@Override
	public String getCdevice(String vhost) {
		return vhostEnquiryDao.getCdevice(vhost);
	}
	
	@CacheResult
	public Integer getIlanguageIdByVhost(String vhost) {
		return vhostEnquiryDao.getIlanguageIdByVhost(vhost);
	}

	@Override
	public List<Vhost> getAllDevice() {
		return vhostEnquiryDao.getAllDevice();
	}

	public String getCorderplaceholder(String vhost) {
		return this.vhostEnquiryDao.getCorderplaceholder(vhost);
	}
}
