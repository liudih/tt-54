package valueobjects.order_api;

import java.io.Serializable;

public class OrderCount  implements Serializable{
	private Integer all;
	private Integer pending;
	private Integer confirmed;
	private Integer onHold;
	private Integer processing;
	private Integer dispatched;
	private Integer cancelled;
	private Integer refunded;
	private Integer recycle;

	public Integer getAll() {
		return all;
	}

	public void setAll(Integer all) {
		this.all = all;
	}

	public Integer getPending() {
		return pending;
	}

	public void setPending(Integer pending) {
		this.pending = pending;
	}

	public Integer getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Integer confirmed) {
		this.confirmed = confirmed;
	}

	public Integer getOnHold() {
		return onHold;
	}

	public void setOnHold(Integer onHold) {
		this.onHold = onHold;
	}

	public Integer getDispatched() {
		return dispatched;
	}

	public void setDispatched(Integer dispatched) {
		this.dispatched = dispatched;
	}

	public Integer getCancelled() {
		return cancelled;
	}

	public void setCancelled(Integer cancelled) {
		this.cancelled = cancelled;
	}

	public Integer getRefunded() {
		return refunded;
	}

	public void setRefunded(Integer refunded) {
		this.refunded = refunded;
	}

	public Integer getProcessing() {
		return processing;
	}

	public void setProcessing(Integer processing) {
		this.processing = processing;
	}

	public Integer getRecycle() {
		return recycle;
	}

	public void setRecycle(Integer recycle) {
		this.recycle = recycle;
	}
}
