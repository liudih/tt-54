package dao.order;

import java.util.List;

import dto.order.PreparatoryDetail;

public interface IPreparatoryDetailUpdateDao {
	int batchInsert(List<PreparatoryDetail> list);
}
