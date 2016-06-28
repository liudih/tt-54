package dao.dropship.impl;

import mapper.member.DropShipBaseMapper;

import com.google.inject.Inject;

import dao.dropship.IDropShipBaseUpdateDao;
import dto.member.DropShipBase;

public class DropShipBaseUpdateDao implements IDropShipBaseUpdateDao {
	@Inject
	DropShipBaseMapper dropShipBaseMapper;

	@Override
	public int addDropShipBase(DropShipBase record) {
		return dropShipBaseMapper.addDropShipBase(record);
	}

	@Override
	public int updateStatusByIid(Integer iid, Integer istatus) {
		return dropShipBaseMapper.updateStatusByIid(iid, istatus);
	}

	@Override
	public int updateLevelByIid(Integer iid, Integer levelId) {
		return dropShipBaseMapper.updateLevelByIid(iid, levelId);
	}

	@Override
	public int updateDropShip(DropShipBase dropShipBase) {
		return dropShipBaseMapper.updateDropShip(dropShipBase);
	}

}
