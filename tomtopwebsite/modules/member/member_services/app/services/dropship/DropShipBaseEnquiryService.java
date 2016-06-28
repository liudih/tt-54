package services.dropship;

import java.util.List;

import com.google.inject.Inject;

import dao.dropship.IDropShipBaseEnquiryDao;
import dto.member.DropShipBase;
import form.dropship.DropShipSearchForm;

public class DropShipBaseEnquiryService {
	@Inject
	IDropShipBaseEnquiryDao dropShipBaseEnquiryDao;

	public List<DropShipBase> getDropShipBasesBySearch(
			DropShipSearchForm dropShipSearchForm) {
		return dropShipBaseEnquiryDao.getDropShipBases(
				dropShipSearchForm.getStatus(),
				dropShipSearchForm.getPageSize(),
				dropShipSearchForm.getPageNum(), dropShipSearchForm.getEmail(),
				dropShipSearchForm.getLevel());
	}

	public Integer getDropShipBasesCount(Integer status, String email,
			Integer level) {
		return dropShipBaseEnquiryDao.getDropShipBasesCount(status, email,
				level);
	}

	public DropShipBase getDropShipBaseByEmail(String email, Integer websiteId) {
		return dropShipBaseEnquiryDao.getDropShipBaseByEmail(email, websiteId);
	}

	public boolean checkoutDropShipBaseByEmail(String email, Integer websiteId) {
		DropShipBase dropShipBase = dropShipBaseEnquiryDao
				.getDropShipBaseByEmail(email, websiteId);
		if (null != dropShipBase && 1 == dropShipBase.getIstatus()) {
			return true;
		}
		return false;
	}
	
	public DropShipBase getDropShipBaseByIid(Integer iid) {
		return dropShipBaseEnquiryDao.getDropShipBaseByIid(iid);
	}
}
