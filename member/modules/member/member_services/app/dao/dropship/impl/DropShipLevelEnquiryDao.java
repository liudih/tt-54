package dao.dropship.impl;

import java.util.List;

import mapper.member.DropShipLevelMapper;

import com.google.inject.Inject;

import dao.dropship.IDropShipLevelEnquiryDao;
import dto.member.DropShipLevel;

public class DropShipLevelEnquiryDao implements IDropShipLevelEnquiryDao {
	@Inject
	DropShipLevelMapper dropShipLevelMapper;

	@Override
	public List<DropShipLevel> getDropShipLevels() {
		return dropShipLevelMapper.getDropShipLevels();
	}

	@Override
	public DropShipLevel getDropShipLevelById(Integer levelid) {
		return dropShipLevelMapper.getDropShipLevelById(levelid);
	}

	@Override
	public DropShipLevel getByTotalPrice(double total) {
		return dropShipLevelMapper.getByTotalPrice(total);
	}
}
