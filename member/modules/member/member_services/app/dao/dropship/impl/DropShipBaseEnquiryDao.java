package dao.dropship.impl;

import java.util.List;

import mapper.member.DropShipBaseMapper;

import com.google.inject.Inject;

import dao.dropship.IDropShipBaseEnquiryDao;
import dto.member.DropShipBase;

public class DropShipBaseEnquiryDao implements IDropShipBaseEnquiryDao {
	@Inject
	DropShipBaseMapper dropShipBaseMapper;

	@Override
	public List<DropShipBase> getDropShipBases(Integer status,
			Integer pageSize, Integer pageNum, String email, Integer level) {
		return dropShipBaseMapper.getDropShipBasesBySearch(status, pageSize,
				pageNum, email, level);
	}

	@Override
	public Integer getDropShipBasesCount(Integer status, String email,
			Integer level) {
		return dropShipBaseMapper.getDropShipBasesCount(status, email, level);
	}

	@Override
	public DropShipBase getDropShipBaseByEmail(String email, Integer websiteId) {
		return dropShipBaseMapper.getDropShipBaseByEmail(email, websiteId);
	}

	@Override
	public DropShipBase getDropShipBaseByIid(Integer iid) {
		return dropShipBaseMapper.getDropShipBaseByIid(iid);
	}

}
