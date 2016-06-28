package services.wholesale;

import com.google.inject.Inject;

import dao.wholesale.IWholeSaleBaseUpdateDao;
import entity.wholesale.WholeSaleBase;

public class WholeSaleBaseUpdateService {
	@Inject
	IWholeSaleBaseUpdateDao wholeSaleBaseUpdateDao;

	public boolean addWholeSaleBase(WholeSaleBase record) {
		return wholeSaleBaseUpdateDao.addWholeSaleBase(record) > 0;
	}

	public boolean updateStatusByIid(Integer iid, Integer istatus) {
		return wholeSaleBaseUpdateDao.updateStatusByIid(iid, istatus) > 0;
	}

	public boolean updateWholeSaleBase(WholeSaleBase wholeSaleBase) {
		return wholeSaleBaseUpdateDao.updateWholeSaleBase(wholeSaleBase) > 0;
	}
}
