package services.order;

import java.util.List;

import dto.order.PreparatoryOrder;

public interface IPreparatoryOrderService {
	boolean insert(PreparatoryOrder order);

	PreparatoryOrder getByID(int id);

	boolean updateNoByIDs(List<Integer> ids, int no);

	List<PreparatoryOrder> getByCartID(String cartID);
}
