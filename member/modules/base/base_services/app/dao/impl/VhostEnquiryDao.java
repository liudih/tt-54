package dao.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.base.VhostMapper;
import dao.IVhostEnquiryDao;
import dto.Vhost;

public class VhostEnquiryDao implements IVhostEnquiryDao {
	@Inject
	VhostMapper vhostMapper;

	@Override
	public List<String> getCvhost() {
		return vhostMapper.getCvhost();
	}

	@Override
	public String getCdevice(String vhost) {
		return vhostMapper.getCdevice(vhost);
	}

	public List<Vhost> getAllDevice() {
		return vhostMapper.getAllDevice();
	}

	/**
	 * 获取在生成订单号时的占位符
	 * 
	 * @author lijun
	 * @param vhost
	 * @return
	 */
	public String getCorderplaceholder(String vhost) {
		return this.vhostMapper.getCorderplaceholder(vhost);
	}
	
	
	public Integer getIlanguageIdByVhost(String vhost) {
		return this.vhostMapper.getIlanguageIdByVhost(vhost);
	}
}
