package services.order;

import java.util.List;

import dto.order.PreparatoryDetail;

public interface IPreparatoryDetailService {
	boolean batchInsert(List<PreparatoryDetail> list);

	List<PreparatoryDetail> getByOrderID(int orderID);
}
