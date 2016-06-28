package services.order;

import java.util.List;

import javax.inject.Inject;

import dao.order.IPreparatoryDetailEnquiryDao;
import dao.order.IPreparatoryDetailUpdateDao;
import dto.order.PreparatoryDetail;

public class PreparatoryDetailService implements IPreparatoryDetailService {
	@Inject
	private IPreparatoryDetailEnquiryDao enquiryDao;
	@Inject
	private IPreparatoryDetailUpdateDao updateDao;

	@Override
	public boolean batchInsert(List<PreparatoryDetail> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		return updateDao.batchInsert(list) == list.size() ? true : false;
	}

	@Override
	public List<PreparatoryDetail> getByOrderID(int orderID) {
		return enquiryDao.getByOrderID(orderID);
	}

}
