package services.wholesale;

import javax.inject.Inject;

import dao.wholesale.IWholeSaleDiscountLevelUpdateDao;
import entity.wholesale.WholeSaleDiscountLevel;

public class WholeSaleDiscountLevelUpdateService {
	@Inject
	IWholeSaleDiscountLevelUpdateDao updateDao;

	public boolean update(WholeSaleDiscountLevel discount) {
		int i = updateDao.update(discount);
		return i == 1 ? true : false;
	}

	public boolean insert(WholeSaleDiscountLevel discount) {
		int i = updateDao.insert(discount);
		return i == 1 ? true : false;
	}

	public boolean delete(int id) {
		int i = updateDao.delete(id);
		return i == 1 ? true : false;
	}
}
