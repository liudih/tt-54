package dao.wholesale;

import dao.IWholeSaleUpdateDao;
import entity.wholesale.WholeSaleDiscountLevel;

public interface IWholeSaleDiscountLevelUpdateDao extends IWholeSaleUpdateDao {
	int update(WholeSaleDiscountLevel discount);

	int insert(WholeSaleDiscountLevel discount);

	int delete(int id);
}
