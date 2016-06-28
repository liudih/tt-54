package dao.order;

import java.util.List;

import dto.order.PreparatoryDetail;

public interface IPreparatoryDetailEnquiryDao {
	List<PreparatoryDetail> getByOrderID(int orderID);
}
