package services.order;

import java.util.List;

import javax.inject.Inject;

import dao.order.IPreparatoryOrderEnquiryDao;
import dao.order.IPreparatoryOrderUpdateDao;
import dto.order.PreparatoryOrder;

public class PreparatoryOrderService implements IPreparatoryOrderService {
	@Inject
	private IPreparatoryOrderEnquiryDao enquiryDao;
	@Inject
	private IPreparatoryOrderUpdateDao updateDao;

	@Override
	public boolean insert(PreparatoryOrder order) {
		return updateDao.insert(order) == 1 ? true : false;
	}

	@Override
	public PreparatoryOrder getByID(int id) {
		return enquiryDao.getByID(id);
	}

	@Override
	public boolean updateNoByIDs(List<Integer> ids, int no) {
		if (ids == null || ids.isEmpty()) {
			return true;
		}
		return updateDao.updateNoByIDs(ids, no) == ids.size() ? true : false;
	}

	@Override
	public List<PreparatoryOrder> getByCartID(String cartID) {
		return enquiryDao.getByCartID(cartID);
	}

}
