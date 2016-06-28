package dao.wholesale;

import dao.IWholeSaleUpdateDao;
import entity.wholesale.WholeSaleCategory;

public interface IWholeSaleCategoryUpdateDao extends IWholeSaleUpdateDao {
	public int deleteByWholeSaleId(Integer wholeSaleId);

	public int addWholeSaleCategory(WholeSaleCategory record);
}
