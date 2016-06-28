package valueobjects.payment;

import java.io.Serializable;

public class BraintreePaymentStatus implements Serializable{
	
	private int re; 
	
	private String msg;

	public String getMsg() {
		msg = msg==null ? "" : msg;
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getRe() {
		return re;
	}

	public void setRe(int re) {
		this.re = re;
	}
	
}
