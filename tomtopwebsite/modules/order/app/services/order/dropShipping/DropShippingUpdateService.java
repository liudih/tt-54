package services.order.dropShipping;

import javax.inject.Inject;

import dao.order.IDropShippingUpdateDao;
import dto.order.dropShipping.DropShipping;

public class DropShippingUpdateService {
	@Inject
	private IDropShippingUpdateDao dropShippingUpdate;

	public boolean insert(DropShipping ds) {
		int i = dropShippingUpdate.insert(ds);
		return i == 1 ? true : false;
	}

	public boolean updateByID(DropShipping ds) {
		int i = dropShippingUpdate.updateByID(ds);
		return i == 1 ? true : false;
	}

	public boolean updateByDropShippingID(DropShipping ds) {
		int i = dropShippingUpdate.updateByDropShippingID(ds);
		return i == 1 ? true : false;
	}

	public boolean setUsedByDropShippingID(String dropShippingID) {
		int i = dropShippingUpdate
				.setUsedByDropShippingID(dropShippingID, true);
		return i == 1 ? true : false;
	}
}
