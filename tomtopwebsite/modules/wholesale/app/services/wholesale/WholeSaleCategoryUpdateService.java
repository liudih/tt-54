package services.wholesale;

import com.google.inject.Inject;

import dao.wholesale.IWholeSaleCategoryUpdateDao;
import entity.wholesale.WholeSaleCategory;

public class WholeSaleCategoryUpdateService {
	@Inject
	IWholeSaleCategoryUpdateDao wholeSaleCategoryUpdateDao;

	public boolean addWholeSaleCategory(WholeSaleCategory record) {
		return wholeSaleCategoryUpdateDao.addWholeSaleCategory(record) > 0;
	}

	public boolean deleteByWholeSaleId(Integer wholeSaleId) {
		return wholeSaleCategoryUpdateDao.deleteByWholeSaleId(wholeSaleId) > 0;
	}

}
