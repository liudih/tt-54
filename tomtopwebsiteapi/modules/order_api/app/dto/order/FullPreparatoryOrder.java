package dto.order;

import java.util.Collection;

public class FullPreparatoryOrder {
	private PreparatoryOrder order;
	private Collection<PreparatoryDetail> details;

	public FullPreparatoryOrder() {
	}

	public FullPreparatoryOrder(PreparatoryOrder order,
			Collection<PreparatoryDetail> details) {
		super();
		this.order = order;
		this.details = details;
	}

	public PreparatoryOrder getOrder() {
		return order;
	}

	public void setOrder(PreparatoryOrder order) {
		this.order = order;
	}

	public Collection<PreparatoryDetail> getDetails() {
		return details;
	}

	public void setDetails(Collection<PreparatoryDetail> details) {
		this.details = details;
	}

}
