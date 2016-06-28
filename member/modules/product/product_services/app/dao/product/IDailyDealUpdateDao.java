package dao.product;

import dao.IProductUpdateDao;
import dto.product.DailyDeal;

public interface IDailyDealUpdateDao extends IProductUpdateDao {
	public int insert(Integer iwebsiteid, String clistingid, String csku,
			String ccreateuse, boolean bvalid);

	public int updateDailyDealBvalid(Integer id, Boolean bvalid);

	public int insert(DailyDeal dailyDeal);
}
