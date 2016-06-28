package dao.wholesale;

import java.util.List;

import dao.IWholeSaleUpdateDao;
import entity.wholesale.WholeSaleOrderProduct;

public interface IWholeSaleOrderProductUpdateDao extends IWholeSaleUpdateDao {

	int insert(WholeSaleOrderProduct product);

	int batchInsert(List<WholeSaleOrderProduct> list);
}
