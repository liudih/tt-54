package dto.mobile;

import utils.ValidataUtils;

public class OrderClassCount {

	private Integer all;//所有订单
	private Integer ordering;//订单正在处理中（从成功收款到到发货这段时间）
	private Integer onHold;//订单审核中，比如可能这个单的付款有些问题，我们暂时hold住，不发货 
	private Integer dispatched;//订单已发货
	private Integer cancelled;//已取消
	private Integer refunded;//已退款
	private Integer pending;//待付款
	private Integer payin;//付款处理中
	private Integer confirmed;//收款成功 
	private Integer completed;//订单已完成
	private Integer recycle;//回收.内存在状态
	
	
	public Integer getAll() {
		return ValidataUtils.validataInt(all);
	}
	public void setAll(Integer all) {
		this.all = all;
	}
	public Integer getOrdering() {
		return ValidataUtils.validataInt(ordering);
	}
	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}
	public Integer getOnHold() {
		return ValidataUtils.validataInt(onHold);
	}
	public void setOnHold(Integer onHold) {
		this.onHold = onHold;
	}
	public Integer getDispatched() {
		return ValidataUtils.validataInt(dispatched);
	}
	public void setDispatched(Integer dispatched) {
		this.dispatched = dispatched;
	}
	public Integer getCancelled() {
		return ValidataUtils.validataInt(cancelled);
	}
	public void setCancelled(Integer cancelled) {
		this.cancelled = cancelled;
	}
	public Integer getRefunded() {
		return ValidataUtils.validataInt(refunded);
	}
	public void setRefunded(Integer refunded) {
		this.refunded = refunded;
	}
	public Integer getPending() {
		return ValidataUtils.validataInt(pending);
	}
	public void setPending(Integer pending) {
		this.pending = pending;
	}
	public Integer getPayin() {
		return ValidataUtils.validataInt(payin);
	}
	public void setPayin(Integer payin) {
		this.payin = payin;
	}
	public Integer getConfirmed() {
		return ValidataUtils.validataInt(confirmed);
	}
	public void setConfirmed(Integer confirmed) {
		this.confirmed = confirmed;
	}
	public Integer getCompleted() {
		return ValidataUtils.validataInt(completed);
	}
	public void setCompleted(Integer completed) {
		this.completed = completed;
	}
	public Integer getRecycle() {
		return ValidataUtils.validataInt(recycle);
	}
	public void setRecycle(Integer recycle) {
		this.recycle = recycle;
	}
	
	
}
