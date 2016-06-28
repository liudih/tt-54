package dao.order;

import dto.order.TotalOrder;

public interface ITotalOrderDao {
	TotalOrder getByID(Integer id);

	TotalOrder getByCID(String cid);

	int insert(String cid);
}
