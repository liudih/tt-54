package services.dropship;

import play.Logger;

import com.google.inject.Inject;

import dao.dropship.IDropShipBaseEnquiryDao;
import dao.dropship.IDropShipBaseUpdateDao;
import dao.dropship.IDropShipLevelEnquiryDao;
import dto.member.DropShipBase;
import dto.member.DropShipLevel;

public class DropShipBaseUpdateService {
	@Inject
	IDropShipBaseUpdateDao dropShipBaseUpdateDao;
	@Inject
	IDropShipBaseEnquiryDao dropShipBaseEnquiryDao;
	@Inject
	IDropShipLevelEnquiryDao enquiryDao;

	public boolean addDropShipBase(DropShipBase dropShipBase) {
		return dropShipBaseUpdateDao.addDropShipBase(dropShipBase) > 0;
	}

	public boolean updateStatusByIid(Integer iid, Integer istatus) {
		return dropShipBaseUpdateDao.updateStatusByIid(iid, istatus) > 0;
	}

	public boolean updateLevelByIid(Integer iid, Integer levelId) {
		return dropShipBaseUpdateDao.updateLevelByIid(iid, levelId) > 0;
	}

	public boolean updateDropShip(DropShipBase dropShipBase) {
		return dropShipBaseUpdateDao.updateDropShip(dropShipBase) > 0;
	}

	public boolean updateLevelByEmail(String mail, Integer iwebsiteId,
			Double total) {
		DropShipBase dropShipBase = dropShipBaseEnquiryDao
				.getDropShipBaseByEmail(mail, iwebsiteId);
		if (null != dropShipBase && 1 == dropShipBase.getIstatus()) {
			Integer levelId = dropShipBase.getIdropshiplevel();
			DropShipLevel nowLevel = enquiryDao.getDropShipLevelById(levelId);
			Logger.debug(nowLevel.toString());
			DropShipLevel dropShipLevel = enquiryDao.getByTotalPrice(total);
			Logger.debug(dropShipLevel.toString());
			if (null != dropShipLevel && levelId != dropShipLevel.getIid()
					&& nowLevel.getFtotal() < dropShipLevel.getFtotal()) {
				return updateLevelByIid(dropShipBase.getIid(),
						dropShipLevel.getIid());
			}
		}
		return false;
	}
}
