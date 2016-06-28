package dto.mobile;

import utils.ValidataUtils;

public class UserInfo {

	private String email;// 邮件
	private String nick;// 昵称
	private Integer total;// 订单总数单数
	private Integer nopayqty;// 待付款单数
	private Integer confirmqty;// 成功付款单数
	private Integer dealqty;// 付款处理中单数
	private Integer dispatqty;// 订单已发货单数
	private Integer cancelledqty;// 订单取消单数
	private Integer refunqty;// 已退款单数
	private Integer msgqty;// 未读取消息数量

	public String getEmail() {
		return ValidataUtils.validataStr(email);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNick() {
		return ValidataUtils.validataStr(nick);
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getTotal() {
		total = getNopayqty() + getConfirmqty() + getDealqty() + getDispatqty()
				+ getCancelledqty() + getRefunqty();
		return ValidataUtils.validataInt(total);
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getNopayqty() {
		return ValidataUtils.validataInt(nopayqty);
	}

	public void setNopayqty(Integer nopayqty) {
		this.nopayqty = nopayqty;
	}

	public Integer getConfirmqty() {
		return ValidataUtils.validataInt(confirmqty);
	}

	public void setConfirmqty(Integer confirmqty) {
		this.confirmqty = confirmqty;
	}

	public Integer getDealqty() {
		return ValidataUtils.validataInt(dealqty);
	}

	public void setDealqty(Integer dealqty) {
		this.dealqty = dealqty;
	}

	public Integer getDispatqty() {
		return ValidataUtils.validataInt(dispatqty);
	}

	public void setDispatqty(Integer dispatqty) {
		this.dispatqty = dispatqty;
	}

	public Integer getCancelledqty() {
		return ValidataUtils.validataInt(cancelledqty);
	}

	public void setCancelledqty(Integer cancelledqty) {
		this.cancelledqty = cancelledqty;
	}

	public Integer getRefunqty() {
		return ValidataUtils.validataInt(refunqty);
	}

	public void setRefunqty(Integer refunqty) {
		this.refunqty = refunqty;
	}

	public Integer getMsgqty() {
		return ValidataUtils.validataInt(msgqty);
	}

	public void setMsgqty(Integer msgqty) {
		this.msgqty = msgqty;
	}
}
