package dao.wholesale;

import java.util.List;

import dao.IWholeSaleUpdateDao;
import entity.wholesale.WholeSaleProduct;

public interface IWholeSaleProductUpdateDao extends IWholeSaleUpdateDao {
	public int deleteByIid(Integer iid, String email);

	public int addWholeSaleProduct(WholeSaleProduct record);

	public int updateQtyByIid(Integer iid, Integer qty);
	
	public int batchDeleteByIid(List<Integer> ids, String email);
}
