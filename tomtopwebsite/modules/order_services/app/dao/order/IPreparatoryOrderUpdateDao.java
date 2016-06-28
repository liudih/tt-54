package dao.order;

import java.util.List;

import dao.IOrderUpdateDao;
import dto.order.PreparatoryOrder;

public interface IPreparatoryOrderUpdateDao extends IOrderUpdateDao {
	int insert(PreparatoryOrder order);

	int updateNoByIDs(List<Integer> ids, int no);
}
