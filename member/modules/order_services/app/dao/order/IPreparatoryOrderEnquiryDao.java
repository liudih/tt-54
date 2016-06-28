package dao.order;

import java.util.List;

import dao.IOrderEnquiryDao;
import dto.order.PreparatoryOrder;

public interface IPreparatoryOrderEnquiryDao extends IOrderEnquiryDao {
	PreparatoryOrder getByID(int id);

	List<PreparatoryOrder> getByCartID(String cartID);
}
