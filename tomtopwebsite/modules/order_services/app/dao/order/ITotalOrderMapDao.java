package dao.order;

import java.util.List;

import dto.order.TotalOrderMap;

public interface ITotalOrderMapDao {
	List<TotalOrderMap> getByTotalID(Integer totalID);

	TotalOrderMap getByOrderID(Integer orderID);

	int batchInsert(List<TotalOrderMap> list);
}
