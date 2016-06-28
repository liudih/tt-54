package services.dropship;

import java.util.List;

import com.google.inject.Inject;

import dao.dropship.IDropShipLevelEnquiryDao;
import dto.member.DropShipLevel;

public class DropShipLevelEnquiryService {
	@Inject
	IDropShipLevelEnquiryDao dropShipLevelEnquiryDao;

	public List<DropShipLevel> getDropShipLevels() {
		return dropShipLevelEnquiryDao.getDropShipLevels();
	}

	public DropShipLevel getDropShipLevelById(Integer levelid) {
		return dropShipLevelEnquiryDao.getDropShipLevelById(levelid);
	}

	public DropShipLevel getByTotalPrice(double total) {
		return dropShipLevelEnquiryDao.getByTotalPrice(total);
	}
}
