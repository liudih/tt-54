package dao.wholesale;

import dao.IWholeSaleUpdateDao;
import entity.wholesale.WholeSaleBase;

public interface IWholeSaleBaseUpdateDao extends IWholeSaleUpdateDao {
	public int addWholeSaleBase(WholeSaleBase record);

	public int updateStatusByIid(Integer iid, Integer istatus);

	public int updateWholeSaleBase(WholeSaleBase wholeSaleBase);
}
