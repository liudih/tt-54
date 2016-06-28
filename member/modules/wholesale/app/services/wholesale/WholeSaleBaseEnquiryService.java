package services.wholesale;

import java.util.List;

import com.google.inject.Inject;

import dao.wholesale.IWholeSaleBaseEnquiryDao;
import entity.wholesale.WholeSaleBase;
import form.wholesale.WholeSaleSearchForm;

public class WholeSaleBaseEnquiryService {
	@Inject
	IWholeSaleBaseEnquiryDao wholeSaleBaseEnquiryDao;

	public WholeSaleBase getWholeSaleBaseByEmail(String email,
			Integer iwebsiteId) {
		return wholeSaleBaseEnquiryDao.getWholeSaleBaseByEmail(email,
				iwebsiteId);
	}

	public boolean checkWholeSaleBaseByEmail(String email, Integer iwebsiteId) {
		WholeSaleBase wholeSaleBase = wholeSaleBaseEnquiryDao
				.getWholeSaleBaseByEmail(email, iwebsiteId);
		if (null != wholeSaleBase && 1 == wholeSaleBase.getIstatus()) {
			return true;
		}

		return false;
	}

	public List<WholeSaleBase> getWholeSaleBasesBySearchForm(
			WholeSaleSearchForm wholeSaleSearchForm) {
		return wholeSaleBaseEnquiryDao
				.getWholeSaleBasesBySearchForm(wholeSaleSearchForm);
	}

	public Integer getWholeSaleBaseCount(WholeSaleSearchForm wholeSaleSearchForm) {
		return wholeSaleBaseEnquiryDao
				.getWholeSaleBaseCount(wholeSaleSearchForm);
	}

	public WholeSaleBase getWholeSaleBaseByIid(Integer iid) {
		return wholeSaleBaseEnquiryDao.getWholeSaleBaseByIid(iid);
	}
}
